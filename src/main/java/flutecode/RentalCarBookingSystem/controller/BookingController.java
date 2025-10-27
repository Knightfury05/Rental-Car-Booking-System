package flutecode.RentalCarBookingSystem.controller;

import flutecode.RentalCarBookingSystem.entity.BookingEntity;
import flutecode.RentalCarBookingSystem.entity.CarEntity;
import flutecode.RentalCarBookingSystem.entity.UserEntity;
import flutecode.RentalCarBookingSystem.repository.BookingRepository;
import flutecode.RentalCarBookingSystem.repository.CarRepository;
import flutecode.RentalCarBookingSystem.repository.UserRepository;
import flutecode.RentalCarBookingSystem.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public BookingController(BookingService bookingService,
                             CarRepository carRepository,
                             UserRepository userRepository,
                             BookingRepository bookingRepository) {
        this.bookingService = bookingService;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    @ModelAttribute("booking")
    public BookingEntity bookingModel() {
        return new BookingEntity();
    }

    // ✅ Show booking form for a specific car
    @GetMapping("/bookcar/{carId}")
    public String showBookingForm(@PathVariable("carId") Long carId, Model model) {
        Optional<CarEntity> carOpt = carRepository.findById(carId);
        if (carOpt.isEmpty()) {
            model.addAttribute("error", "Car not found");
            return "error";
        }

        BookingEntity booking = new BookingEntity();
        booking.setCar(carOpt.get());
        model.addAttribute("booking", booking);
        model.addAttribute("isUpdate", false);
        return "bookingForm";
    }

    // ✅ Save a new booking
    @PostMapping("/bookingSuccess")
    public String confirmBooking(@ModelAttribute BookingEntity booking,
                                 Authentication authentication,
                                 HttpSession session,
                                 Model model) {
        try {
            String email = null;
            if (authentication != null && authentication.isAuthenticated()) {
                email = authentication.getName();
                session.setAttribute("userEmail", email);
            } else {
                email = (String) session.getAttribute("userEmail");
            }

            if (email == null) {
                model.addAttribute("error", "Please log in to book a car.");
                return "error";
            }

            Optional<UserEntity> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                model.addAttribute("error", "User not found.");
                return "error";
            }

            UserEntity user = userOpt.get();
            booking.setUser(user);

            if (booking.getFromDate() != null && booking.getToDate() != null) {
                long days = ChronoUnit.DAYS.between(booking.getFromDate(), booking.getToDate());
                if (days <= 0) days = 1;

                CarEntity car = booking.getCar();
                if (car != null && car.getPrice() != null) {
                    booking.setTotalDays((int) days);
                    booking.setTotalPrice(car.getPrice().doubleValue() * days);
                }
            }

            bookingRepository.save(booking);
            model.addAttribute("message", "Booking confirmed successfully!");
            return "bookingSuccess";
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            return "error";
        }
    }

    // ✅ View all bookings for the logged-in user
    @GetMapping("/myBookings")
    public String viewMyBookings(Authentication authentication, HttpSession session, Model model) {
        String email = null;
        if (authentication != null && authentication.isAuthenticated()) {
            email = authentication.getName();
            session.setAttribute("userEmail", email);
        } else {
            email = (String) session.getAttribute("userEmail");
        }

        if (email == null) return "redirect:/login";

        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "User not found.");
            return "error";
        }

        UserEntity user = userOpt.get();
        List<BookingEntity> bookings = bookingRepository.findByUser(user);
        model.addAttribute("bookings", bookings);
        return "myBookings";
    }

    // ✅ Cancel booking
    @PostMapping("/cancelBooking/{id}")
    public String cancelBooking(@PathVariable("id") Integer bookingId, Model model) {
        bookingRepository.deleteById(bookingId);
        model.addAttribute("message", "Booking cancelled successfully!");
        return "redirect:/myBookings";
    }

    // ✅ Modify booking - show form with existing data
    @GetMapping("/modifyBooking/{id}")
    public String modifyBooking(@PathVariable("id") Integer bookingId, Model model) {
        Optional<BookingEntity> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            model.addAttribute("error", "Booking not found.");
            return "error";
        }

        model.addAttribute("booking", bookingOpt.get());
        model.addAttribute("isUpdate", true);
        return "bookingForm";
    }

    // ✅ Update booking details
    @PostMapping("/updateBooking")
    public String updateBooking(@ModelAttribute BookingEntity booking, Model model) {
        try {
            Optional<BookingEntity> existingOpt = bookingRepository.findById(booking.getBookingId());
            if (existingOpt.isPresent()) {
                BookingEntity existing = existingOpt.get();

                existing.setFromDate(booking.getFromDate());
                existing.setToDate(booking.getToDate());

                if (existing.getFromDate() != null && existing.getToDate() != null) {
                    long days = ChronoUnit.DAYS.between(existing.getFromDate(), existing.getToDate());
                    if (days <= 0) days = 1;
                    CarEntity car = existing.getCar();
                    if (car != null && car.getPrice() != null) {
                        existing.setTotalDays((int) days);
                        existing.setTotalPrice(car.getPrice().doubleValue() * days);
                    }
                }

                bookingRepository.save(existing);
            }

            model.addAttribute("message", "Booking updated successfully!");
            return "redirect:/myBookings";
        } catch (Exception e) {
            model.addAttribute("error", "Error updating booking: " + e.getMessage());
            return "error";
        }
    }
}
