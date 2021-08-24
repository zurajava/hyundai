package com.web.hyundai.model.about;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class About {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mainSlider;
    private String mainSliderTitle;
    @JsonIgnore
    private String mainSliderTitleGEO;
    @OneToMany
    @Fetch(FetchMode.JOIN)
    private List<AboutFeature> features = new ArrayList<>();
    private String midSlider;
    private String midSliderTitle;
    @JsonIgnore
    private String midSliderTitleGEO;
    @Lob
 @Column(length=5000)
    private String history1;
    @Lob
 @Column(length=5000)
    @JsonIgnore
    private String history1GEO;
    @Lob
 @Column(length=5000)
    private String history2;
    @Lob
    @Column(length=5000)
    @JsonIgnore
    private String history2GEO;
    private String botSlider;
    private String vision;
    @JsonIgnore
    private String visionGEO;


    public About(String mainSlider, String mainSliderTitle, String mainSliderTitleGEO, List<AboutFeature> features, String midSlider, String midSliderTitle, String midSliderTitleGEO, String history1, String history1GEO, String history2, String history2GEO, String botSlider, String vision, String visionGEO) {
        this.mainSlider = mainSlider;
        this.mainSliderTitle = mainSliderTitle;
        this.mainSliderTitleGEO = mainSliderTitleGEO;
        this.features = features;
        this.midSlider = midSlider;
        this.midSliderTitle = midSliderTitle;
        this.midSliderTitleGEO = midSliderTitleGEO;
        this.history1 = history1;
        this.history1GEO = history1GEO;
        this.history2 = history2;
        this.history2GEO = history2GEO;
        this.botSlider = botSlider;
        this.vision = vision;
        this.visionGEO = visionGEO;
    }
}


