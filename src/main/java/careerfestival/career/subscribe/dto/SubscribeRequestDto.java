package careerfestival.career.subscribe.dto;


import careerfestival.career.domain.User;
import careerfestival.career.domain.mapping.Subscribe;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor


public class SubscribeRequestDto {

    private Long toUser;
    private Long fromUser;
    private Integer subscriberCount;
    @Builder
    public static SubscribeRequestDto of(Long toUser, Long fromUser, Integer subscriberCount) {
        return SubscribeRequestDto.builder()
                .toUser(toUser)
                .fromUser(fromUser)
                .subscriberCount(subscriberCount)
                .build();
    }


}
