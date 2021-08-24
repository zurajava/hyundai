package com.web.hyundai.repo.car;

import com.web.hyundai.model.car.Engine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EngineRepo extends JpaRepository<Engine,Long> {

    @Query(value = "select * from engine where car_id=:id",nativeQuery = true)
    List<Engine> findAllByCarId(@Param("id") Long id);

    @Query(value = "select * from engine where car_id=:id order by id desc limit 3",nativeQuery = true)
    List<Engine> findAllByCarIdLimit3(@Param("id") Long id);

    @Query(value = "SELECT e.id as engine_id,e.title,c.name,c.id as complect_id,e.price FROM engine e inner join car_complect c on e.id=c.engine_id where car_id=:carid",
            nativeQuery = true)
    List<Object[]> findAllEnginesWithComplect(@Param("carid") Long id);
}
