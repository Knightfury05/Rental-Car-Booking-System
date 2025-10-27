package flutecode.RentalCarBookingSystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "car_details")
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId; // was int


    private String name;
    private String model;
    private BigDecimal price;

    @Column(name = "seating_capacity")
    private int seatingCapacity;

    @Column(name = "luggage_capacity")
    private int luggageCapacity;

    @Column(name = "ac_or_non_ac")
    private String acOrNonAc;

    @Column(name = "is_free_cancellation")
    private String isFreeCancellation;

    private int year;

    @Column(name = "registration_number")
    private String registrationNumber;


    @Column(name = "availability_status")
    private String availabilityStatus;

    @Column(name = "image_url", length = 2000)
    private String imageUrl; // comma-separated URLs

    @Column(name="model_type")
    private String modelType;
}
