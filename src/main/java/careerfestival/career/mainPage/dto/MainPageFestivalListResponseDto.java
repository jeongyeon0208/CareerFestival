package careerfestival.career.mainPage.dto;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.mapping.Organizer;
import careerfestival.career.domain.mapping.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPageFestivalListResponseDto {
    private String eventMainFileUrl;
    private String eventName;
    private LocalDateTime recruitmentStart;
    private LocalDateTime recruitmentEnd;
    private String eventCost;

    // 주최자 정보 조회 위함
    private String organizerName;
    private String organizerProfileFileUrl;

    // 지역 필터링 위함
    private String city;
    private String addressLine;

    public static MainPageFestivalListResponseDto fromEventEntity(Event event){
        return MainPageFestivalListResponseDto.builder()
                .eventMainFileUrl(event.getEventMainFileUrl())
                .eventName(event.getEventName())
                .recruitmentStart(event.getRecruitmentStart())
                .recruitmentEnd(event.getRecruitmentEnd())
                .eventCost(event.getEventCost())
                .build();
    }

    public static MainPageFestivalListResponseDto fromOrganizerEntity(Organizer organizer){
        return MainPageFestivalListResponseDto.builder()
                .organizerName(organizer.getOrganizerName())
                .organizerProfileFileUrl(organizer.getOrganizerProfileFileUrl())
                .build();
    }
}
