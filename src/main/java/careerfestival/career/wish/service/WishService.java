package careerfestival.career.wish.service;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.enums.Role;
import careerfestival.career.domain.mapping.Wish;
import careerfestival.career.participate.Exception.UserOrEventNotFoundException;
import careerfestival.career.repository.EventRepository;
import careerfestival.career.repository.UserRepository;
import careerfestival.career.repository.WishRepository;
import careerfestival.career.wish.dto.WishRequestDto;
import careerfestival.career.wish.dto.WishResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final WishRepository wishRepository;

    public boolean CheckWish(Long userId, Long eventId, WishRequestDto wishRequestDto){

        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (userOptional.isPresent() && eventOptional.isPresent()) {
            User user = userOptional.get();
            Event event = eventOptional.get();

            Wish check = wishRepository.findByUserIdAndEventId(user.getId(), event.getId()).orElse(null);
            if(user.getRole() == Role.ROLE_PARTICIPANT){
                if (check == null) {

                    Wish newWish = wishRequestDto.toEntity(user, event);
                    wishRepository.save(newWish);
                    return true;
                } else {
                    wishRepository.delete(check);
                    return false;
                }
            }
            return false;
        }else {
            throw new UserOrEventNotFoundException("User or Event not found");
        }

    }

    public List<WishResponseDto> getAllWishByEvent(String email, Long eventId) {
        User userOptional = userRepository.findByEmail(email);
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (userOptional != null && eventOptional.isPresent()) {
            User user = userOptional;
            Event event = eventOptional.get();

            List<Wish> wish = wishRepository.findByUser_IdAndEvent_Id(user.getId(), event.getId());

            return wish.stream()
                    .map(WishResponseDto::new)
                    .collect(Collectors.toList());
        } else {
            // 원하는 예외 처리 또는 에러 응답을 수행할 수 있습니다.
            throw new UserOrEventNotFoundException("User or Event not found");
        }
    }

}
