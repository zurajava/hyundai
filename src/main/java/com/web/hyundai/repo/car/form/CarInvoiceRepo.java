package com.web.hyundai.repo.car.form;

import com.web.hyundai.model.car.forms.CarInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarInvoiceRepo extends JpaRepository<CarInvoice,Long> {
}
