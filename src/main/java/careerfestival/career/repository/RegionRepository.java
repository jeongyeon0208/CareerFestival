package careerfestival.career.repository;

import careerfestival.career.domain.mapping.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Region findRegionByCityAndAddressLine(String city, String addressLine);
}