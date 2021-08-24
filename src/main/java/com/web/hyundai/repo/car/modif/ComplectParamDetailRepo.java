package com.web.hyundai.repo.car.modif;

import com.web.hyundai.model.car.modif.ComplectParam;
import com.web.hyundai.model.car.modif.ComplectParamDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComplectParamDetailRepo extends JpaRepository<ComplectParamDetail,Long> {

    @Query(value = "select * from complect_param_detail where complect_param = :id",nativeQuery = true)
    List<ComplectParamDetail> findAllByParamId(@Param("id") Long id);
}
