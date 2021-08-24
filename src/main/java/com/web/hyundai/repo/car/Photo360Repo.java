package com.web.hyundai.repo.car;

import com.web.hyundai.model.car.Feature;
import com.web.hyundai.model.car.Photo360;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface Photo360Repo extends JpaRepository<Photo360,Long> {
    @Query(value = "select * from photo360 where car_id=:id",nativeQuery = true)
    List<Photo360> findAllByCarId(@Param("id") Long id);

    @Query(value = "select * from photo360 where car_id=:id LIMIT 1",nativeQuery = true)
    Optional<Photo360> findAllByCarIdOne(@Param("id") Long id);

    @Query(value = "select * from photo360 where complect_id=:id",nativeQuery = true)
    List<Photo360> findAllByComplectId(@Param("id") Long id);

    @Query(value = "select * from photo360 where color_id=:id",nativeQuery = true)
    List<Photo360> findAllByColorId(@Param("id") Long id);


    @Query(value = "select * from photo360 where color_id=:colorid and car_id=:carid",nativeQuery = true)
    List<Photo360> findByColorIdAndCarId(@Param("colorid") Long colorid, @Param("carid")Long carid);


    @Query(value = "select * from photo360 where complect_id is null and id in :photolist",nativeQuery = true)
    List<Photo360> findAllByIdList(@Param("photolist") List<Long> photolist);


}
