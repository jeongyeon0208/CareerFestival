package careerfestival.career.repository;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.enums.Category;
import careerfestival.career.domain.enums.KeywordName;
import careerfestival.career.domain.mapping.Participate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /*
     사용자가 등록한 행사 리스트 조회하기 위함.
     5단계의 행사 목록 List 형태로 제시
     */


    List<Event> findAllById(Long eventId);


    @Query(value = "SELECT * FROM event ORDER BY hits DESC LIMIT ?1", nativeQuery = true)
    List<Event> findAllByOrderByHitsDesc(int limit);
    @Query(value = "SELECT * FROM Event ORDER BY RAND() LIMIT ?1", nativeQuery = true)
    List<Event> findRandomEvents(int limit);
    @Query(value = "SELECT e FROM Event e WHERE e.category IN (?1) AND e.keywordName IN (?2) AND e.region.id = ?3")
    Page<Event> findAllByCategoryKeywordName(List<Category> category,
                                             List<KeywordName> keywordName,
                                             Long regionId,
                                             Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.region.id = :regionId ORDER BY e.hits DESC")
    List<Event> findRegionEvents(Long regionId);

    Page<Event> findPageByOrganizerId(Long organizerId, Pageable pageable);
    Event findByOrganizerId(Long organizerId);

    //마이페이지 주최자 용
    @Query(value = "SELECT e FROM Event e WHERE e.user IN (?1)")
    Page<Event> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}