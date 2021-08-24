package com.web.hyundai.repo.car;

import com.web.hyundai.model.car.Comfort;
import com.web.hyundai.model.car.PhotoFeatures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComfortRepo extends JpaRepository<Comfort,Long> {
    @Query(value = "select * from comfort where car_id=:id",nativeQuery = true)
    List<Comfort> findAllByCarId(@Param("id") Long id);
}
