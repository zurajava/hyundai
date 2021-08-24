package com.web.hyundai.model.car.forms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Entity
@Data
@NoArgsConstructor
public class CarInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String number;
    @Email
    private String email;

    @JsonIgnore
    private Long tireid;
    @JsonIgnore
    private Long colorid;
    @JsonIgnore
    private Long complectid;
    @JsonIgnore
    private Long carid;


    public CarInvoice(@NotEmpty String name, @NotEmpty String number, @Email String email) {
        this.name = name;
        this.number = number;
        this.email = email;
    }
}
