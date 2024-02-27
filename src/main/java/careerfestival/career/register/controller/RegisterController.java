package careerfestival.career.register.controller;

import careerfestival.career.domain.enums.Role;
import careerfestival.career.login.dto.CustomUserDetails;
import careerfestival.career.register.dto.RegisterEventDto;
import careerfestival.career.register.dto.RegisterMainResponseDto;
import careerfestival.career.register.dto.RegisterOrganizerDto;
import careerfestival.career.register.service.RegisterService;
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
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class RegisterController {
    private final RegisterService registerService;

    // 주최자 프로필 형성 (행사 등록하기 1단계)
    @PostMapping("/event/organizer")
    public ResponseEntity<Void>  registerOrganizer(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                HttpServletRequest request,
                                                @RequestPart("organizerProfileImage") MultipartFile organizerProfileImage,
                                                @RequestPart("registerOrganizerDto") RegisterOrganizerDto registerOrganizerDto) {
        // 주최자 프로필 생성하기 전에 검증
        if(Role.ROLE_ORGANIZER.equals(registerService.getUserRole(customUserDetails.getUsername()))){
            try{
                registerService.registerOrganizer(customUserDetails.getUsername(), organizerProfileImage, registerOrganizerDto);
                return new ResponseEntity(HttpStatus.OK);
            } catch (IllegalArgumentException e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // 행사 등록하기 1, 행사 등록하기 2 통합
    @PostMapping("/event/register")
    public ResponseEntity<Void> registerEvent(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        HttpServletRequest request,
                                        @RequestPart("eventMainImage") MultipartFile eventMainImage,
                                        @RequestPart("eventInformImage") MultipartFile eventInformImage,
                                        @RequestPart("registerEventDto") RegisterEventDto registerEventDto) {
        // 행사 등록하기 전에 검증
        if(Role.ROLE_ORGANIZER.equals(registerService.getUserRole(customUserDetails.getUsername()))){
            try{
                registerService.registerEvent(customUserDetails.getUsername(), eventMainImage, eventInformImage, registerEventDto);
                return new ResponseEntity(HttpStatus.OK);
            } catch (IllegalArgumentException e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }










    // 주최자가 자신의 프로필을 확인할 수 있음 (행사 등록하기 5단계) 바로
    @GetMapping("profile/register")
    public ResponseEntity<Map<String, Object>> getRegisterByOrganizerId(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 4, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        try{
            Long organizerId = registerService.getOrganizerId(customUserDetails.getUsername());
            String organizerName = registerService.getOrganizerName(organizerId);
            int getEventCount = registerService.getCountRegisterEvent(organizerId);
            Page<RegisterMainResponseDto> registerMainResponseDtos
                    = registerService.getEventList(organizerId, pageable);

            /*
            주최자를 구독하는 사람들의 인원수 반환 필요
             */

            Map<String, Object> registerMainResponseDtoObjectMap = new HashMap<>();
            registerMainResponseDtoObjectMap.put("organizerName", organizerName);
            // 구독자 관련 코드 추가 작성 필요
            registerMainResponseDtoObjectMap.put("festivalList", registerMainResponseDtos);
            registerMainResponseDtoObjectMap.put("festivalCount", getEventCount);

            return ResponseEntity.ok().body(registerMainResponseDtoObjectMap);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}