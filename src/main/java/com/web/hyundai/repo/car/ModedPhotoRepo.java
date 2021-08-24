package com.web.hyundai.repo.car;

import com.web.hyundai.model.car.ModedPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModedPhotoRepo extends JpaRepository<ModedPhoto,Long> {

    @Query(value = "select * from moded_photo where car_id=:id",nativeQuery = true)
    List<ModedPhoto> findAllByCarId(@Param("id") Long id);
}
