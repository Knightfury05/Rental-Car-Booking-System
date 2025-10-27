package flutecode.RentalCarBookingSystem.repository;

import flutecode.RentalCarBookingSystem.entity.CarEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long> {
    // Pagination support
    Page<CarEntity> findAll(Pageable pageable);

    // Fetch cars by model and availability
    List<CarEntity> findByModelTypeAndAvailabilityStatus(String modelType, String availabilityStatus);
}
