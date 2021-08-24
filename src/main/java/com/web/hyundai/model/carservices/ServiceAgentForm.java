package com.web.hyundai.model.carservices;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ServiceAgentForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String fullName;
    @NotEmpty
    private String phone;
    @Email
    private String email;
    @DateTimeFormat(pattern="yyyy.MM.dd HH:mm:ss")
    private LocalDateTime date;
    @NotEmpty
    private String location;

    public ServiceAgentForm(@NotEmpty String fullName, @NotEmpty String phone, @Email String email, LocalDateTime date, @NotEmpty String location) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.date = date;
        this.location = location;
    }
}
