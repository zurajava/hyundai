package com.web.hyundai.model.carservices;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@JsonFilter("serviceFilter")
public class CarService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    //@JsonIgnore
    private String titleGEO;
    @Lob
 @Column(length=5000)
    private String text;
    @Lob
 @Column(length=5000)
    //@JsonIgnore
    private String textGEO;
    private String logo;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "service_id")
    private List<CarServiceDetails> carServiceDetailsList = new ArrayList<>();


    public CarService(String name, String nameGEO, String text, String textGEO) {
        this.title = name;
        this.titleGEO = nameGEO;
        this.text = text;
        this.textGEO = textGEO;

    }
}

