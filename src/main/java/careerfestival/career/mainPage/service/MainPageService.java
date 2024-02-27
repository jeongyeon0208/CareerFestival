package careerfestival.career.mainPage.service;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.enums.Category;
import careerfestival.career.domain.enums.KeywordName;
import careerfestival.career.domain.mapping.Organizer;
import careerfestival.career.domain.mapping.Region;
import careerfestival.career.login.dto.CustomUserDetails;
import careerfestival.career.mainPage.dto.MainPageFestivalListResponseDto;
import careerfestival.career.mainPage.dto.MainPageOrganizerListResponseDto;
import careerfestival.career.mainPage.dto.MainPageResponseDto;
import careerfestival.career.repository.EventRepository;
import careerfestival.career.repository.OrganizerRepository;
import careerfestival.career.repository.RegionRepository;
import careerfestival.career.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainPageService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final OrganizerRepository organizerRepository;
    private final RegionRepository regionRepository;

    public List<MainPageResponseDto> getEventsHitsDesc() {
        // 조회수에 의한 내림차순 정렬한 events
        List<Event> events = eventRepository.findAllByOrderByHitsDesc(6);
        
        return events.stream()
                .map(MainPageResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MainPageResponseDto> getEventNames() {
        // 조회수에 의한 정렬 처리 필요
        List<Event> eventNames = eventRepository.findAllByOrderByHitsDesc(6);

        return eventNames.stream()
                .map(MainPageResponseDto::fromEntityName)
                .collect(Collectors.toList());
    }

    public List<MainPageResponseDto> getEventsHitsRandom() {
        List<Event> events = eventRepository.findRandomEvents(3);

        return events.stream()
                .map(MainPageResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MainPageResponseDto> getEventsRegion(Region region) {
        Long regionId = regionRepository.findRegionByCityAndAddressLine(region.getCity(), region.getAddressLine()).getId();
        List<Event> events = eventRepository.findRegionEvents(regionId);

        return events.stream()
                .map(MainPageResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<MainPageFestivalListResponseDto> getEventsFiltered(List<Category> category,
                                                                   List<KeywordName> keywordName,
                                                                   Region region,
                                                                   Pageable pageable) {
        Long regionId = regionRepository.findRegionByCityAndAddressLine(region.getCity(), region.getAddressLine()).getId();
        Page<Event> events = eventRepository.findAllByCategoryKeywordName(category, keywordName, regionId, pageable);
        return events.map(MainPageFestivalListResponseDto::fromEventEntity);
    }

    public Page<MainPageFestivalListResponseDto> getOrganizersFiltered(Category category,
                                                                       KeywordName keywordName,
                                                                       Pageable pageable){
        Page<Organizer> organizers = organizerRepository.findAllByCategoryKeywordName(category, keywordName, pageable);
        return organizers.map(MainPageFestivalListResponseDto::fromOrganizerEntity);
    }



    public boolean findExistUserByCustomUserDetails(CustomUserDetails customUserDetails) {
        return userRepository.existsByEmail(customUserDetails.getUsername());
    }

    public String getUserName(String email) {
        User user = userRepository.findByEmail(email);
        Organizer organizer = organizerRepository.findByUserId(user.getId());
        if(organizer != null){
            return organizer.getOrganizerName();
        } else{
            return user.getName();
        }
    }


    public Region findRegion(String city, String addressLine) {
        return regionRepository.findRegionByCityAndAddressLine(city, addressLine);
    }

    public int getOrganizerCount() {
        return organizerRepository.countOrganizer();
    }

    public Page<MainPageOrganizerListResponseDto> getOrganizers(Pageable organizerPageable) {
        Page<Organizer> organizers = organizerRepository.findOrganizers(organizerPageable);
        return organizers.map(MainPageOrganizerListResponseDto::fromOrganizerEntity);
    }
}
