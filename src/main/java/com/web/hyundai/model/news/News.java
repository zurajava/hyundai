package com.web.hyundai.model.news;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.hyundai.model.car.Car;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// sanam hashtagebi moaqvs lazy initializationit iqamde hibernate sesia iketeba da erors urtyavs jacksoni
@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@JsonFilter("newsFilter")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String titleGEO;
    @LastModifiedDate
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date date;
    private String displayText;
    private String displayTextGEO;
    @Lob
 @Column(length=50000)
    private String text;
    @Lob
 @Column(length=50000)
    private String textGEO;
    private String photo;

    private String thumbnail;
    //@ManyToMany(fetch = FetchType.EAGER)
//    @JsonIgnore
//    private Set<Hashtag> hashtag = new HashSet<>();
    private boolean prom;
    private int share;
    private String slugUrl;
    private int views;
    private int sort;
    @ManyToOne
    @JoinColumn(name = "car_id")
    @JsonIgnore
    private Car car;


    public News(String title, String titleGEO, String displayText, String displayTextGEO, String text, String textGEO, String photo, String thumbnail, boolean prom, int share,int sort,Car car) {
        this.title = title;
        this.titleGEO = titleGEO;
        this.displayText = displayText;
        this.displayTextGEO = displayTextGEO;
        this.text = text;
        this.textGEO = textGEO;
        this.photo = photo;
        this.thumbnail = thumbnail;
        this.prom = prom;
        this.share = share;
        this.sort=sort;
        this.car=car;
    }


    public News(String title, String titleGEO, String displayText, String displayTextGEO, String text, String textGEO, boolean prom, int share, int sort,String slugUrl) {
        this.title = title;
        this.titleGEO = titleGEO;
        this.displayText = displayText;
        this.displayTextGEO = displayTextGEO;
        this.text = text;
        this.textGEO = textGEO;
        this.prom = prom;
        this.share = share;
        this.sort=sort;
        this.slugUrl=slugUrl;
    }






}
