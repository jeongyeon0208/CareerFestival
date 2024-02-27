package careerfestival.career.eventPage.controller;

import careerfestival.career.comments.Service.CommentService;
import careerfestival.career.comments.dto.CommentResponseDto;
import careerfestival.career.config.SecurityConfig;
import careerfestival.career.domain.User;
import careerfestival.career.domain.mapping.Statistics;
import careerfestival.career.domain.mapping.StatisticsDto;
import careerfestival.career.eventPage.dto.EventPageResponseDto;
import careerfestival.career.eventPage.service.EventPageService;
import careerfestival.career.inquiry.dto.InquiryResponseDto;
import careerfestival.career.inquiry.service.InquiryService;
import careerfestival.career.jwt.JWTUtil;
import careerfestival.career.login.dto.CustomUserDetails;
import careerfestival.career.login.service.UserService;
import careerfestival.career.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor

public class EventPageController {
    private final EventPageService eventPageService;
    private final CommentService commentService;
    private final InquiryService inquiryService;
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @GetMapping("/event/{eventId}")
    public ResponseEntity<Map<String, Object>> getAllCommentsByEvent(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("eventId") Long eventId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "3") int size,
            @RequestParam(value = "index", defaultValue = "1") int index,
            @RequestParam(value = "standard", defaultValue = "3") int standard) {
        int pageSize = size;
        int pageStandard = standard;
        int offset = (page - 1) * pageSize;
        int setOff = (index - 1) * pageStandard;


        String email = (customUserDetails != null) ? customUserDetails.getUsername() : null;
        Long userId = (email != null) ? userRepository.findByEmail(email).getId() : null;
        // 페이징 정보를 생성합니다.
        // 댓글을 조회합니다.
        try {
            List<EventPageResponseDto> eventInformation = eventPageService.getEvents(eventId);

            // 수정된 부분: CommentService에서 수정한 메서드 사용
            Page<CommentResponseDto> comments = commentService.getAllCommentsByEvent(eventId, pageSize, offset);
            Page<InquiryResponseDto> inquiry = inquiryService.getAllCommentsByEvent(userId ,eventId, pageStandard, setOff);

            //통계자료 추가
//            Statistics statics = eventPageService.getStatics(eventId);

            // 댓글 정보를 응답으로 반환합니다.
            Map<String, Object> eventPage = new HashMap<>();
            eventPage.put("eventInformation", eventInformation);
            eventPage.put("comments", comments.getContent());  // Page의 getContent() 메서드를 사용하여 데이터 추출
            eventPage.put("currentPage", comments.getNumber());  // 현재 페이지 번호
            eventPage.put("totalPages", comments.getTotalPages());  // 전체 페이지 수
            eventPage.put("totalElements", comments.getTotalElements());  // 전체 요소 수
            eventPage.put("inquiry", inquiry.getContent());
//            eventPage.put("statics", statics);  //통계자료
            eventPage.put("currentIndex", inquiry.getNumber());
            eventPage.put("totalIndex", inquiry.getTotalPages());
            eventPage.put("totalElement", inquiry.getTotalElements());
            return ResponseEntity.ok()
                    .header("Location", "/event/" + eventId + "?page=" + (page + 1) + "&&index=" + (index + 1))
                    .body(eventPage);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Log the exception or return a more specific error response
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

