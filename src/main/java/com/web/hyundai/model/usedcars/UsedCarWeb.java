package com.web.hyundai.model.usedcars;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsedCarWeb {

    private Long id;
    private String model;
    private Integer price;
    private int year;
    private Integer mileage;
    private String fuel;
    private String extColor;
    private String intColor;
    private String engine;
    private double hp;
    private String displayPhoto;
    private String transmission;

    public UsedCarWeb(Long id, Integer price, int year, Integer mileage, String fuel, String extColor, String intColor, String engine, double hp, String displayPhoto, String transmission) {
        this.id = id;
        this.price = price;
        this.year = year;
        this.mileage = mileage;
        this.fuel = fuel;
        this.extColor = extColor;
        this.intColor = intColor;
        this.engine = engine;
        this.hp = hp;
        this.displayPhoto = displayPhoto;
        this.transmission = transmission;
    }
}
