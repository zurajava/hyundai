package com.web.hyundai.model.car.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModedPhotoWeb {
    private Long id;
    private String image;
    private List<ModedPhotoDetailsWeb> modedPhotoDetailsWebs = new ArrayList<>();

}
