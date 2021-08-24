package com.web.hyundai.model.car.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModedPhotoDetailsWeb {
    private Long id;
    private int width;
    private int height;
    private String image;
    private String title;
    //private String titleGEO;
    private String part;
    private String type;
}
