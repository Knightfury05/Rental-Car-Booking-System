package flutecode.RentalCarBookingSystem.service;

import flutecode.RentalCarBookingSystem.entity.CarEntity;
import flutecode.RentalCarBookingSystem.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // Get all cars
    public List<CarEntity> getAllCars() {
        return carRepository.findAll();
    }

    // Get a single car by its ID
    public CarEntity getCarById(Long id) {
        Optional<CarEntity> car = carRepository.findById(id);
        return car.orElse(null);
    }
}
