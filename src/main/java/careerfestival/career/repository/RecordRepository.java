package careerfestival.career.repository;

import careerfestival.career.domain.Record;
import careerfestival.career.domain.mapping.RecordDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    Page<Record> findByUserId(Long userId, Pageable pageable);
    Record findRecordByUserId(Long userId);
    Record findRecordById(Long recordId);
    List<Record> findRecordsById(Long recordId);
}
