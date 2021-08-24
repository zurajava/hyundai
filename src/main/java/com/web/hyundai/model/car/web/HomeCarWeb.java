package com.web.hyundai.model.car.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@Data
@AllArgsConstructor
public class HomeCarWeb {
    private Long id;
    private String model;
    private int year;
    private int place;
    private String title;
    private String slider;
    private String logo;
    private int price;
    private String vehicleType;
    private int active;
    private String slugURL;
    private boolean isElectro;
    private boolean isNew;


}

