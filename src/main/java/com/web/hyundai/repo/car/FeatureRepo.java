package com.web.hyundai.repo.car;

import com.web.hyundai.model.car.Engine;
import com.web.hyundai.model.car.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeatureRepo extends JpaRepository<Feature,Long> {
    @Query(value = "select * from feature where car_id=:id",nativeQuery = true)
    List<Feature> findAllByCarId(@Param("id") Long id);
}
