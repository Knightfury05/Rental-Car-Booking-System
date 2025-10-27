package flutecode.RentalCarBookingSystem.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {
        @NotBlank(message = "Car name is required")
        private String carName;


        @NotNull(message = "Pickup date is required")
        @FutureOrPresent(message = "Pickup date cannot be in the past")
        private LocalDate pickupDate;

        @NotNull(message = "Pickup time is required")
        @FutureOrPresent(message = "Pickup time cannot be in the past")
        private LocalTime pickupTime;

        private String carModelType; // e.g., Sedan, Hatchback, SUV


}
