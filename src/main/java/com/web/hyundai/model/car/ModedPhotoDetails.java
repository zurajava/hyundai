package com.web.hyundai.model.car;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ModedPhotoDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int width;
    private int height;
    private String image;
    private String title;
    private String titleGEO;
    private String part;
    @Enumerated(EnumType.STRING)
    private DesignType DesignType;
    @ManyToOne
    @JoinColumn(name = "photo_id")
    private ModedPhoto modedPhoto;
}
