package com.web.hyundai.model.legalinfo;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class LegalInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String title;
    private String titleGEO;
    @Lob
 @Column(length=5000)
    private String text;
    @Lob
 @Column(length=5000)
    private String textGEO;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "Legal_ID")
    private Set<LegalSpoiler> legalSpoilers = new HashSet<>();


    public LegalInfo(String image, String title, String titleGEO, String text, String textGEO) {
        this.image = image;
        this.title = title;
        this.titleGEO = titleGEO;
        this.text = text;
        this.textGEO = textGEO;
    }
}
