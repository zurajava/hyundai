package com.web.hyundai.model.car.modif;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@JsonFilter("InterierFilter")
public class ComplectInterierFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String feature;
    private String featureGEO;

    public ComplectInterierFeature(String feature, String featureGEO) {
        this.feature = feature;
        this.featureGEO = featureGEO;
    }
}
