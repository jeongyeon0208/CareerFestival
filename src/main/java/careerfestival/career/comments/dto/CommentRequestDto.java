package careerfestival.career.comments.dto;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.mapping.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDto {
    private Long id;
    private Long userId;
    private Long eventId;
    private Long parent;  // Updated to Long type
    private String commentContent;
    private  Long orderNumber;
    private boolean isParent;
    private LocalDateTime createdAt;
    private String Name;

    @Builder
    public static CommentRequestDto of(Long id, Long userId, Long eventId, String commentContent, Long parent, Long orderNumber, boolean isParent, LocalDateTime createdAt, String name)
 {
        return CommentRequestDto.builder()
                .id(id)
                .userId(userId)
                .eventId(eventId)
                .commentContent(commentContent)
                .parent(parent)
                .orderNumber(orderNumber)
                .isParent(isParent)
                .createdAt(LocalDateTime.now())
                .Name(name)
                .build();
    }

    public Comment toEntity(User user, Event event, String commentContent) {
        return Comment.builder()
                .user(user)
                .event(event)
                .commentContent(commentContent)
                .build();
    }

    public Comment toEntityWithParent(User user, Event event, String commentContent, Comment parent) {
        return Comment.builder()
                .user(user)
                .event(event)
                .commentContent(commentContent)
                .parent(parent)
                .depth(parent.getDepth())
                .orderNumber(parent.getOrderNumber())
                .build();
    }
}
