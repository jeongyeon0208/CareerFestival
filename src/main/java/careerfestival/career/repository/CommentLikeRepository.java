package careerfestival.career.repository;

import careerfestival.career.domain.mapping.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Integer countByCommentId(Long commentId);
    List<CommentLike> findByUser_IdAndEvent_idAndComment_Id(Long user_id, Long event_id,Long comment_id);

    CommentLike findByUserIdAndEventIdAndCommentId(Long userId, Long eventId, Long commentId);
}
