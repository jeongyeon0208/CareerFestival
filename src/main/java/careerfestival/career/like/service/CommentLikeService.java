package careerfestival.career.like.service;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.mapping.Comment;
import careerfestival.career.domain.mapping.CommentLike;
import careerfestival.career.like.dto.CommentLikeRequestDto;
import careerfestival.career.like.dto.CommentLikeResponseDto;
import careerfestival.career.participate.Exception.UserOrEventNotFoundException;
import careerfestival.career.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final EventRepository eventRepository;
    public boolean LikeSaveAndRemove(Long userId, Long eventId, Long commentId, CommentLikeRequestDto commentLikeRequestDto){
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (userOptional.isPresent() && eventOptional.isPresent() && commentOptional.isPresent()) {
            User user = userOptional.get();
            Event event = eventOptional.get();
            Comment comment = commentOptional.get();
            CommentLike check = commentLikeRepository.findByUserIdAndEventIdAndCommentId(user.getId(), event.getId(), comment.getId());
            if (check == null) {

                CommentLike newLike = commentLikeRequestDto.toEntity(user, event,comment);
                commentLikeRepository.save(newLike);
                return true;
            } else {
                commentLikeRepository.delete(check);
                return false;
            }
        }else {
            throw new UserOrEventNotFoundException("User or Event not found");
        }

    }

    public List<CommentLikeResponseDto> getAllCommentLikeByComment(Long userId, Long eventId ,Long commentId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (userOptional.isPresent() && commentOptional.isPresent()) {
            User user = userOptional.get();
            Comment comments = commentOptional.get();
            Event event = eventOptional.get();

            List<CommentLike> commentLikes = commentLikeRepository.findByUser_IdAndEvent_idAndComment_Id(user.getId(), event.getId(),comments.getId());

            return commentLikes.stream()
                    .map(CommentLikeResponseDto::new)
                    .collect(Collectors.toList());
        } else {
            // 원하는 예외 처리 또는 에러 응답을 수행할 수 있습니다.
            throw new UserOrEventNotFoundException("User or Event not found");
        }
    }
}
