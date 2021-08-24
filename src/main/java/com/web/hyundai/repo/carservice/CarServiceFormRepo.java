package com.web.hyundai.repo.carservice;

import com.web.hyundai.model.carservices.CarServiceForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarServiceFormRepo extends JpaRepository<CarServiceForm,Long> {
}
