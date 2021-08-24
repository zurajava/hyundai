package com.web.hyundai.model.carservices;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@JsonFilter("serviceFilter")
public class CarServiceDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    //@JsonIgnore
    private String nameGEO;

    public CarServiceDetails(String name, String nameGEO) {
        this.name = name;
        this.nameGEO = nameGEO;
    }
}
