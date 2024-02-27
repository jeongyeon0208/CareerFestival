package careerfestival.career.subscribe.controller;


import careerfestival.career.jwt.JWTUtil;
import careerfestival.career.register.service.RegisterService;
import careerfestival.career.subscribe.dto.SubscribeRequestDto;
import careerfestival.career.subscribe.dto.SubscribeResponseDto;
import careerfestival.career.subscribe.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubscribeController {
    private final JWTUtil jwtUtil;
    private final SubscribeService subscribeService;
    private final RegisterService registerService;

    // 구독하기
    @PostMapping("/profile/{fromUserId}/{toUserId}/subs")
    public ResponseEntity<Long> addSubs(
            @PathVariable Long fromUserId,
            @PathVariable Long toUserId,
            @RequestBody SubscribeRequestDto subscribeRequestDto) {


        try {
            boolean subsId = subscribeService.addRemove(subscribeRequestDto);
            String redirectUrl = "/profile/" + fromUserId;
            return ResponseEntity.ok()
                    .header("Location", redirectUrl)
                    .build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Log the exception or return a more specific error response
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/profile/{fromUserId}/{toUserId}/subs")
    public ResponseEntity<List<SubscribeResponseDto>> getAllSubscribeByUser(
            @PathVariable("fromUserId") Long fromUserId,
            @PathVariable("toUserId") Long toUserId) {

        try {
            List<SubscribeResponseDto> subs = subscribeService.getAllSubscribeByUser(toUserId);
            return new ResponseEntity<>(subs, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
