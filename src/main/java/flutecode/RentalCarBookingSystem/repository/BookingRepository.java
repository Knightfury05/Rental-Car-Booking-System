package flutecode.RentalCarBookingSystem.repository;

import flutecode.RentalCarBookingSystem.entity.BookingEntity;
import flutecode.RentalCarBookingSystem.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {
    List<BookingEntity> findByUser(UserEntity user);
}
