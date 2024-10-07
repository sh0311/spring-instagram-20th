package com.ceos20.instagram.dm.service;


import com.ceos20.instagram.dm.domain.DmRoom;
import com.ceos20.instagram.dm.domain.Message;
import com.ceos20.instagram.dm.dto.DmRoomResponseDto;
import com.ceos20.instagram.dm.dto.MessageRequestDto;
import com.ceos20.instagram.dm.dto.MessageResponseDto;
import com.ceos20.instagram.dm.repository.DmRoomRepository;
import com.ceos20.instagram.dm.repository.MessageRepository;
import com.ceos20.instagram.global.exception.ExceptionCode;
import com.ceos20.instagram.global.exception.NotFoundException;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import com.ceos20.instagram.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class DmService {
    private final MessageRepository messageRepository;
    private final DmRoomRepository dmRoomRepository;
    private final UserService userService;

    // dm 보내기
    @Transactional
    public void sendMessage(MessageRequestDto messageRequestDto){
        //방이 안만들어져있다면 방 생성하기(DmRoomService 메소드 호출)
        User sender=userService.findUserById(messageRequestDto.getSenderId());
        User receiver=userService.findUserById(messageRequestDto.getReceiverId());
        DmRoom dmRoom=dmRoomRepository.findByUsers(sender, receiver).orElse(null);
        if(dmRoom==null){
            dmRoom=createRoom(sender, receiver);
        }
        //dto->entity
        Message newMessage=messageRequestDto.toEntity(messageRequestDto, dmRoom, sender); //연관관계 주인에 dmRoom 매핑

        messageRepository.save(newMessage);

        //dmRoom의 updated_at 갱신 (채팅방 정렬하기 위해)
        dmRoom.updateLastActivity();
        dmRoomRepository.save(dmRoom);
    }

    // 채팅방 생성
    private DmRoom createRoom(User sender, User receiver){
        DmRoom newRoom=DmRoom.builder()
                .user1(sender)  //처음 채팅 보낸 사람
                .user2(receiver)  //처음 채팅 받은 사람
                .user1LeaveTime(null) // 처음 만들어지면 첫 메시지부터 보이게 하기 위해
                .user2LeaveTime(null)
                .build();
        dmRoomRepository.save(newRoom);
        return newRoom;
    }



    // 채팅방 나가기
    @Transactional
    public void leaveRoom(Long roomId, Long userId){
        DmRoom targetRoom=dmRoomRepository.findById(roomId).orElseThrow(()->new NotFoundException(ExceptionCode.NOT_FOUND_ROOM));
        if(!targetRoom.isUserInRoom(userId)){
            throw new NotFoundException(ExceptionCode.NOT_FOUND_USER);
        }
        //유저가 채팅방에 존재하는 경우
        //떠나는 유저의 leaveTime 업데이트
        targetRoom.updateLeaveTime(userId);
    }



    // 최근 대화 오간순으로 내 채팅방 리스트 반환
    public List<DmRoomResponseDto> getMyAllRooms(Long userId){
        userService.findUserById(userId); //해당 id의 유저가 존재하는지 ㅔ크
        //내가 참여한 모든 채팅방 조회
        List<DmRoom> myRoomList=dmRoomRepository.findRoomsByUserIdOrderByUpdatedAtDesc(userId);
        //채팅방 리스트 엔티티 -> dto로
        List<DmRoomResponseDto> rooms=myRoomList.stream()
                .map(room-> DmRoomResponseDto.of(room,findOtherUser(userId, room).getNickname()))
                .toList();
        return rooms;
    }
    //채팅방 내 상대방 유저 찾기
    private User findOtherUser(Long userId,DmRoom room){
        return userId.equals(room.getUser1().getId())?room.getUser2():room.getUser1();
    }


    
    // 내가 보낸 dm삭제
    @Transactional
    public void deleteMessage(Long messageId){
        messageRepository.findById(messageId).orElseThrow(()->new NotFoundException(ExceptionCode.NOT_FOUND_MESSAGE));
        messageRepository.deleteById(messageId);
    }
    
    
    
    // 특정 dmRoom에 있는 메시지들 조회
    @Transactional  // updateIsRead보단 하나의 전체 프로세스를 관리하는 특정 서비스 메소드에 @Transactional 거는 게 좋다
    public List<MessageResponseDto> getMessagesInRoom(Long roomId, Long userId){
        DmRoom dmRoom=dmRoomRepository.findById(roomId).orElseThrow(()->new NotFoundException(ExceptionCode.NOT_FOUND_ROOM));
        userService.findUserById(userId);
        //해당 유저가 채팅방 나간시간 조회
        LocalDateTime userLeaveTime=userId.equals(dmRoom.getUser1().getId())?dmRoom.getUser1LeaveTime():dmRoom.getUser2LeaveTime();

        // userLeaveTime이 null이라면(채팅방 나간적 x) 모든 메시지 조회, 아니면 message의 생성시간이 leaveTime 이후인 message들만 조회
        List<Message> messages=(userLeaveTime==null)?messageRepository.findMessageWithSenderByRoomId(roomId):messageRepository.findMessageWithSenderByRoomIdAndCreatedAtAfter(roomId, userLeaveTime);

        //message의 isRead 필드값 true로 변경+읽은 시간 저장
        updateIsRead(messages);

        return messages.stream()
                .map(MessageResponseDto::from)
                .toList();

    }
    //message의 isRead 필드값 true로 변경+읽은 시간 저장
    private void updateIsRead(List<Message> messages){
        messages.forEach(message->{
            if(!message.isRead()){
                message.setRead();}
        });
    }
}
