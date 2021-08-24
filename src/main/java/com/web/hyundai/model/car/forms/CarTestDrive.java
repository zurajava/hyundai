package com.web.hyundai.model.car.forms;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
public class CarTestDrive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String fullName;
    @NotNull
    private Integer engineId;
    @NotEmpty
    private String number;
    @Email
    private String email;

    public CarTestDrive(@NotEmpty String fullName, @NotEmpty Integer engineId, @NotEmpty String number, @Email String email) {
        this.fullName = fullName;
        this.engineId = engineId;
        this.number = number;
        this.email = email;
    }
}
