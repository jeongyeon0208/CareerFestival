package careerfestival.career.eventPage.service;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.mapping.Statistics;
import careerfestival.career.eventPage.dto.EventPageResponseDto;
import careerfestival.career.repository.CommentRepository;
import careerfestival.career.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventPageService {
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    public List<EventPageResponseDto> getEvents(Long eventId) {
        // 조회수에 의한 정렬 처리 필요
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        return eventOptional.stream()
                .map(EventPageResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

//    public Statistics getStatics(Long eventId){
//        Optional<Event> findEvent = eventRepository.findById(eventId);
//        return findEvent.get().getStatistics();
//    }

}
