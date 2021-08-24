package com.web.hyundai.model.about;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
public class AboutWeb {
    private Long id;
    private String mainSlider;
    private String mainSliderTitle;
    private String mainSliderTitleGEO;
    private List<AboutFeatureWeb> features = new ArrayList<>();
    private String midSlider;
    private String midSliderTitle;
    private String midSliderTitleGEO;
    private String history1;
    private String history1GEO;
    private String history2;
    private String history2GEO;
    private String botSlider;
    private String vision;
    private String visionGEO;


    public AboutWeb(About info) {
        List<AboutFeatureWeb> featureWeb = new ArrayList<>();
        info.getFeatures().forEach(aboutFeature -> featureWeb.add(new AboutFeatureWeb(aboutFeature)));


        this.id = info.getId();
        this.mainSlider = info.getMainSlider();
        this.mainSliderTitle = info.getMainSliderTitle();
        this.mainSliderTitleGEO = info.getMainSliderTitleGEO();
        this.midSlider = info.getMidSlider();
        this.midSliderTitle = info.getMidSliderTitle();
        this.midSliderTitleGEO = info.getMidSliderTitleGEO();
        this.history1 = info.getHistory1();
        this.history1GEO = info.getHistory1GEO();
        this.history2 = info.getHistory2();
        this.history2GEO = info.getHistory2GEO();
        this.botSlider = info.getBotSlider();
        this.vision = info.getVision();
        this.visionGEO = info.getVisionGEO();
        this.features = featureWeb;
    }
}
