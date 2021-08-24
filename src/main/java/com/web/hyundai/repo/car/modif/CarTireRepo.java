package com.web.hyundai.repo.car.modif;

import com.web.hyundai.model.car.Photo360;
import com.web.hyundai.model.car.modif.CarTire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CarTireRepo extends JpaRepository<CarTire,Long> {
    @Query(value = "select * from car_tire where car_id=:id",nativeQuery = true)
    List<CarTire> findAllTiresByCarId(@Param("id") Long id);

    @Query(value = "select * from car_tire where car_id=:id limit 1",nativeQuery = true)
    Optional<CarTire> findOneTireByCarId(@Param("id") Long id);


    @Query(value = "select * from car_tire where id in :cartirelist and complect_id is null",nativeQuery = true)
    Set<CarTire> findAllByIdList(@Param("cartirelist") List<Long> cartirelist);

    @Query(value = "delete from car_tire where complect_id = :complectId",nativeQuery = true)
    Set<CarTire> findAllByIdList(@Param("complectId") Long complectId);
}
