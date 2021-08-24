package com.web.hyundai.controller.car;


import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.car.web.CarWeb;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.service.car.CarBuildService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@Api(tags = "Car BluePrint")
@CrossOrigin("*")

public class CarController {



    @Autowired
    private CarBuildService carBuildService;

    @Autowired
    private CarRepo carRepo;


    @GetMapping(path = "/api/car/get/{model}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> getCar(
            @PathVariable String model,
            @RequestHeader(value = "accept-language", defaultValue = "en") String lang) {

        System.out.println(model);
        System.out.println(model.toLowerCase().replaceAll(" ",""));

        CarWeb car = carBuildService.buildCar(model, lang);
        if (car.getId() != null) return ResponseEntity.ok(car);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი მანქანა ვერ მოიძებნა");
    }

    @GetMapping(path = "/admin/car/getcars",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<List<Car>> getCarListForUpdate() {
        return ResponseEntity.ok(carRepo.findAll());


    }



    // type of vehicles SEDAN,SUV,ECO,COMMERCIAL,CROSSOVER
    @PostMapping(path = "/admin/car/create",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> createCar(
            @RequestParam  boolean isElectro,
            @RequestParam boolean isNew,
            @RequestParam("model") String model,
            @RequestParam("year") int year,

            @RequestParam("place")
            @ApiParam(value = "ადგილების რაოდენობა", required = true)
                    int place,
            @RequestParam("title") String title,
            @RequestParam("titleGEO") String titleGEO,
            @RequestParam("slider")
            @ApiParam(value = "მთავარი სლაიდერი ფოტო", required = true)
                    MultipartFile slider,
            @RequestParam("slider2")
            @ApiParam(value = "მეორე სლაიდერი ფოტო", required = true)
                    MultipartFile slider2,
            @RequestParam(value = "videoslider",required = false)
            @ApiParam(value = "ვიდეო სლაიდერი")
                    MultipartFile videoslider,
            @RequestParam(value = "videosliderurl",required = false)
            @ApiParam(value = "ვიდეო სლაიდერის ლინკი")
                    String videosliderurl,
            @RequestParam("logo") MultipartFile logo,
            @RequestParam("file") @ApiParam(value = "მანქანის შაბლონი", required = true) MultipartFile file,
            @RequestParam("price") int price,
            @RequestParam("type")
            @ApiParam(value = "მანქანის ტიპი ზუსტი შესატყვისით: SEDAN,SUV,ECO,COMMERCIAL,CROSSOVER", required = true)
                    String vehicleType,
            @RequestParam("active")
            @ApiParam(value = "აქტიურია თუარა მანქანა საიტზე, 0 ან 1", required = true, defaultValue = "0")
                    int active) throws IOException {

        Car car = carBuildService.createCar(model, year, place, title, titleGEO, slider,slider2,videoslider, logo, file, price, vehicleType, active,videosliderurl,isElectro,isNew);
        if (car.getId() != null && car.getId() == -1L) ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი მოდელი უკვე არსებობს !");
        if (car.getId() != null && car.getId() == -2L) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("მოდელის სახელი შეიცავს არალათინურ ასოებს");
        if (car.getId() != null) return ResponseEntity.ok(car);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("ბაზაში ჩაწერის დროს დაფიქსირდა ერორი");

    }
    // type of vehicles SEDAN,SUV,ECO,COMMERCIAL,CROSSOVER

    @PostMapping(path = "/admin/car/update/{carid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> updateCar(
            @PathVariable Long carid,
            @RequestParam(required = false)  boolean isElectro,
            @RequestParam(required = false) boolean isNew,
            @RequestParam("model") String model,
            @RequestParam("year") int year,

            @RequestParam("place")
            @ApiParam(value = "ადგილების რაოდენობა", required = true)
                    int place,
            @RequestParam("title") String title,
            @RequestParam("titleGEO") String titleGEO,
            @RequestParam(value = "slider",required = false)
            @ApiParam(value = "მთავარი სლაიდერი ფოტო")
                    MultipartFile slider,
            @RequestParam(value = "slider2",required = false)
            @ApiParam(value = "მეორე სლაიდერი ფოტო", required = false)
                    MultipartFile slider2,
            @RequestParam(value = "videoslider",required = false)
            @ApiParam(value = "ვიდეო სლაიდერი", required = false)
                    MultipartFile videoslider,
            @RequestParam("videosliderurl")
            @ApiParam(value = "ვიდეო სლაიდერის ლინკი", required = false)
                    String videosliderurl,
            @RequestParam(value = "logo",required = false) MultipartFile logo,
            @RequestParam(value = "file",required = false) @ApiParam(value = "მანქანის შაბლონი") MultipartFile file,
            @RequestParam("price") int price,
            @RequestParam("type")
            @ApiParam(value = "მანქანის ტიპი ზუსტი შესატყვისით: SEDAN,SUV,ECO,COMMERCIAL,CROSSOVER", required = true)
                    String vehicleType,
            @RequestParam("active")
            @ApiParam(value = "აქტიურია თუარა მანქანა საიტზე, 0 ან 1", required = true, defaultValue = "0")
                    int active) throws IOException {

        Car car = carBuildService
                .updateCar(model, year, place, title, titleGEO, slider,slider2,videoslider, logo,file, price, vehicleType, active, carid,videosliderurl,isElectro,isNew);
        if (car.getId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი მანქანა ვერ მოიძებნა");
        }
        if (car.getId().equals(-1L)) {
            System.out.println("Aqvar");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი მოდელი უკვე არსებობს !");
        }
        if (car.getId() == -2L) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("მოდელის სახელი შეიცავს არალათინურ ასოებს");

        System.out.println(car.getId());
        System.out.println(car.getId().equals(-1L));
        return ResponseEntity.ok(car);




    }

}


