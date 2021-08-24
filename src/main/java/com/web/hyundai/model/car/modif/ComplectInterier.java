package com.web.hyundai.model.car.modif;

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
@JsonFilter("InterierFilter")
public class ComplectInterier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "complect_interier")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<ComplectInterierFeature> complectInterierFeatures = new ArrayList<>();
    @OneToOne
    @JsonIgnore
    private CarComplect CarComplect;


    public ComplectInterier(String image) {
        this.image = image;
    }
}
