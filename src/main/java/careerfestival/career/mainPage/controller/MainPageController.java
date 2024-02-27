package careerfestival.career.mainPage.controller;

import careerfestival.career.domain.enums.Category;
import careerfestival.career.domain.enums.KeywordName;
import careerfestival.career.domain.mapping.Region;
import careerfestival.career.login.dto.CustomUserDetails;
import careerfestival.career.mainPage.dto.MainPageFestivalListResponseDto;
import careerfestival.career.mainPage.dto.MainPageOrganizerListResponseDto;
import careerfestival.career.mainPage.dto.MainPageResponseDto;
import careerfestival.career.mainPage.service.MainPageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class MainPageController {
    private final MainPageService mainPageService;

    // 로그인 이후 리다이렉트 메인화면
    @Transactional
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> mainPage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Region region = null;
        if (customUserDetails != null){         // 로그인된 사용자일 때
            if(region != null){
                try{
                    // 1. 사용자 이름
                    String userName = mainPageService.getUserName(customUserDetails.getUsername());
                    // 2. 조회수에 의한 이벤트명
                    List<MainPageResponseDto> mainPageResponseDtoNames = mainPageService.getEventNames();
                    // 3. 조회수에 의한 정렬 리스트
                    List<MainPageResponseDto> mainPageResponseDtoViews = mainPageService.getEventsHitsDesc();
                    // 4. 지역 선택에 의한 행사 반환
                    List<MainPageResponseDto> mainPageResponseDtoRegions = mainPageService.getEventsRegion(region);

                    Map<String, Object> mainPageResponseDtoObjectMap = new HashMap<>();

                    mainPageResponseDtoObjectMap.put("userName", userName);
                    mainPageResponseDtoObjectMap.put("eventRegions", mainPageResponseDtoRegions);
                    mainPageResponseDtoObjectMap.put("eventViews", mainPageResponseDtoViews);
                    mainPageResponseDtoObjectMap.put("eventNames", mainPageResponseDtoNames);

                    return ResponseEntity.ok().body(mainPageResponseDtoObjectMap);
                } catch (IllegalArgumentException e){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            else{
                try{
                    // 1. 사용자 이름
                    String userName = mainPageService.getUserName(customUserDetails.getUsername());
                    // 2. 조회수에 의한 이벤트명
                    List<MainPageResponseDto> mainPageResponseDtoNames = mainPageService.getEventNames();
                    // 3. 조회수에 의한 정렬 리스트
                    List<MainPageResponseDto> mainPageResponseDtoViews = mainPageService.getEventsHitsDesc();
                    // 4. 랜덤에 의한 정렬 리스트
                    List<MainPageResponseDto> mainPageResponseDtoRandom = mainPageService.getEventsHitsRandom();

                    Map<String, Object> mainPageResponseDtoObjectMap = new HashMap<>();
                    mainPageResponseDtoObjectMap.put("userName", userName);
                    mainPageResponseDtoObjectMap.put("eventRandom", mainPageResponseDtoRandom);
                    mainPageResponseDtoObjectMap.put("eventViews", mainPageResponseDtoViews);
                    mainPageResponseDtoObjectMap.put("eventNames", mainPageResponseDtoNames);

                    return ResponseEntity.ok().body(mainPageResponseDtoObjectMap);
                } catch (IllegalArgumentException e){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
        } else {                                // 로그인되지 않은 사용자일 때
            if(region != null){
                try{
                    // 1. 조회수에 의한 이벤트명
                    List<MainPageResponseDto> mainPageResponseDtoNames = mainPageService.getEventNames();
                    // 2. 조회수에 의한 정렬 리스트
                    List<MainPageResponseDto> mainPageResponseDtoViews = mainPageService.getEventsHitsDesc();
                    // 3. 지역 선택에 의한 행사 반환
                    List<MainPageResponseDto> mainPageResponseDtoRegions = mainPageService.getEventsRegion(region);


                    Map<String, Object> mainPageResponseDtoObjectMap = new HashMap<>();
                    mainPageResponseDtoObjectMap.put("eventRegions", mainPageResponseDtoRegions);
                    mainPageResponseDtoObjectMap.put("eventViews", mainPageResponseDtoViews);
                    mainPageResponseDtoObjectMap.put("eventNames", mainPageResponseDtoNames);

                    return ResponseEntity.ok().body(mainPageResponseDtoObjectMap);
                } catch (IllegalArgumentException e){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            else{
                try{
                    // 1. 조회수에 의한 이벤트명
                    List<MainPageResponseDto> mainPageResponseDtoNames = mainPageService.getEventNames();
                    // 2. 조회수에 의한 정렬 리스트
                    List<MainPageResponseDto> mainPageResponseDtoViews = mainPageService.getEventsHitsDesc();
                    // 3. 랜덤에 의한 정렬 리스트
                    List<MainPageResponseDto> mainPageResponseDtoRandom = mainPageService.getEventsHitsRandom();

                    Map<String, Object> mainPageResponseDtoObjectMap = new HashMap<>();
                    mainPageResponseDtoObjectMap.put("eventRandom", mainPageResponseDtoRandom);
                    mainPageResponseDtoObjectMap.put("eventViews", mainPageResponseDtoViews);
                    mainPageResponseDtoObjectMap.put("eventNames", mainPageResponseDtoNames);

                    return ResponseEntity.ok().body(mainPageResponseDtoObjectMap);
                } catch (IllegalArgumentException e){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
        }
    }


    // 메인페이지에서 행사 목록 클릭 시 보여지는 화면
    // Category와 keywordName에 의해서 필터링되고 페이징 적용
    @GetMapping("/festival-list")
    public ResponseEntity<Map<String, Object>> getEventsFilter(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "category") List<Category> category,
            @RequestParam(value = "keywordName") List<KeywordName> keywordName,
            @RequestParam(value = "region") Region region,          // 지역은 1개에 대해서만 필터링 진행
            @PageableDefault(size = 9, sort = "hits", direction = Sort.Direction.DESC) Pageable pageable)
    {
        if (customUserDetails != null) {        // 로그인 된 사용자일 경우
            try{
                // 1. 사용자 이름
                String userName = mainPageService.getUserName(customUserDetails.getUsername());

                // 2. 필터링이 적용될 9개의 행사 리스트 조회
                Page<MainPageFestivalListResponseDto> mainPageFestivalListResponseDtos
                        = mainPageService.getEventsFiltered(category, keywordName, region, pageable);
                // 3. 조회수에 의한 이벤트명
                List<MainPageResponseDto> mainPageResponseDtoNames = mainPageService.getEventNames();

                // 4. (219명의 주최자) 정보 입력 공간
                int countOrganizer = mainPageService.getOrganizerCount();

                // 5. 주최자 정보 페이지 형태 반환
                Pageable organizerPageable = PageRequest.of(0, 10, Sort.by("countEvent").descending());
                Page<MainPageOrganizerListResponseDto> mainPageOrganizerListDtos = mainPageService.getOrganizers(organizerPageable);

                Map<String, Object> mainPageFestivalListResponseDtoObjectMap = new HashMap<>();

                // userName은 로그인한 사용자의 사용자명
                mainPageFestivalListResponseDtoObjectMap.put("userName", userName);
                // eventFilter는 메인페이지의 필터링이 적용된 페이지 행사 리스트들
                mainPageFestivalListResponseDtoObjectMap.put("eventFilter", mainPageFestivalListResponseDtos);
                // eventNames는 상단바에 위치한 조회수에 의한 행사명 리스트
                mainPageFestivalListResponseDtoObjectMap.put("eventNames", mainPageResponseDtoNames);
                // countOrganizer는 등록된 주최자 전체 인원 수 반환
                mainPageFestivalListResponseDtoObjectMap.put("countOrganizer", countOrganizer);
                // organizers는 보여질 주최자 인원 수
                mainPageFestivalListResponseDtoObjectMap.put("organizers", mainPageOrganizerListDtos);

                return ResponseEntity.ok().body(mainPageFestivalListResponseDtoObjectMap);
            } catch (IllegalArgumentException e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {                                // 로그인 하지 않은 사용자일 경우
            try{
                // 1. 조회순으로 보여지는 행사 9개
                Page<MainPageFestivalListResponseDto> mainPageFestivalListResponseDtos
                        = mainPageService.getEventsFiltered(category, keywordName, region, pageable);
                // 2. 조회수에 의한 이벤트명
                List<MainPageResponseDto> mainPageResponseDtoNames = mainPageService.getEventNames();
                // 3. (219명의 주최자) 정보 입력 공간
                int countOrganizer = mainPageService.getOrganizerCount();

                // 4. 주최자 정보 페이지 형태 반환
                Pageable organizerPageable = PageRequest.of(0, 10, Sort.by("countEvent").descending());
                Page<MainPageOrganizerListResponseDto> mainPageOrganizerListDtos = mainPageService.getOrganizers(organizerPageable);


                Map<String, Object> mainPageFestivalListResponseDtoObjectMap = new HashMap<>();
                // eventFilter는 메인페이지의 필터링이 적용된 페이지 행사 리스트들
                mainPageFestivalListResponseDtoObjectMap.put("eventFilter", mainPageFestivalListResponseDtos);
                // eventNames는 상단바에 위치한 조회수에 의한 행사명 리스트
                mainPageFestivalListResponseDtoObjectMap.put("eventNames", mainPageResponseDtoNames);
                // countOrganizer는 등록된 주최자 전체 인원 수 반환
                mainPageFestivalListResponseDtoObjectMap.put("countOrganizer", countOrganizer);
                // organizers는 보여질 주최자 인원 수
                mainPageFestivalListResponseDtoObjectMap.put("organizers", mainPageOrganizerListDtos);

                return ResponseEntity.ok().body(mainPageFestivalListResponseDtoObjectMap);
            } catch (IllegalArgumentException e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

    }
}
