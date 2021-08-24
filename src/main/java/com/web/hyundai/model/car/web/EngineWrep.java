package com.web.hyundai.model.car.web;

import com.web.hyundai.model.car.EngineDesc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EngineWrep {
    private Long engineId;
    private String engineTitle;
    private String complectName;
    private Long complectId;
    private int hp;
    private int price;
    private List<EngineDescWeb> engineDesc = new ArrayList<>();
    private String city;
    private String outCity;
    private String hundred;
    private String combined;
    private String carLogo;


}
