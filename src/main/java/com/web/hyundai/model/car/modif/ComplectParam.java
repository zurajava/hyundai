package com.web.hyundai.model.car.modif;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonView;
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
public class ComplectParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;
    @JsonView(View.Summary.class)
    private String paramName;
    private String paramNameGEO;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "complect_param")
    @JsonView(View.Summary.class)
    private Set<ComplectParamDetail> complectParamDetails = new HashSet<>();


    public ComplectParam(String paramName, String paramNameGEO, Set<ComplectParamDetail> complectParamDetails) {
        this.paramName = paramName;
        this.paramNameGEO = paramNameGEO;
        this.complectParamDetails = complectParamDetails;
    }

    public ComplectParam(String paramName, String paramNameGEO) {
        this.paramName = paramName;
        this.paramNameGEO = paramNameGEO;
    }
}
