package com.web.hyundai.repo.car.modif;

import com.web.hyundai.model.car.Photo360;
import com.web.hyundai.model.car.modif.ComplectParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ComplectParamRepo extends JpaRepository<ComplectParam,Long> {

    @Query(value = "select * from complect_param where complect_id = :id",nativeQuery = true)
    List<ComplectParam> findAllByComplectId(@Param("id") Long id);


    @Query(value = "select * from complect_param where id in :paramlist",nativeQuery = true)
    Set<ComplectParam> findAllByIdList(@Param("paramlist") List<Long> paramlist);
}
