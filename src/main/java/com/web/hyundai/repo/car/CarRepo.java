package com.web.hyundai.repo.car;

import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.home.CarSizeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarRepo extends JpaRepository<Car,Long> {
    @Query(value = "select * from car where active=1 and is_new=1 order by id desc limit 10",nativeQuery = true)
    List<Car> findAllActiveCarLimit10();


    @Query(value = "select * from car where active=1 order by id desc",nativeQuery = true)
    List<Car> findAllActiveCar();


    @Query(value = "select * from car where active=1 and slugurl=:model",nativeQuery = true)
    Optional<Car> findActiveCarByModel(@Param("model") String model);

    @Query(value = "select * from car where active=1 and vehicle_type=:type",nativeQuery = true)
    List<Car> findActiveCarByType(@Param("type") String type);


    @Query(value = "select vehicle_type as type,count(*) as size from car where active=1 group by vehicle_type",nativeQuery = true)
    List<CarSizeList> countActiveCarByType();

    @Query(value = "select * from car where model=:model",nativeQuery = true)
    Optional<Car> findCarByModel(@Param("model") String model);


    @Query(value = "select * from car where lower(model) like lower(concat('%', ?1,'%')) and active=1",nativeQuery = true)
    List<Car> findByModelIsContainingIgnoreCase(String model);
}
