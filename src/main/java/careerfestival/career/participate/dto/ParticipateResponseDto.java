package careerfestival.career.participate.dto;

import careerfestival.career.domain.mapping.Participate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipateResponseDto {
    private Long userId;
    private Long eventId;

    public ParticipateResponseDto(Participate participate) {
        this.userId = participate.getUser().getId();
        this.eventId = participate.getEvent().getId();
    }
}
