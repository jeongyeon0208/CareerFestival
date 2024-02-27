package careerfestival.career.participate.service;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.enums.CompanyType;
import careerfestival.career.domain.enums.Gender;
import careerfestival.career.domain.mapping.Participate;
import careerfestival.career.domain.mapping.StatisticsDto;
import careerfestival.career.participate.Exception.UserOrEventNotFoundException;
import careerfestival.career.participate.dto.ParticipateRequestDto;
import careerfestival.career.participate.dto.ParticipateResponseDto;
import careerfestival.career.repository.EventRepository;
import careerfestival.career.repository.ParticipateRepository;
import careerfestival.career.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipateService {
    private final ParticipateRepository participateRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public Long participateSave(Long userId, Long eventId, ParticipateRequestDto participateRequestDto) {

        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);
        System.out.println("event = " + event);
        Participate participate = participateRequestDto.toEntity(user.orElse(null), event.orElse(null));
        participateRepository.save(participate);

        updateStatics(eventId);

        return participate.getId();
    }

    public List<ParticipateResponseDto> getAllParticipateByEvent(Long userId, Long eventId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (userOptional.isPresent() && eventOptional.isPresent()) {
            User user = userOptional.get();
            Event event = eventOptional.get();

            List<Participate> participates = participateRepository.findByUserAndEvent(user, event);

            return participates.stream()
                    .map(ParticipateResponseDto::new)
                    .collect(Collectors.toList());
        } else {
            // 원하는 예외 처리 또는 에러 응답을 수행할 수 있습니다.
            throw new UserOrEventNotFoundException("User or Event not found");
        }
    }

    public String getLink(Long eventId){
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null){
            if(event.getLink() != null){
                return event.getLink();
            }
            return null;
        }
        else {
            // 원하는 예외 처리 또는 에러 응답을 수행할 수 있습니다.
            throw new UserOrEventNotFoundException("Event not found");
        }
    }

    public void updateStatics(Long eventId){

        int countedMale = participateRepository.countByEvent_IdAndUser_Gender(eventId, Gender.남성);
        int countedFemale = participateRepository.countByEvent_IdAndUser_Gender(eventId, Gender.여성);

        int countedUnder19 = participateRepository.countByEvent_IdAndUser_AgeBetween(eventId, 0, 19);
        int counted20to24 = participateRepository.countByEvent_IdAndUser_AgeBetween(eventId, 20, 24);
        int counted25to29 = participateRepository.countByEvent_IdAndUser_AgeBetween(eventId, 25, 29);
        int counted30to34 = participateRepository.countByEvent_IdAndUser_AgeBetween(eventId, 30, 34);
        int counted35to40 = participateRepository.countByEvent_IdAndUser_AgeBetween(eventId, 35, 40);
        int countedOver40 = participateRepository.countByEvent_IdAndUser_AgeBetween(eventId, 40, 100);

        int countedCompanyType1 = participateRepository.countByEvent_IdAndUser_Company(eventId, CompanyType.미성년자);
        int countedCompanyType2 = participateRepository.countByEvent_IdAndUser_Company(eventId, CompanyType.학부생);
        int countedCompanyType3 = participateRepository.countByEvent_IdAndUser_Company(eventId, CompanyType.졸업생);
        int countedCompanyType4 = participateRepository.countByEvent_IdAndUser_Company(eventId, CompanyType.대학원생);
        int countedCompanyType5 = participateRepository.countByEvent_IdAndUser_Company(eventId, CompanyType.무직);
        int countedCompanyType6 = participateRepository.countByEvent_IdAndUser_Company(eventId, CompanyType.기타);

        StatisticsDto statisticsDto = StatisticsDto.builder()
                .countedMale(countedMale)
                .countedFemale(countedFemale)

                .countedUnder19(countedUnder19)
                .counted20to24(counted20to24)
                .counted25to29(counted25to29)
                .counted30to34(counted30to34)
                .counted35to39(counted35to40)
                .countedOver40(countedOver40)

                .countedCompanyType1(countedCompanyType1)
                .countedCompanyType2(countedCompanyType2)
                .countedCompanyType3(countedCompanyType3)
                .countedCompanyType4(countedCompanyType4)
                .countedCompanyType5(countedCompanyType5)
                .countedCompanyType6(countedCompanyType6)
                .build();

        Optional<Event> findEvent = eventRepository.findById(eventId);
        boolean present = findEvent.isPresent();
        if(present){
//            findEvent.get().updateStatistics(statisticsDto);
        }
    }
}

