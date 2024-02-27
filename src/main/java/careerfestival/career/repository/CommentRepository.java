package careerfestival.career.repository;

import careerfestival.career.domain.mapping.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByOrderNumber(Long orderNumber);

    @Query("SELECT COALESCE(MAX(c.orderNumber), 0) FROM Comment c WHERE c.parent IS NULL")
    Long findMaxOrderNumber();

    @Query(value = "(SELECT c.* " +
            "FROM comment c " +
            "WHERE c.event_id = :eventId " +
            "AND c.is_parent = 1 " +
            "ORDER BY c.order_number DESC , c.created_at DESC " +
            "LIMIT :pageSize OFFSET :offset) " +
            "UNION ALL " +
            "(SELECT c.* " +
            "FROM comment c " +
            "WHERE c.event_id = :eventId " +
            "AND c.is_parent = 0 " +
            "AND c.order_number IN ( " +
            "    SELECT c.id " +
            "    FROM comment c " +
            "    WHERE c.event_id = :eventId " +
            "    AND c.is_parent = 1 " +
            "ORDER BY order_number ASC, created_at ASC" +
            ")) " +
            "ORDER BY order_number DESC ", // 수정된 부분
            nativeQuery = true)
    List<Comment> findAllLimitedParentCommentsWithRepliesByEventId(
            @Param("eventId") Long eventId,
            @Param("pageSize") int pageSize,
            @Param("offset") int offset);

}