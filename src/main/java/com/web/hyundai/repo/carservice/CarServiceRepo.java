package com.web.hyundai.repo.carservice;

import com.web.hyundai.model.carservices.CarService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarServiceRepo extends JpaRepository<CarService,Long> {



}
