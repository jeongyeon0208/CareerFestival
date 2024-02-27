package careerfestival.career.inquiry.service;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.mapping.Inquiry;
import careerfestival.career.inquiry.dto.InquiryRequestDto;
import careerfestival.career.inquiry.dto.InquiryResponseDto;
import careerfestival.career.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    public static final Long DEFAULT_USER_ID = 0L;


    public Long inquirySave(Long userId, Long eventId, InquiryRequestDto inquiryRequestDto) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);
        if (user.isPresent() && event.isPresent() && inquiryRequestDto.getCommentContent() != null) {
            String name = user.get().getName();
            if (inquiryRequestDto.getParent() != null) {
                // 대댓글인 경우
                List<Inquiry> parentComment = inquiryRepository.findByOrderNumber(inquiryRequestDto.getParent());

                if (!parentComment.isEmpty()) {
                    Inquiry lastParentComment = parentComment.get(parentComment.size() - 1); // 마지막 부모 댓글을 가져옴
                    Inquiry inquiry = inquiryRequestDto.toEntityWithParent(user.orElse(null), event.orElse(null), inquiryRequestDto.getCommentContent(), lastParentComment);

                    // 대댓글이므로 isParent를 false로 설정
                    inquiry.setIsParent(false);
                    inquiry.setOrderNumber(lastParentComment.getOrderNumber());
                    inquiry.setDepth(lastParentComment.getDepth() + 1);
                    inquiry.setName(name);
                    Inquiry savedComment = inquiryRepository.save(inquiry);
                    return savedComment.getId();
                } else {
                    // 부모 댓글이 없는 경우에는 예외 처리 또는 기본값 지정
                    throw new IllegalArgumentException("User, Event, or Comment Content is missing");
                }
            } else {
                // 댓글인 경우
                Inquiry inquiry = inquiryRequestDto.toEntity(user.orElse(null), event.orElse(null), inquiryRequestDto.getCommentContent());

                // 기존 코드에서의 위치 변경 및 isParent 값 설정
                inquiry.setIsParent(true); // 댓글이므로 isParent를 true로 설정
                inquiry.setOrderNumber( (inquiryRepository.findMaxOrderNumber() + 1));
                inquiry.setDepth(0);
                inquiry.setName(name);
                inquiry.setSecret(inquiryRequestDto.isSecret()); // Set the isSecret field
                Inquiry savedComment = inquiryRepository.save(inquiry);
                return savedComment.getId();
            }

        } else {
            throw new IllegalArgumentException("User, Event, or Comment Content is missing");
        }
    }


    public Page<InquiryResponseDto> getAllCommentsByEvent(
            Long userId,
            Long eventId, int pageStandard, int setOff) {
        if (userId == null) {
            userId = DEFAULT_USER_ID;
        }
        Optional<User> currentUserIdOptional = userRepository.findById(userId);

        Long user = currentUserIdOptional.map(User::getId).orElse(DEFAULT_USER_ID);
        List<Inquiry> comments = inquiryRepository.findAllLimitedParentCommentsWithRepliesByEventId(eventId, pageStandard, setOff);
        // 전체 댓글 중에서 부모 댓글만 추출
        List<Inquiry> parentComments = comments.stream()
                .filter(comment -> comment.isParent())
                .collect(Collectors.toList());

        // 부모 댓글 목록에서 대댓글을 찾아 대댓글도 함께 추가
        List<Inquiry> commentsWithReplies = parentComments.stream()
                .flatMap(parentComment -> {
                    List<Inquiry> replies = comments.stream()
                            .filter(reply -> !reply.isParent() && reply.getOrderNumber().equals(parentComment.getId()))
                            .collect(Collectors.toList());
                    return Stream.concat(Stream.of(parentComment), replies.stream());
                })
                .collect(Collectors.toList());

        Page<InquiryResponseDto> page = new PageImpl<>(
                commentsWithReplies.stream()
                        .map(comment ->apply(comment, user))
                        .collect(Collectors.toList()),
                PageRequest.of(setOff, pageStandard),
                commentsWithReplies.size()
        );

        return page;}


    private InquiryResponseDto apply(Inquiry comment, Long currentUserId) {
        InquiryResponseDto responseDto = new InquiryResponseDto(comment);
        if (comment.isSecret()) {
            Event event = eventRepository.findById(responseDto.getEventId()).get();

            if (Objects.equals(responseDto.getUserId(), currentUserId) || Objects.equals(event.getUser().getId(), currentUserId)) {
                responseDto.setSecreteMessage(null);
            } else {
                responseDto.setSecreteMessage("사용자가 비공개처리 했습니다.");
                responseDto.setCommentContent(null);
            }
        }
        return responseDto;
    }
}
