package com.web.hyundai.model.about;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class AboutFeatureWeb {
    private Long id;
    private String title;
    private String titleGEO;
    private String desc;
    private String descGEO;
    private String type;
    private String image;


    AboutFeatureWeb(AboutFeature aboutFeature) {
        this.id = aboutFeature.getId();
        this.title = aboutFeature.getTitle();
        this.titleGEO = aboutFeature.getTitleGEO();
        this.desc = aboutFeature.getDesc();
        this.descGEO = aboutFeature.getDescGEO();
        this.type = aboutFeature.getType();
        this.image = aboutFeature.getImage();
    }
}
