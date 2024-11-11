package com.ceos20.instagram.refresh.scheduler;

import com.ceos20.instagram.refresh.domain.Refresh;
import com.ceos20.instagram.refresh.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ScheduledTask {
    private final RefreshRepository refreshRepository;

    // 날짜 형식에 맞게 변경

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

    @Scheduled(fixedRate = 24000 * 60 * 60) //24시간마다 수행
    public void scheduledDeleteRefresh(){

        List<Refresh> refreshTokens=refreshRepository.findAllByOrderByCreatedAtAsc();

        LocalDate current= LocalDate.now();

        //현재 날짜보다 이틀뒤에 발급된 refresh 토큰 삭제 (refresh토큰 유효기간을 하루로 내가 생성해서)
        for(Refresh refresh:refreshTokens){
            // String 타입의 expiration을 LocalDate로 변환
            LocalDate expirationDate = LocalDate.parse(refresh.getExpiration(), DATE_FORMATTER);

            if(ChronoUnit.DAYS.between(expirationDate, current) > 1){
                refreshRepository.delete(refresh);
            }
            else{
                break;
            }
        }

    }
}
