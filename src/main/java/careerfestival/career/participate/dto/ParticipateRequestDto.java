package careerfestival.career.participate.dto;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.mapping.Participate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipateRequestDto {

    private Long userId;
    private Long eventId;
    @Builder
    public static ParticipateRequestDto of(Long userId, Long eventId) {
        return ParticipateRequestDto.builder()
                .userId(userId)
                .eventId(eventId)
                .build();
    }

    public Participate toEntity(User user, Event event) {
        // toEntity 메서드를 통해 Participate 엔티티 생성
        return new Participate(user, event);
    }
}
