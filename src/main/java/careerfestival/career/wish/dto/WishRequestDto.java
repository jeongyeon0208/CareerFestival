package careerfestival.career.wish.dto;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.mapping.Wish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishRequestDto {

    private Long eventId;

    @Builder
    public static WishRequestDto of(Long eventId) {
        return WishRequestDto.builder()
                .eventId(eventId)
                .build();
    }

    public Wish toEntity(User user, Event event) {
        // toEntity 메서드를 통해 Participate 엔티티 생성
        return new Wish(user, event);
    }

}
