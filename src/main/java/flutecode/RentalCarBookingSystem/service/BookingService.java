package flutecode.RentalCarBookingSystem.service;

import flutecode.RentalCarBookingSystem.entity.BookingEntity;
import flutecode.RentalCarBookingSystem.entity.CarEntity;
import flutecode.RentalCarBookingSystem.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public BookingEntity saveBooking(BookingEntity booking) {
        long days = ChronoUnit.DAYS.between(booking.getFromDate(), booking.getToDate());
        if (days <= 0) days = 1;
        booking.setTotalDays((int) days);

        CarEntity car = booking.getCar();
        if (car != null && car.getPrice() != null) {
            booking.setTotalPrice(days * car.getPrice().doubleValue());
        } else {
            booking.setTotalPrice(0.0);
        }

        return bookingRepository.save(booking);
    }

    public List<BookingEntity> getAllBookings() {
        return bookingRepository.findAll();
    }
}
