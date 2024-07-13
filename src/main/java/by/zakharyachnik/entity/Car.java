package by.zakharyachnik.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "cars")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "car_id", nullable = false)
    private Integer carId;

    @Column(nullable = false, name="make")
    private String make;
    @Column(nullable = false, name="model")
    private String model;
    @Column(nullable = false, name="year")
    private int year;
    @Column(nullable = false, name="body_type")
    private String bodyType;
    @Column(nullable = false, name="engineType")
    private String engineType;
    @Column(nullable = false, name="driveType")
    private String driveType;
    @Column(nullable = false, name="transmission")
    private String transmission;
    @Column(nullable = false, name="photo_name")
    private String photoName;

    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Publication publication;

    public Car(String make, String carModel, int yearInt, String bodyType, String engineType, String driveType, String transmission) {
        this.make = make;
        this.model = carModel;
        this.year = yearInt;
        this.bodyType = bodyType;
        this.engineType = engineType;
        this.driveType = driveType;
        this.transmission = transmission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return getYear() == car.getYear() && Objects.equals(getCarId(), car.getCarId()) && Objects.equals(getMake(), car.getMake()) && Objects.equals(getModel(), car.getModel()) && Objects.equals(getBodyType(), car.getBodyType()) && Objects.equals(getEngineType(), car.getEngineType()) && Objects.equals(getDriveType(), car.getDriveType()) && Objects.equals(getTransmission(), car.getTransmission()) && Objects.equals(getPhotoName(), car.getPhotoName()) && Objects.equals(getPublication(), car.getPublication());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCarId(), getMake(), getModel(), getYear(), getBodyType(), getEngineType(), getDriveType(), getTransmission(), getPhotoName(), getPublication());
    }
}
