package com.web.hyundai.controller.car;


import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.car.Feature;
import com.web.hyundai.model.car.web.FeatureWeb;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.car.FeatureRepo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = "Car-Features | ძირითადი მაჩვენებლები")
@CrossOrigin("*")

public class FeatureController {

    @Autowired
    FeatureRepo featureRepo;

    @Autowired
    CarRepo carRepo;



    @GetMapping(path = "/admin/car-fefature/findall/{carid}",produces = "application/json;**charset=UTF-8**")
    public List<Feature> findAllFeatureByCar(@PathVariable Long carid){
        return featureRepo.findAllByCarId(carid);
    }

    @GetMapping(path = "/admin/car-fefature/get/{featureid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> findFeatureById(@PathVariable Long featureid){
        Optional<Feature> feature = featureRepo.findById(featureid);
        if (feature.isPresent()) {
            return ResponseEntity.ok(feature);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ვერ მოიძებნა");

    }


    @PostMapping(path="/admin/car-feature/create/{carid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> createFeature(@PathVariable Long carid,
                                                    @RequestParam("text") String name,
                                                    @RequestParam("textGEO") String nameGEO) {
        Optional<Car> car = carRepo.findById(carid);
        if (car.isPresent()) {


            Feature feature = new Feature();
            feature.setName(name);
            feature.setNameGEO(nameGEO);
            feature.setCar(car.get());


            return ResponseEntity.ok().body(featureRepo.save(feature));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი მანქანა ვერ მოიძებნა");

    }

    @PostMapping(path = "/admin/car-feature/update/{featureid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> updateFeature(@PathVariable Long featureid,
                                                    @RequestParam("text") String name,
                                                    @RequestParam("textGEO") String nameGEO) {
        Optional<Feature> feature = featureRepo.findById(featureid);
        if (feature.isPresent()) {
            feature.get().setName(name);
            feature.get().setNameGEO(nameGEO);


            return ResponseEntity.ok().body(featureRepo.save(feature.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი FEATURE ვერ მოიძებნა");

    }

    @PostMapping(path = "/admin/car-feature/delete/{featureid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> deleteFeature(@PathVariable Long featureid) {
        Optional<Feature> feature = featureRepo.findById(featureid);
        if (feature.isPresent()) {
            featureRepo.deleteById(featureid);
            return ResponseEntity.ok().body("წარმატებით წაიშალა");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი FEATURE ვერ მოიძებნა");

    }
}
