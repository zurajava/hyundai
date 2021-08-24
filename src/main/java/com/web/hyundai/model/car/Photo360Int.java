package com.web.hyundai.model.car;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Photo360Int {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo360int_id")
    @Fetch(FetchMode.JOIN)
    private List<Photo360IntList> photo360IntList = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "car_id")
    @JsonIgnore
    private Car car;

}

