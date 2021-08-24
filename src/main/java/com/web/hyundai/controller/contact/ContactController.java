package com.web.hyundai.controller.contact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.web.hyundai.model.contact.Contact;
import com.web.hyundai.model.contact.ContactForm;
import com.web.hyundai.repo.contact.ContactRepo;
import com.web.hyundai.service.car.CarBuildService;
import com.web.hyundai.service.email.EmailService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Api("Contact")
public class ContactController {

    @Autowired
    ContactRepo contactRepo;
    @Autowired
    private CarBuildService carBuildService;
    @Autowired
    private EmailService emailService;


    private final Set<String> EXCLUDED_FIELDS = Stream.of("contactTitleGEO", "formTitleGEO").collect(Collectors.toSet());
    @Value("${mailListContact}")
    private String[] TO;

    @PostMapping("/api/contact/form")
    public ResponseEntity<Object> contactForm(@RequestBody @Valid  ContactForm contactForm){
        String text = String.format("სახელი: %s გვარი: %s მეილი: %s ტექსტი: %s",contactForm.getFirstName(),contactForm.getLastName(),contactForm.getEmail(),contactForm.getMessage());
        try {
            emailService.sendSimpleMessage(TO,"Contact",text);
            return ResponseEntity.ok().build();
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PostMapping(path = "/admin/contact/create", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> createContact(@RequestBody @Valid Contact contact, @RequestHeader("accept-language") @NotNull String lang) throws JsonProcessingException {
        if (contactRepo.findAll().size() > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("კონტაქტი უკვე არსებობს");
        }

        Contact savedContact = contactRepo.save(contact);
        return ResponseEntity.ok(carBuildService.jsonFilter("ContactFilter", savedContact));
    }

    @PostMapping(path = "/admin/contact/update/{contactId}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> updateContact(@PathVariable Long contactId,
                                           @RequestBody Contact contact,
                                           @RequestHeader("accept-language") String lang) throws JsonProcessingException {
        Optional<Contact> myContact = contactRepo.findById(contactId);

        if (myContact.isPresent()) {
            myContact.get().setFormTitle(contact.getFormTitle());
            myContact.get().setFormTitleGEO(contact.getFormTitleGEO());
            myContact.get().setContactTitle(contact.getContactTitle());
            myContact.get().setContactTitleGEO(contact.getContactTitleGEO());
            myContact.get().setAddress(contact.getAddress());
            myContact.get().setEmail(contact.getEmail());
            myContact.get().setPhone(contact.getPhone());
            contactRepo.save(myContact.get());

            return ResponseEntity.ok(carBuildService.jsonFilter("ContactFilter", myContact.get()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("კონტაქტი არ არსებობს");

    }

    @GetMapping(path = "/api/contact/find", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> findContact(@RequestHeader("accept-language") String lang) throws JsonProcessingException {
        List<Contact> contacts = contactRepo.findAll();
        if (contacts.size()>0) {
            Contact contact = contacts.get(0);
            if (lang.equals("ka")) {
                contact.setContactTitle(contact.getContactTitleGEO());
                contact.setFormTitle(contact.getFormTitleGEO());
            }
            return ResponseEntity.ok(carBuildService.jsonExcludeFilter("ContactFilter", EXCLUDED_FIELDS, contact));

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("კონტაქტი არ არსებობს");

    }

    @GetMapping(path = "/admin/contact/find", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> findContactAdmin() throws JsonProcessingException {
        List<Contact> contacts = contactRepo.findAll();
        if (contacts.size()>0) {
            Contact contact = contacts.get(0);
            return ResponseEntity.ok(carBuildService.jsonFilter("ContactFilter",contact));

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("კონტაქტი არ არსებობს");

    }


}
