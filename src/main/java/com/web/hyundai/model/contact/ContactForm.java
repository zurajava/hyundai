package com.web.hyundai.model.contact;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactForm {
    @Size(max = 5000,message = "max char size 5000")
    @NotEmpty
    private String firstName;
    @Size(max = 5000,message = "max char size 5000")
    @NotEmpty
    private String lastName;
    @Email
    @NotEmpty
    private String email;
    @Size(max = 5000,message = "max char size 5000")
    @NotEmpty
    private String message;
}
