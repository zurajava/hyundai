package com.web.hyundai.model.car.modif.web;


import com.web.hyundai.model.car.Photo360;
import com.web.hyundai.model.car.modif.CarTire;
import com.web.hyundai.model.car.modif.ComplectParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterComplectWeb {
    private String complectName;
    private Set<ComplectParam> complectParams = new HashSet<>();
    private Photo360 photo360;
    private CarTire carTire;
    private String engineTitle;
    private String price;
    private String model;
    private ArrayList<Long> featureList = new ArrayList<>();


}
