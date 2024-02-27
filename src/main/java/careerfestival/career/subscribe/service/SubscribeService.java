package careerfestival.career.subscribe.service;

import careerfestival.career.domain.User;
import careerfestival.career.domain.mapping.Subscribe;
import careerfestival.career.participate.Exception.UserOrEventNotFoundException;
import careerfestival.career.repository.SubscribeRepository;
import careerfestival.career.repository.UserRepository;
import careerfestival.career.subscribe.dto.SubscribeRequestDto;
import careerfestival.career.subscribe.dto.SubscribeResponseDto;
import careerfestival.career.wish.dto.WishResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
    public class SubscribeService {
    private final UserRepository userRepository;
    private final SubscribeRepository subscribeRepository;

    public boolean addRemove(SubscribeRequestDto subscribeRequestDto) {
        Long toUserId = subscribeRequestDto.getToUser();
        Long fromUserId = subscribeRequestDto.getFromUser();

        Optional<User> toUserOptional = userRepository.findById(toUserId);
        Optional<User> fromUserOptional = userRepository.findById(fromUserId);

        if (toUserOptional.isEmpty() || fromUserOptional.isEmpty()) {
            throw new UserOrEventNotFoundException("User not found");
        }

        User toUser = toUserOptional.get();
        User fromUser = fromUserOptional.get();

        Subscribe subscribe = subscribeRepository.findByFromUser_IdAndToUser_id(fromUserId, toUserId);
        if (subscribe == null) {

            subscribeRepository.save(new Subscribe(toUser, fromUser));
            return true;
        } else {
            subscribeRepository.delete(subscribe);
            return false;
        }

    }


        public List<SubscribeResponseDto> getAllSubscribeByUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserOrEventNotFoundException("User not found");
        } else {
            List<Subscribe> subscribes = subscribeRepository.findByFromUserId(userId);
            return subscribes.stream()
                    .map(SubscribeResponseDto::new)
                    .collect(Collectors.toList());
        }
    }

    public int countFollower (User user){
        int counted = subscribeRepository.findByFromUser(user);
        return counted;
    }
}
