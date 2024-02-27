package careerfestival.career.repository;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.enums.CompanyType;
import careerfestival.career.domain.enums.Gender;
import careerfestival.career.domain.mapping.Participate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipateRepository extends JpaRepository<Participate, Long> {
    List<Participate> findByUserAndEvent(User user, Event event);

    @Query(value = "SELECT p FROM Participate p WHERE p.user IN (?1) ORDER BY p.createdAt DESC")
    List<Participate> findAllByUserOrderByCreatedAtDesc(User user);

    int countByEvent_IdAndUser_Gender(Long eventId, Gender gender);

    int countByEvent_IdAndUser_Company(Long eventId, CompanyType companyType);

    int countByEvent_IdAndUser_AgeBetween(Long eventId, int startAge, int endAge);

}
