package com.web.hyundai.model.car.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EngineDescWeb {

    private Long id;
    private String name;
    private String engineDescLogo;



}
