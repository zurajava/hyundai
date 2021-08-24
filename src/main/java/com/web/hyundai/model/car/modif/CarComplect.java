package com.web.hyundai.model.car.modif;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.car.Engine;
import com.web.hyundai.model.car.Photo360;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class CarComplect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;
    @JsonView(View.Summary.class)
    private String name;
    private String nameGEO;
    private String pdfFile;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "complect_id")
    @JsonView(View.Summary.class)
    private Set<ComplectParam> complectParams = new HashSet<>();
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "complect_id")
    //@Fetch(FetchMode.JOIN)
    @JsonIgnore
    private List<Photo360> photo360 = new ArrayList<>();
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "complect_id")
    @JsonIgnore
    private Set<CarTire> carTires = new HashSet<>();
    @OneToOne
    @JoinColumn(name = "engine_id")
    @JsonIgnore
    //@MapsId("id") // cascade type.ALL aris avtomaturad
    private Engine engine;



    public CarComplect(String name, String nameGEO, String pdfFile, Set<ComplectParam> complectParams, List<Photo360> photo360, Set<CarTire> carTires, Engine engine) {
        this.name = name;
        this.nameGEO = nameGEO;
        this.pdfFile = pdfFile;
        this.complectParams = complectParams;
        this.photo360 = photo360;
        this.carTires = carTires;
        this.engine = engine;
    }
}
