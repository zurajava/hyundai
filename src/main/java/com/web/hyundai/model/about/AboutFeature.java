package com.web.hyundai.model.about;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Data
public class AboutFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @JsonIgnore
    private String titleGEO;
    private String desc;
    @JsonIgnore
    private String descGEO;
    private String type;
    private String image;


    public AboutFeature(String title, String titleGEO, String desc, String descGEO) {
        this.title = title;
        this.titleGEO = titleGEO;
        this.desc = desc;
        this.descGEO = descGEO;
    }
}
