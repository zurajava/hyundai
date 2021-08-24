package com.web.hyundai.repo.car;

import com.web.hyundai.model.car.ModedPhotoDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModedPhotoDetailsRepo extends JpaRepository<ModedPhotoDetails,Long> {

    @Query(value = "select * from moded_photo_details  where photo_id=:id",nativeQuery = true)
    List<ModedPhotoDetails> findAllByPhotoId(@Param("id")Long id);



}
