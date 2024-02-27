package careerfestival.career.inquiry.controller;

import careerfestival.career.domain.User;
import careerfestival.career.inquiry.dto.InquiryRequestDto;
import careerfestival.career.inquiry.service.InquiryService;
import careerfestival.career.login.dto.CustomUserDetails;
import careerfestival.career.login.service.UserService;
import careerfestival.career.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InquiryController{
    private final InquiryService inquiryService;
    private final UserRepository userRepository;
    private final UserService userService;
    public static final Long DEFAULT_USER_ID = 0L;


    @PostMapping("/event/{eventId}/inquiry")
    public ResponseEntity<Long> addInquiry(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody InquiryRequestDto inquiryRequestDto,
            @PathVariable("eventId") Long eventId){
        // Assuming you have the authenticated user's email
        // Replace this with the actual email

        if (customUserDetails == null) {
            // 로그인하지 않은 사용자는 로그인 페이지로 리다이렉트합니다.
            return ResponseEntity.ok()
                    .header("Location", "/login")
                    .build();
        }
        String email = customUserDetails.getUsername();
        Long userId = userRepository.findByEmail(email).getId();
        try {
            Long commentId = inquiryService.inquirySave(userId, eventId, inquiryRequestDto);

            // 리다이렉트를 위한 URL 생성
            String redirectUrl = "/event/" + eventId;

            // ResponseEntity로 리다이렉트 응답 생성
            return ResponseEntity.ok()
                    .header("Location", redirectUrl)
                    .body(commentId);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Log the exception or return a more specific error response
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
