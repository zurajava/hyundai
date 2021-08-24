package com.web.hyundai.repo.car;

import com.web.hyundai.model.car.PhotoFeatures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoFeaturesRepo extends JpaRepository<PhotoFeatures,Long> {
    @Query(value = "select * from photo_features where car_id=:id",nativeQuery = true)
    List<PhotoFeatures> findAllByCarId(@Param("id") Long id);
}
