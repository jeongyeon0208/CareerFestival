package careerfestival.career.myPage.dto;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.mapping.Organizer;
import careerfestival.career.domain.mapping.Participate;
import careerfestival.career.domain.mapping.Wish;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyPageEventResponseDto {
    private String eventMainFileUrl;
    private String eventName;
    private LocalDateTime recruitmentStart;
    private LocalDateTime recruitmentEnd;
    private String eventCost;
    private String organizerProfileUrl;

    public static MyPageEventResponseDto fromWish(Wish wish) {
        return MyPageEventResponseDto.builder()
                .eventMainFileUrl(wish.getEvent().getEventMainFileUrl())
                .eventName(wish.getEvent().getEventName())
                .recruitmentStart(wish.getEvent().getRecruitmentStart())
                .recruitmentEnd(wish.getEvent().getRecruitmentEnd())
                .eventCost(wish.getEvent().getEventCost())
                .organizerProfileUrl(wish.getEvent().getOrganizer().getOrganizerProfileFileUrl())
                .build();
    }

    public static MyPageEventResponseDto fromParticipate(Participate participate) {
        return MyPageEventResponseDto.builder()
                .eventMainFileUrl(participate.getEvent().getEventMainFileUrl())
                .eventName(participate.getEvent().getEventName())
                .recruitmentStart(participate.getEvent().getRecruitmentStart())
                .recruitmentEnd(participate.getEvent().getRecruitmentEnd())
                .eventCost(participate.getEvent().getEventCost())
                .organizerProfileUrl(participate.getEvent().getOrganizer().getOrganizerProfileFileUrl())
                .build();
    }

    public static MyPageEventResponseDto fromEvent(Event event) {
        return MyPageEventResponseDto.builder()
                .eventMainFileUrl(event.getEventMainFileUrl())
                .eventName(event.getEventName())
                .recruitmentStart(event.getRecruitmentStart())
                .recruitmentEnd(event.getRecruitmentEnd())
                .eventCost(event.getEventCost())
                .organizerProfileUrl(event.getOrganizer().getOrganizerProfileFileUrl())
                .build();
    }

}
