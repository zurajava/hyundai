package com.web.hyundai.model.contact;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonFilter("ContactFilter")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String contactTitle;
    @NotEmpty
    private String contactTitleGEO;
    @NotEmpty
    private String formTitle;
    @NotEmpty
    private String formTitleGEO;
    @NotEmpty
    private String address;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String phone;
}
