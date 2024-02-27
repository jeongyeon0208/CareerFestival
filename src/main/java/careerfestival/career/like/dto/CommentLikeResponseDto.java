package careerfestival.career.like.dto;

import careerfestival.career.domain.mapping.Comment;
import careerfestival.career.domain.mapping.CommentLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentLikeResponseDto {
    private Long commentId;
    private Long eventId;
    public CommentLikeResponseDto(CommentLike commentLike){
        this.commentId = commentLike.getId();
    }


}
