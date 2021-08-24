package com.web.hyundai.model.car.web;

import com.web.hyundai.model.car.CarColor;
import com.web.hyundai.model.car.Photo360Int;
import com.web.hyundai.model.car.modif.CarComplect;
import com.web.hyundai.model.car.modif.CarTire;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarWeb {
    private Long id;
    private String model;
    private int year;
    private int place;
    private String title;
    //private String titleGEO;
    private String slider;
    private String slider2;
    private String videoSlider;
    private String videoSliderURL;
    private int price;
    private String vehicleType;
    private int active;
    private String file;
    private String logo;
    private CarTire carTire;
    private boolean isElectro;
    private boolean isNew;
    private String slugURL;
    private List<Photo360Web> photo360s = new ArrayList<>();
    private List<CarColor> colors = new ArrayList<>();
    private Photo360Int photo360Int;
    private List<EngineWrep> engineList = new ArrayList<>();
    private List<FeatureWeb> features = new ArrayList<>();
    private List<ModedPhotoWeb> modedPhotosList = new ArrayList<>();
    private List<PhotoFeaturesWeb> photoFeatures = new ArrayList<>();
    private List<ComfortWeb> comfort = new ArrayList<>();


}
