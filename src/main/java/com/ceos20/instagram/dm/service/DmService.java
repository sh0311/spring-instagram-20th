package com.ceos20.instagram.dm.service;


import com.ceos20.instagram.dm.domain.DmRoom;
import com.ceos20.instagram.dm.domain.Message;
import com.ceos20.instagram.dm.dto.DmRoomResponseDto;
import com.ceos20.instagram.dm.dto.MessageRequestDto;
import com.ceos20.instagram.dm.dto.MessageResponseDto;
import com.ceos20.instagram.dm.repository.DmRoomRepository;
import com.ceos20.instagram.dm.repository.MessageRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import com.ceos20.instagram.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User sender=messageRequestDto.getSender();
        User receiver=messageRequestDto.getReceiver();
        DmRoom dmRoom=dmRoomRepository.findByUser1AndUser2(sender, receiver).orElse(null);
        if(dmRoom==null){
            dmRoom=createRoom(sender, receiver);
        }
        Message newMessage=messageRequestDto.toMessage(messageRequestDto, dmRoom); //연관관계 주인에 dmRoom 매핑

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
                .build();
        dmRoomRepository.save(newRoom);
        return newRoom;
    }



    
    // 최근 대화 오간순으로 내 채팅방 리스트 반환
    public List<DmRoomResponseDto> getAllRooms(Long userId){
        userService.findUserById(userId); //해당 id의 유저가 존재하는지 ㅔ크
        //내가 참여한 모든 채팅방 조회
        List<DmRoom> myRoomList=dmRoomRepository.findRoomsByUserIdOrderByUpdatedAtDesc(userId);
        List<DmRoomResponseDto> rooms=myRoomList.stream()
                .map(room-> {User otherUser= room.getUser1().getId().equals(userId)?room.getUser2():room.getUser1();
                    return DmRoomResponseDto.of(room,otherUser.getNickname());
                })
                .toList();
        return rooms;
    }


    
    // 내가 보낸 dm삭제
    @Transactional
    public void deleteMessage(Long messageId){
        messageRepository.findById(messageId).orElseThrow(()->new IllegalArgumentException("해당 id의 메시지가 없습니다."));
        messageRepository.deleteById(messageId);
    }
    
    
    
    // 특정 dmRoom에 있는 메시지들 조회
    public List<MessageResponseDto> getMessagesInRoom(Long roomId){
        DmRoom dmRoom=dmRoomRepository.findById(roomId).orElseThrow(()->new IllegalArgumentException("해당 id의 dm방이 없습니다."));
        List<Message> messages=messageRepository.findMessageWithSenderByRoomId(roomId);
        //message의 isRead 필드값 true로 변경+읽은 시간 저장
        messages.forEach(message->{
            if(!message.isRead()){
                message.setRead();}
        });
        return messages.stream()
                .map(MessageResponseDto::from)
                .toList();

    }
}
