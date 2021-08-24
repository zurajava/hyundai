package com.web.hyundai.repo.usedcars;

import com.web.hyundai.model.usedcars.UsedCar;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsedCarRepo extends JpaRepository<UsedCar,Long>, JpaSpecificationExecutor<UsedCar> {



    @Query(value="select id,model_id,price,year,mileage,fuel,ext_color,display_photo,transmission,int_color,hp,engine from used_car"
    ,nativeQuery = true)
    List<Object[]> findallCarWeb(Pageable firstPageWithTwoElements);

    @Query(value="select * from used_car where model_id=?1",nativeQuery = true)
    List<UsedCar> findallByModelId(Long id);

//
//    @Query(value="select id,model,price,year,mileage,fuel,extColor,displayPhoto from UsedCar")
//    List<Object[]> OrderByDateDesc(Pageable firstPageWithTwoElements);
//
//    @Query(value="select id,model,price,year,mileage,fuel,extColor,displayPhoto from UsedCar")
//    List<Object[]> OrderByPriceDesc(Pageable firstPageWithTwoElements);
//
//    @Query(value="select id,model,price,year,mileage,fuel,extColor,displayPhoto from UsedCar")
//    List<Object[]> OrderByPriceAsc(Pageable firstPageWithTwoElements);
//
//    @Query(value="select id,model,price,year,mileage,fuel,extColor,displayPhoto from UsedCar")
//    List<Object[]> OrderByYearDesc(Pageable firstPageWithTwoElements);
//
//    @Query(value="select id,model,price,year,mileage,fuel,extColor,displayPhoto from UsedCar")
//    List<Object[]> OrderByYearAsc(Pageable firstPageWithTwoElements);
//
//    @Query(value="select id,model,price,year,mileage,fuel,extColor,displayPhoto from UsedCar")
//    List<Object[]> OrderByMileageAsc(Pageable firstPageWithTwoElements);
//
//    @Query(value="select id,model,price,year,mileage,fuel,extColor,displayPhoto from UsedCar")
//    List<Object[]> OrderByMileageDesc(Pageable firstPageWithTwoElements);






}
