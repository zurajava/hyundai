package com.web.hyundai.model.car.web;

import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.car.CarColor;
import com.web.hyundai.model.car.Photo360List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo360Web {

    private Long id;
    private List<Photo360List> photo360ListList = new ArrayList<>();
    private CarColor carColor;


}
