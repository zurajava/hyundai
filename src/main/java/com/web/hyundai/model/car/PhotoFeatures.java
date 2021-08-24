package com.web.hyundai.model.car;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PhotoFeatures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String titleGEO;
    @Lob
    @Column(length=5000)
    private String desc;
    @Lob
    @Column(length=5000)
    private String descGEO;
    private String image;
    private String type;
    @ManyToOne
    @JoinColumn(name = "car_id")
    @JsonIgnore
    private Car car;


}
