package careerfestival.career.wish.dto;

import careerfestival.career.domain.mapping.Wish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishResponseDto {
    private Long eventId;

    public WishResponseDto(Wish wish) {
        this.eventId = wish.getEvent().getId();
    }


}
