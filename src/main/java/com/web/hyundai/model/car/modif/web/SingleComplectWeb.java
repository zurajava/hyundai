package com.web.hyundai.model.car.modif.web;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.web.hyundai.model.car.Engine;
import com.web.hyundai.model.car.Photo360;
import com.web.hyundai.model.car.modif.CarTire;
import com.web.hyundai.model.car.modif.ComplectInterier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleComplectWeb {
    private Long complectid;
    private Long engineid;
    private String name;
    private List<Photo360> photo360 = new ArrayList<>();
    private Set<CarTire> carTires = new HashSet<>();
    private ComplectInterier complectInterier;
    private String pdfFile;
}
