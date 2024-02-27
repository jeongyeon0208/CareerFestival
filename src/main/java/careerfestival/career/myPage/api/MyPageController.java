package careerfestival.career.myPage.api;

import careerfestival.career.domain.User;
import careerfestival.career.domain.enums.Role;
import careerfestival.career.login.dto.CustomUserDetails;
import careerfestival.career.login.service.UserService;
import careerfestival.career.myPage.dto.MyPageEventResponseDto;
import careerfestival.career.myPage.dto.MyPageOrganizerResponseDto;
import careerfestival.career.myPage.dto.MyPageUserInfoResponseDto;
import careerfestival.career.myPage.dto.UpdateMypageResponseDto;
import careerfestival.career.myPage.service.MyPageService;
import careerfestival.career.organizer.OrganizerService;
import careerfestival.career.repository.OrganizerRepository;
import careerfestival.career.repository.SubscribeRepository;
import careerfestival.career.subscribe.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final UserService userService;
    private final MyPageService myPageService;
    private final SubscribeService subscribeService;
    private final OrganizerService organizerService;

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> myPage (
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable)
    {
        try{
            //사용자 정보
            User findUser = userService.findUserByCustomUserDetails(customUserDetails);
            MyPageUserInfoResponseDto myPageUserInfoResponse = userService.fillMyPage(findUser);

            Map<String, Object> myPageResponeDtoObjectMap = new HashMap<>();
            myPageResponeDtoObjectMap.put("userInfo", myPageUserInfoResponse);

            //참가자인 경우
            if (findUser.getRole() == Role.ROLE_PARTICIPANT) {

                //내가 관심있는 행사
                List<MyPageEventResponseDto> wishEvent = myPageService.getWishEvent(findUser);

                //내가 참가 확정한 행사
                List<MyPageEventResponseDto> participateEvent = myPageService.getParticipateEvent(findUser);

                myPageResponeDtoObjectMap.put("wishEvent", wishEvent);
                myPageResponeDtoObjectMap.put("participateEvent", participateEvent);

                return ResponseEntity.ok().body(myPageResponeDtoObjectMap);

            }
            //주최자인 경우
            else {
                //구독자수, 등록한 행사 수
                int countedFollowers = subscribeService.countFollower(findUser);
                int countedEvents = organizerService.countRegisterdEvent(findUser);
                MyPageOrganizerResponseDto myPageOrganizerResponseDto = MyPageOrganizerResponseDto.builder()
                        .countFollower(countedFollowers)
                        .countEvent(countedEvents)
                        .build();


                //내가 주최한 행사
                Page<MyPageEventResponseDto> registerdEvent = myPageService.getEventByUser(findUser, pageable);

                myPageResponeDtoObjectMap.put("OrganizerInfo", myPageOrganizerResponseDto);
                myPageResponeDtoObjectMap.put("registerdEvent", registerdEvent);
                return ResponseEntity.ok().body(myPageResponeDtoObjectMap);
            }


        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> myPageUpdate  (
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable)
    {
        try{
            //사용자 정보
            User findUser = userService.findUserByCustomUserDetails(customUserDetails);
            MyPageUserInfoResponseDto myPageUserInfoResponse = userService.fillMyPage(findUser);

            Map<String, Object> myPageResponeDtoObjectMap = new HashMap<>();
            myPageResponeDtoObjectMap.put("userInfo", myPageUserInfoResponse);

            //참가자인 경우
            if (findUser.getRole() == Role.ROLE_PARTICIPANT) {

                //내가 관심있는 행사
                List<MyPageEventResponseDto> wishEvent = myPageService.getWishEvent(findUser);

                //내가 참가 확정한 행사
                List<MyPageEventResponseDto> participateEvent = myPageService.getParticipateEvent(findUser);

                myPageResponeDtoObjectMap.put("wishEvent", wishEvent);
                myPageResponeDtoObjectMap.put("participateEvent", participateEvent);

                return ResponseEntity.ok().body(myPageResponeDtoObjectMap);

            }
            //주최자인 경우
            else {
                //구독자수, 등록한 행사 수
                int countedFollowers = subscribeService.countFollower(findUser);
                int countedEvents = organizerService.countRegisterdEvent(findUser);
                MyPageOrganizerResponseDto myPageOrganizerResponseDto = MyPageOrganizerResponseDto.builder()
                        .countFollower(countedFollowers)
                        .countEvent(countedEvents)
                        .build();


                //내가 주최한 행사
                Page<MyPageEventResponseDto> registerdEvent = myPageService.getEventByUser(findUser, pageable);

                myPageResponeDtoObjectMap.put("OrganizerInfo", myPageOrganizerResponseDto);
                myPageResponeDtoObjectMap.put("registerdEvent", registerdEvent);
                return ResponseEntity.ok().body(myPageResponeDtoObjectMap);
            }


        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PatchMapping("/update")
    public ResponseEntity<Void> updateMember(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @RequestBody UpdateMypageResponseDto updateMypageResponseDto) {

        User findUser = userService.findUserByCustomUserDetails(customUserDetails);
        userService.findUserByEmailAndUpdate(findUser.getEmail(), updateMypageResponseDto);

        // 회원정보 수정 이후 리다이렉션할 URL 생성
        String redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/mypage")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", redirectUrl);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}

