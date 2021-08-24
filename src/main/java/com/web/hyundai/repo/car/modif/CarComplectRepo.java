package com.web.hyundai.repo.car.modif;

import com.web.hyundai.model.car.modif.CarComplect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CarComplectRepo extends JpaRepository<CarComplect,Long> {

    @Query(value = "select * from car_complect c where c.engine_id = ?1", nativeQuery = true)
    Optional<CarComplect> findByEngineId(Long engineId);



    @Query(value = "select id from car_complect where complect_interier_id = :id",nativeQuery = true)
    Long findComplectId(@Param("id")Long id);

}
