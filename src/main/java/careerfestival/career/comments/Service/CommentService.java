    package careerfestival.career.comments.Service;

    import careerfestival.career.comments.dto.CommentRequestDto;
    import careerfestival.career.comments.dto.CommentResponseDto;
    import careerfestival.career.domain.mapping.Comment;
    import careerfestival.career.domain.Event;
    import careerfestival.career.domain.User;
    import careerfestival.career.repository.CommentLikeRepository;
    import careerfestival.career.repository.CommentRepository;
    import careerfestival.career.repository.EventRepository;
    import careerfestival.career.repository.UserRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageImpl;
    import org.springframework.data.domain.PageRequest;
    import java.util.stream.Stream;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;
    import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class CommentService {
        private final CommentRepository commentRepository;
        private final UserRepository userRepository;
        private final EventRepository eventRepository;
        private final CommentLikeRepository commentLikeRepository;

        public Long commentSave(Long userId, Long eventId, CommentRequestDto commentRequestDto) {
            Optional<User> user = userRepository.findById(userId);
            Optional<Event> event = eventRepository.findById(eventId);
            String name = user.get().getName();
            Long commentId = commentRequestDto.getId();
            if (user.isPresent() && event.isPresent() && commentRequestDto.getCommentContent() != null) {
                if (commentRequestDto.getParent() != null) {
                    // 대댓글인 경우
                    List<Comment> parentComment = commentRepository.findByOrderNumber(commentRequestDto.getParent());

                    if (!parentComment.isEmpty()) {
                        Comment lastParentComment = parentComment.get(parentComment.size() - 1); // 마지막 부모 댓글을 가져옴
                        Comment comment = commentRequestDto.toEntityWithParent(user.orElse(null), event.orElse(null), commentRequestDto.getCommentContent(), lastParentComment);

                        // 대댓글이므로 isParent를 false로 설정
                        comment.setIsParent(false);
                        comment.setOrderNumber(lastParentComment.getOrderNumber());
                        comment.setDepth(lastParentComment.getDepth() + 1);
                        comment.setName(name);
                        Comment savedComment = commentRepository.save(comment);
                        return savedComment.getId();
                    } else {
                        // 부모 댓글이 없는 경우에는 예외 처리 또는 기본값 지정
                        return null;
                    }
                } else {
                    // 댓글인 경우
                    Comment comment = commentRequestDto.toEntity(user.orElse(null), event.orElse(null), commentRequestDto.getCommentContent());

                    // 기존 코드에서의 위치 변경 및 isParent 값 설정
                    comment.setIsParent(true); // 댓글이므로 isParent를 true로 설정
                    comment.setOrderNumber( (commentRepository.findMaxOrderNumber() + 1));
                    comment.setDepth(0);
                    comment.setName(name);
                    Comment savedComment = commentRepository.save(comment);
                    return savedComment.getId();
                }
            } else {
                return null;
            }
        }

        public Page<CommentResponseDto> getAllCommentsByEvent(Long eventId, int pageSize, int offset) {
            List<Comment> comments = commentRepository.findAllLimitedParentCommentsWithRepliesByEventId(eventId, pageSize, offset);

            // 전체 댓글 중에서 부모 댓글만 추출
            List<Comment> parentComments = comments.stream()
                    .filter(comment -> comment.isParent())
                    .collect(Collectors.toList());

            // 부모 댓글 목록에서 대댓글을 찾아 대댓글도 함께 추가
            List<Comment> commentsWithReplies = parentComments.stream()
                    .flatMap(parentComment -> {
                        List<Comment> replies = comments.stream()
                                .filter(reply -> !reply.isParent() && reply.getOrderNumber().equals(parentComment.getId()))
                                .collect(Collectors.toList());
                        return Stream.concat(Stream.of(parentComment), replies.stream());
                    })
                    .collect(Collectors.toList());

            Page<CommentResponseDto> page = new PageImpl<>(
                    commentsWithReplies.stream()
                            .map(comment -> new CommentResponseDto(comment, commentLikeRepository))
                            .collect(Collectors.toList()),
                    PageRequest.of(offset, pageSize),
                    commentsWithReplies.size()
            );

            return page;}

    }


