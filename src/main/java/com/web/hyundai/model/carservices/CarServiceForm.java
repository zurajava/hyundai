package com.web.hyundai.model.carservices;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
@Entity
public class CarServiceForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String serviceName;
    private String name;
    @Email(message = "field must be email type")
    private String email;
    private String phoneNumber;
    @Lob
 @Column(length=5000)
    private String textMessage;


    public CarServiceForm(String serviceName, String name, @Email(message = "field must be email type") String email, String phoneNumber, String textMessage) {
        this.serviceName = serviceName;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.textMessage = textMessage;
    }
}
