package careerfestival.career.organizer;

import careerfestival.career.domain.User;
import careerfestival.career.domain.mapping.Organizer;
import careerfestival.career.repository.OrganizerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizerService {

    private final OrganizerRepository organizerRepository;

    public int countRegisterdEvent(User user){
        Optional<Organizer> organizer = Optional.ofNullable(organizerRepository.findByUserId(user.getId()));
        if (organizer.isEmpty()) {
            return 0;
        }
        else return organizer.get().getCountEvent();
    }
}
