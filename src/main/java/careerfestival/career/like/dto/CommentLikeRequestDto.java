package careerfestival.career.like.dto;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.mapping.Comment;
import careerfestival.career.domain.mapping.CommentLike;
import careerfestival.career.domain.mapping.Inquiry;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class CommentLikeRequestDto {

    public CommentLike toEntity(User user, Event event , Comment comment){
        return new CommentLike(user, event, comment);
    }
}
