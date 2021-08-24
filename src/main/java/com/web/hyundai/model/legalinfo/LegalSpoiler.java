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
public class LegalSpoiler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String file;
    private Long fileSize;
    private String logo;
    private String title;
    private String titleGEO;


    public LegalSpoiler(String file, Long fileSize, String logo, String title, String titleGEO) {
        this.file = file;
        this.fileSize = fileSize;
        this.logo = logo;
        this.title = title;
        this.titleGEO = titleGEO;
    }
}
