package com.web.hyundai.model.car;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuelUsage {
    private String city;
    private String outCity;
    private String hundred;
    private String combined;
}
