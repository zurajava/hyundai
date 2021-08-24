package com.web.hyundai.repo.car;

import com.web.hyundai.model.car.CarColor;
import com.web.hyundai.model.car.Photo360;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarColorRepo extends JpaRepository<CarColor,Long> {

}
