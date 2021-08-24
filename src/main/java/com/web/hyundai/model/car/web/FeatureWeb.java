package com.web.hyundai.model.car.web;


import com.web.hyundai.model.car.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureWeb {

    private Long id;
    private String name;
    //private String nameGEO;


}
