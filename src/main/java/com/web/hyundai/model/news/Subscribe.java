package com.web.hyundai.model.news;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)

public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Email(message = "field must be email type")
    private String email;
    @JsonIgnore
    @LastModifiedDate
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date date;

    public Subscribe(String firstName, String lastName, @Email String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
