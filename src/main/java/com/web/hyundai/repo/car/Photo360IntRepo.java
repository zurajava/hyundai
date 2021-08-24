package com.web.hyundai.repo.car;

import com.web.hyundai.model.car.Photo360Int;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface Photo360IntRepo extends JpaRepository<Photo360Int,Long> {
    @Query(value = "select * from photo360int where car_id=:id LIMIT 1",nativeQuery = true)
    Optional<Photo360Int> findByCarId(@Param("id")Long id);
}
