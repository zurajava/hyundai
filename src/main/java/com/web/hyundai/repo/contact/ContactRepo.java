package com.web.hyundai.repo.contact;

import com.web.hyundai.model.contact.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepo extends JpaRepository<Contact,Long> {
}
