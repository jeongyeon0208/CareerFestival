package careerfestival.career.myPage.service;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.mapping.Participate;
import careerfestival.career.domain.mapping.Wish;
import careerfestival.career.myPage.dto.MyPageEventResponseDto;
import careerfestival.career.repository.EventRepository;
import careerfestival.career.repository.ParticipateRepository;
import careerfestival.career.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final WishRepository wishRepository;
    private final ParticipateRepository participateRepository;
    private final EventRepository eventRepository;

    public List<MyPageEventResponseDto> getWishEvent(User user) {
        List<Wish> wishList = wishRepository.findAllByUserOrderByCreatedAtDesc(user);

        return
                wishList.stream()
                        .map(MyPageEventResponseDto::fromWish)
                        .collect(Collectors.toList());
    }


    public List<MyPageEventResponseDto> getParticipateEvent(User user) {
        List<Participate> participateList = participateRepository.findAllByUserOrderByCreatedAtDesc(user);

        return
                participateList.stream()
                        .map(MyPageEventResponseDto::fromParticipate)
                        .collect(Collectors.toList());
    }

    public Page<MyPageEventResponseDto> getEventByUser(User user, Pageable pageable) {
        Page<Event> events = eventRepository.findAllByUserOrderByCreatedAtDesc(user, pageable);

        return events.map(MyPageEventResponseDto::fromEvent);
    }
}
