package com.web.hyundai.repo.carservice;

import com.web.hyundai.model.carservices.CarServiceDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarServiceDetailRepo extends JpaRepository<CarServiceDetails,Long> {
}
