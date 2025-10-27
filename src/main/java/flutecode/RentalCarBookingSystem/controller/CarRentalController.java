package flutecode.RentalCarBookingSystem.controller;

import flutecode.RentalCarBookingSystem.dto.BookingRequestDto;
import flutecode.RentalCarBookingSystem.dto.UserRequestDto;
import flutecode.RentalCarBookingSystem.entity.BookingEntity;
import flutecode.RentalCarBookingSystem.entity.CarEntity;
import flutecode.RentalCarBookingSystem.entity.UserEntity;
import flutecode.RentalCarBookingSystem.repository.CarRepository;
import flutecode.RentalCarBookingSystem.repository.UserRepository;
import flutecode.RentalCarBookingSystem.service.BookingService;
import flutecode.RentalCarBookingSystem.service.CarService;
import flutecode.RentalCarBookingSystem.Mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@Slf4j
public class CarRentalController {

    private final CarService carService;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final BookingService bookingService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CarRentalController(CarService carService,
                               CarRepository carRepository,
                               UserRepository userRepository,
                               BookingService bookingService,
                               UserMapper userMapper,
                               PasswordEncoder passwordEncoder) {
        this.carService = carService;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.bookingService = bookingService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // -------------------- AUTH & USER --------------------
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRequestDto", new UserRequestDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userRequestDto") UserRequestDto userRequestDto,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        log.info("Saving user details: {}", userRequestDto);
        UserEntity userEntity = userMapper.toUserEntity(userRequestDto);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);

        model.addAttribute("user", userEntity);
        return "login";
    }

    // -------------------- PAGES --------------------
    @GetMapping("/index")
    public String showIndexPage() {
        return "index";
    }

    @GetMapping("/SelectionCar")
    public String showHomePage(Model model) {
        model.addAttribute("bookingRequestDto", new BookingRequestDto());
        return "SelectionCar";
    }

    @GetMapping("/contact")
    public String contactUsPage() {
        return "contact_us";
    }

    // -------------------- CARS PAGE WITH PAGINATION --------------------
    @GetMapping("/cars")
    public String viewCars(Model model,
                           @RequestParam(defaultValue = "1") int page) {

        int pageSize = 6; // 6 cars per page
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<CarEntity> carPage = carRepository.findAll(pageable);

        model.addAttribute("cars", carPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", carPage.getTotalPages());

        return "cars";
    }

    @PostMapping("/SelectionCar")
    public String showAvailableCars(@ModelAttribute BookingRequestDto bookingRequestDto, Model model) {
        String selectedModelType = bookingRequestDto.getCarModelType(); // Make sure DTO has this field
        LocalDate pickupDate = bookingRequestDto.getPickupDate();

        // Fetch cars by model type and availability
        List<CarEntity> availableCars = carRepository.findByModelTypeAndAvailabilityStatus(selectedModelType, "Available");

        model.addAttribute("cars", availableCars);
        model.addAttribute("selectedModelType", selectedModelType);
        model.addAttribute("pickupDate", pickupDate);
        model.addAttribute("pickupTime", bookingRequestDto.getPickupTime());
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);

        return "availablecars";
    }






    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Controller is working!";
    }
}
