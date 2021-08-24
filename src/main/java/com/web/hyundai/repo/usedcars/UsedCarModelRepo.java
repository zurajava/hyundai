package com.web.hyundai.repo.usedcars;

import com.web.hyundai.model.usedcars.UsedCarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsedCarModelRepo extends JpaRepository<UsedCarModel,Long> {

    @Query(value = "select * from used_car_model where model_name=:name",nativeQuery = true)
    public List<UsedCarModel> findAllByModelName(@Param("name") String name);
}
