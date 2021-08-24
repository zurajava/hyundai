package com.web.hyundai.model.car.web;

import com.web.hyundai.model.car.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComfortWeb {
    private Long id;
    private String title;
    //private String titleGEO;
    private String desc;
    //private String descGEO;
    private String image;
}
