package careerfestival.career.mainPage.dto;

import careerfestival.career.domain.Event;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MainPageResponseDto {
    private String eventMainFileUrl;
    private String eventName;
    private LocalDateTime recruitmentStart;
    private LocalDateTime recruitmentEnd;
    private String eventCost;
    private String organizerProfileUrl;

    public static MainPageResponseDto fromEntity(Event event) {
        return MainPageResponseDto.builder()
                .eventMainFileUrl(event.getEventMainFileUrl())
                .eventName(event.getEventName())
                .recruitmentStart(event.getRecruitmentStart())
                .recruitmentEnd(event.getRecruitmentEnd())
                .eventCost(event.getEventCost())
                .organizerProfileUrl(event.getOrganizer().getOrganizerProfileFileUrl())
                .build();
    }

    public static MainPageResponseDto fromEntityName(Event event) {
        return MainPageResponseDto.builder()
                .eventName(event.getEventName())
                .build();
    }
}