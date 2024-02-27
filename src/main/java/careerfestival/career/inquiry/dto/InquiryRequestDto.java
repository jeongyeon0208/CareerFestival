package careerfestival.career.inquiry.dto;


import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.mapping.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryRequestDto {

    private Long userId;
    private Long eventId;
    private Long parent;  // Updated to Long type
    private String commentContent;
    private  Long orderNumber;
    private boolean isParent;
    private boolean secret;



    @Builder
    public static InquiryRequestDto of(Long userId, Long eventId, String commentContent, Long parent, Long orderNumber, boolean isParent, boolean secret)
    {
        return InquiryRequestDto.builder()
                .userId(userId)
                .eventId(eventId)
                .commentContent(commentContent)
                .parent(parent)
                .orderNumber(orderNumber)
                .isParent(isParent)
                .secret(secret)
                .build();
    }

    public Inquiry toEntity(User user, Event event, String commentContent) {
        return Inquiry.builder()
                .user(user)
                .event(event)
                .commentContent(commentContent)
                .build();
    }

    public Inquiry toEntityWithParent(User user, Event event, String commentContent, Inquiry parent) {
        return Inquiry.builder()
                .user(user)
                .event(event)
                .commentContent(commentContent)
                .parent(parent)
                .depth(parent.getDepth())
                .orderNumber(parent.getOrderNumber())
                .build();
    }
}
