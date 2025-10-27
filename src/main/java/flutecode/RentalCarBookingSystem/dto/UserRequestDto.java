package flutecode.RentalCarBookingSystem.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Number cannot be null")
    private Long phoneNumber;

    @NotNull(message = "Password cannot be null")
    @Pattern(regexp = "^[A-Za-z\\d]{8,10}$", message = "Password must be 8 to 10 characters long and contain only letters and digits")
    private String password;
}

