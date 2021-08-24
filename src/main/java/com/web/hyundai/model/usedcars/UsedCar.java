package com.web.hyundai.model.usedcars;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;


@Data
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UsedCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private String model;
    private Integer price;
    private int year;
    private Integer mileage;
    private Integer door;
    private String type;
    private String fuel;
    private String transmission;
    private String extColor;
    private String intColor;
    private String engine;
    private double hp;
    private String displayPhoto;
    @LastModifiedDate
    private Date date;

    // TODO orderis dacva
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "usedcar_id")
    private Set<UsedCarPhoto> usedCarPhotoList = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="model_id")
    //@JsonIgnore
    private UsedCarModel usedCarModel;


    // create shiu used cartan ertad movides valueslist da update deletshi id ebis listi
    //@OneToMany(fetch = FetchType.EAGER)
    //@JoinColumn(name = "usedcar_id")
    //private Set<UsedCarFeature> usedCarFeatures = new HashSet<>();


    public UsedCar(Integer price, int year, Integer mileage, Integer door, String type, String fuel,
                   String transmission, String extColor, String intColor, String engine, double hp,
                   String displayPhoto, Set<UsedCarPhoto> usedCarPhotoList, UsedCarModel usedCarModel) {
        this.price = price;
        this.year = year;
        this.mileage = mileage;
        this.door = door;
        this.type = type;
        this.fuel = fuel;
        this.transmission = transmission;
        this.extColor = extColor;
        this.intColor = intColor;
        this.engine = engine;
        this.hp = hp;
        this.displayPhoto = displayPhoto;
        this.usedCarPhotoList = usedCarPhotoList;
        this.usedCarModel = usedCarModel;
    }
}
