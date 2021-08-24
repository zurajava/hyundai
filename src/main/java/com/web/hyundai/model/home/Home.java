package com.web.hyundai.model.home;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.hyundai.model.car.Car;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
@JsonFilter("homeFilter")
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String titleGEO;
    //private String text;
    //private String textGEO;
    private String image;
    private Integer sort;
    private String link;

    public Home(String title, String titleGEO, String image, Integer sort, String link) {
        this.title = title;
        this.titleGEO = titleGEO;
        this.image = image;
        this.sort = sort;
        this.link = link;
    }
}
