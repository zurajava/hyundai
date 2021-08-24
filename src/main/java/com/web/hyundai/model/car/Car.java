package com.web.hyundai.model.car;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String model;
    private int year;
    private int place;
    private String title;
    //@JsonIgnore
    private String titleGEO;
    private String slider;
    private String slider2;
    private String videoSlider;
    private String videoSliderURL;
    private String logo;
    private int price;
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    private int active;
    private String file;
    private boolean isElectro;
    private boolean isNew;
    private String slugURL;


}
