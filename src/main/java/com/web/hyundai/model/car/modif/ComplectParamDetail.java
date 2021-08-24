package com.web.hyundai.model.car.modif;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class ComplectParamDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;
    @JsonView(View.Summary.class)
    private String field;
    private String fieldGEO;
    @JsonView(View.Summary.class)
    private String value;
    private String valueGEO;



    public ComplectParamDetail(String field, String fieldGEO, String value, String valueGEO) {
        this.field = field;
        this.fieldGEO = fieldGEO;
        this.value = value;
        this.valueGEO = valueGEO;
    }
}
