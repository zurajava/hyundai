package com.web.hyundai.model.car.modif;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.hyundai.model.car.Car;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
public class CarTire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String image;
    @ManyToOne
    @JoinColumn(name = "car_id")
    @JsonIgnore
    private Car car;

    public CarTire(String title, String image, Car car) {
        this.title = title;
        this.image = image;
        this.car = car;
    }
}
