package com.web.hyundai.controller.carservices;


import com.web.hyundai.model.carservices.CarService;
import com.web.hyundai.model.carservices.CarServiceForm;
import com.web.hyundai.model.carservices.ServiceAgentForm;
import com.web.hyundai.repo.carservice.CarServiceFormRepo;
import com.web.hyundai.repo.carservice.CarServiceRepo;
import com.web.hyundai.repo.carservice.ServiceAgentFormRepo;
import com.web.hyundai.service.email.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@Api(tags = "CarService Form")
@CrossOrigin("*")
public class CarServiceFormController {

    @Value("${mailListService}")
    private String[] TO;
    private final String SUBJECT = "Service Reg";
    private final String TEXT = "მომხმარებელი %s ჩაეწერა სერვისზე: %s , მეილი: %s , მობილური: %s ტექსტი: %s";
    private final String TEXT_AGENT = "მომხმარებელმა %s გამოიძახა აგენტი, მეილი: %s , მობილური: %s დრო: %s, ლოკაცია: %s";



    @Autowired
    private CarServiceRepo carServiceRepo;
    @Autowired
    private CarServiceFormRepo carServiceFormRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ServiceAgentFormRepo serviceAgentFormRepo;


    @PostMapping(path = "/api/services/register/{serviceid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> serviceRegistration(
            @PathVariable Long serviceid,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String textMessage
    ) throws MessagingException {

        Optional<CarService> service = carServiceRepo.findById(serviceid);

        if (service.isPresent()) {
            CarServiceForm form = new CarServiceForm(service.get().getTitleGEO(), name, email, phone, textMessage);
            CarServiceForm formSaved = carServiceFormRepo.save(form);
            emailService.sendSimpleMessage(TO, SUBJECT, String.format(TEXT, name, service.get().getTitle(), email, phone, textMessage));
            return ResponseEntity.ok(formSaved);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი სერვისი არ მოიძებნა");
    }


    @PostMapping(path = "/api/services/agent", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> serviceAgent
            (@RequestParam
                @DateTimeFormat(pattern="yyyy.MM.dd HH:mm:ss")
                     @ApiParam(value = "Time Format: yyyy.MM.dd HH:mm:ss",required = true) LocalDateTime localdatetime,
             @RequestParam @NotEmpty String fullName,
             @RequestParam @NotEmpty String phone,
             @RequestParam @NotEmpty String location,
             @Email
             @RequestParam String email)
            throws MessagingException {
        if (localdatetime.isBefore(java.time.LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("არასწორი თარიღი");
        }
        ServiceAgentForm formSaved = serviceAgentFormRepo.save(new ServiceAgentForm(fullName,phone,email,localdatetime,location));
        emailService.sendSimpleMessage(TO, SUBJECT,
                String.format(
                        TEXT_AGENT,
                        formSaved.getFullName(),
                        formSaved.getEmail(),
                        formSaved.getPhone(),
                        localdatetime,
                        formSaved.getLocation())
        );
        return ResponseEntity.ok(formSaved);

    }




}
