package com.web.hyundai.model.car.modif.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class EngineWithComplectWeb {
    private Long engineId;
    private String complectName;
    private String engineTitle;
    private String price;
    private Long complectId;
}
