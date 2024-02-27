package careerfestival.career.register.dto;

import careerfestival.career.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterMainResponseDto {
    private String eventMainFileUrl;
    private String eventName;
    private LocalDateTime recruitmentStart;
    private LocalDateTime recruitmentEnd;
    private String eventCost;
    private String organizerProfileFileUrl;

    public static RegisterMainResponseDto fromEntity(Event event) {
        return RegisterMainResponseDto.builder()
                .eventMainFileUrl(event.getEventMainFileUrl())
                .eventName(event.getEventName())
                .recruitmentStart(event.getRecruitmentStart())
                .recruitmentEnd(event.getRecruitmentEnd())
                .eventCost(event.getEventCost())
                .organizerProfileFileUrl(event.getOrganizer().getOrganizerProfileFileUrl())
                .build();
    }
}
