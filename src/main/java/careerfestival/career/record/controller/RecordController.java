package careerfestival.career.record.controller;

import careerfestival.career.login.dto.CustomUserDetails;
import careerfestival.career.record.dto.RecordRequestDto;
import careerfestival.career.record.service.RecordService;
import careerfestival.career.record.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/record")
@RestController
public class RecordController {
    private final RecordService recordService;

    // 기록장 게시 (사진 업로드 포함)
    @PostMapping(value = "/lecture-seminar")
    public ResponseEntity<Void> recordLectureSeminar(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                     HttpServletRequest request,
                                                     @RequestPart("lectureSeminarImage") MultipartFile lectureSeminarImage,
                                                     @RequestPart("recordRequestDto") RecordRequestDto recordRequestDto) {
        try {
            recordService.recordLectureSeminar(customUserDetails.getUsername(), lectureSeminarImage, recordRequestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    // conference 기록 추가
    @PostMapping("/conference")
    public ResponseEntity<Void> recordConference(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                 HttpServletRequest request,
                                                 @RequestPart("conferenceImage") List<MultipartFile> conferenceImage,
                                                 @RequestPart("recordRequestDto") RecordRequestDto recordRequestDto){
        try {
            recordService.recordConference(customUserDetails.getUsername(), conferenceImage, recordRequestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/exhibition")
    public ResponseEntity<Void> recordExhibition(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                 HttpServletRequest request,
                                                 @RequestPart("exhibitionImage") List<MultipartFile> exhibitionImage,
                                                 @RequestPart("recordRequestDto") RecordRequestDto recordRequestDto){
        try {
            recordService.recordExhibition(customUserDetails.getUsername(), exhibitionImage, recordRequestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/etc")
    public ResponseEntity<Void> recordEtc(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                          HttpServletRequest request,
                                          @RequestPart("etcImage") MultipartFile etcImage,
                                          @RequestPart("recordRequestDto") RecordRequestDto recordRequestDto) {
        try {
            recordService.recordEtc(customUserDetails.getUsername(), etcImage, recordRequestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }










    // 메인페이지
    @GetMapping("")             // ./record
    public ResponseEntity<Page<RecordResponseDto>> getRecordsByUserId(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 4, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            Page<RecordResponseDto> recordResponseDtos = recordService.recordList(customUserDetails.getUsername(), pageable);
            return ResponseEntity.ok(recordResponseDtos);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // 기록장 메인화면에서 하나의 기록장에 대해서 클릭했을 때 처리 - 프론트 페이지 나오고 나서 RequestParam 수정 여부 결정
    @GetMapping("/category")    // ./record/category
    public ResponseEntity<RecordResponseDto> getRecordByRecordId(
            @RequestParam(value = "recordId") Long recordId){
        try{
            RecordResponseDto recordResponseDto = recordService.getRecord(recordId);
            return ResponseEntity.ok(recordResponseDto);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}