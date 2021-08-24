package com.web.hyundai.repo.car.modif;

import com.web.hyundai.model.car.modif.ComplectInterier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ComplectInterierRepo extends JpaRepository<ComplectInterier,Long> {

    @Query(value = "select * from complect_interier where id=:id and car_complect_id is null",nativeQuery = true)
    Optional<ComplectInterier> findWhereComplectInterierIsEmpty(@Param("id")Long id);

    @Query(value = "select * from complect_interier where id=:compint and car_complect_id = :compid",nativeQuery = true)
    Optional<ComplectInterier> findByComplectAndInterierId(@Param("compint")Long compint, @Param("compid")Long compid);

    @Query(value = "select * from complect_interier where car_complect_id = :compid",nativeQuery = true)
    Optional<ComplectInterier> findByComplectId(@Param("compid")Long compint);




}
