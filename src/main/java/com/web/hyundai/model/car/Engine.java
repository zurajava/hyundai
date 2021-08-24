package com.web.hyundai.model.car;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.hyundai.model.car.modif.CarComplect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Engine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    //private String titleGEO;
    private int hp;
    private int price;
    //private boolean active;
    //private String file;
    @Embedded
    private FuelUsage fuelUsage;
    @ManyToOne
    @JoinColumn(name = "car_id")
    @JsonIgnore
    private  Car car;



}





