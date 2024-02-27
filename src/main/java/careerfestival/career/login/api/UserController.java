package careerfestival.career.login.api;

import careerfestival.career.domain.User;
import careerfestival.career.domain.enums.Role;
import careerfestival.career.jwt.JWTUtil;
import careerfestival.career.login.dto.CustomUserDetails;
import careerfestival.career.login.dto.UserSignUpRequestDto;
import careerfestival.career.login.service.UserService;
import careerfestival.career.myPage.dto.UpdateMypageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    // 회원가입 1, 2 (이름, 이메일, 비밀번호, 비밀번호 확인, role)
    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<Void> signUp(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        User user = userService.signUp(userSignUpRequestDto);

        String token = jwtUtil.createJwt(user.getEmail(), String.valueOf(user.getRole()), 600000L);

        // 회원가입 이후 리다이렉션할 URL 생성
        if(Role.ROLE_PARTICIPANT.equals(user.getRole())){
            String redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/participant")
                    .toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            headers.add("Location", redirectUrl);

            return new ResponseEntity<>(headers, HttpStatus.OK); //200
        } else {
            String redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/organizer")
                    .toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            headers.add("Location", redirectUrl);

            return new ResponseEntity<>(headers, HttpStatus.OK); //200
        }
    }


    //회원가입3
        @Transactional
        @PatchMapping("/participant")
        public ResponseEntity<Void> updateParticipantDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody UpdateMypageResponseDto updateMypageResponseDto) {
            try {
                userService.findUserByEmailAndUpdate(customUserDetails.getUsername(), updateMypageResponseDto);

                // 정보 저장 이후 리다이렉션할 URL 생성
                String redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/login")
                        .toUriString();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", redirectUrl);
                return new ResponseEntity<>(headers, HttpStatus.OK);

            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); //400
            }
    }

    @Transactional
    @PatchMapping("/organizer")
    public ResponseEntity<Void> updateOrganizerDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody UpdateMypageResponseDto updateMypageResponseDto) {
        try {
            userService.findUserByEmailAndUpdate(customUserDetails.getUsername(), updateMypageResponseDto);

            // 정보 저장 이후 리다이렉션할 URL 생성
            String redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/login")
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", redirectUrl);
            return new ResponseEntity<>(headers, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); //400
        }
    }
}
