package careerfestival.career.participate.controller;

import careerfestival.career.participate.dto.ParticipateRequestDto;
import careerfestival.career.participate.dto.ParticipateResponseDto;
import careerfestival.career.participate.service.ParticipateService;
import careerfestival.career.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParticipateController {
    private final ParticipateService participateService;
    private final EventRepository eventRepository;

    @PostMapping("/event/{eventId}/{userId}/participate")
    public ResponseEntity<Long> addParticipate(
            @PathVariable("userId") Long userId,
            @PathVariable("eventId") Long eventId,
            @RequestBody ParticipateRequestDto participateRequestDto) {
        // Assuming you have the authenticated user's email
        String userEmail = "user@example.com"; // Replace this with the actual email

        try {
            Long participateId = participateService.participateSave(userId, eventId, participateRequestDto);
            // 리다이렉트를 위한 URL 생성
            String redirectUrl = "/event/" + userId + "/" + eventId;

            // ResponseEntity로 리다이렉트 응답 생성
            return ResponseEntity.ok()
                    .header("Location", redirectUrl)
                    .body(participateId);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Log the exception or return a more specific error response
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/event/{eventId}/{userId}/participate")
    public ResponseEntity<List<ParticipateResponseDto>> getAllParticipateByEvent(
            @PathVariable("userId") Long userId,
            @PathVariable("eventId") Long eventId) {
        String userEmail = "user@example.com"; // Replace this with the actual email
        String name = "test";



        try {
            List<ParticipateResponseDto> participates = participateService.getAllParticipateByEvent(userId, eventId);
            // 리다이렉트를 위한 URL 생성
            return new ResponseEntity<>(participates, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Log the exception or return a more specific error response
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/event/{eventId}/link")
    public String getLink(
            @PathVariable("eventId")  Long eventId){
        try {
            String link = participateService.getLink(eventId);
            if (link != null) {
                // 리다이렉트 구현
                String redirectUrl = link;
                return ResponseEntity.ok()
                        .header("Location", redirectUrl)
                        .body(link).toString();
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Location", "/event/" + eventId)
                        .body("Link is Null").toString();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();
        } catch (Exception e) {
            // 로그 남기기
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).toString();
        }
    }
}
