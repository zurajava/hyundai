package com.web.hyundai.controller.car.complect;


import com.web.hyundai.model.Path;
import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.car.modif.CarTire;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.car.modif.CarTireRepo;
import com.web.hyundai.service.ImageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RestController
@Api(tags = "Car Tires")
@CrossOrigin("*")
public class CarTireController {

    @Autowired
    private CarTireRepo carTireRepo;

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private ImageService imageService;

    @GetMapping("admin/car/tire/get/{tireid}")
    public ResponseEntity<?> getTire(@PathVariable Long tireid) {
        Optional<CarTire> tire = carTireRepo.findById(tireid);
        return tire.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("admin/car/tire/getall/{carid}")
    public List<CarTire> getTireList(@PathVariable Long carid) {
        return carTireRepo.findAllTiresByCarId(carid);
    }

    @PostMapping(path = "admin/car/tire/create/{carid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> createTire(
            @PathVariable Long carid,
            @RequestParam String title,
            @RequestParam("image") MultipartFile image
    ) throws IOException {

        Optional<Car> car = carRepo.findById(carid);
        if (car.isPresent()) {

            String file = imageService.uploadNewDir(image, Path.getTire(car.get().getModel().trim().toLowerCase()));
            return ResponseEntity.ok(carTireRepo.save(new CarTire(title, file,car.get())));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი მანქანა ვერ მოიძებნა");
    }

    @PostMapping(path = "admin/car/tire/update/{tireid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> updateTire(
            @PathVariable Long tireid,
            @RequestParam String title,
            @RequestParam(value = "image",required = false) MultipartFile image
    ) throws IOException {

        Optional<CarTire> carTire = carTireRepo.findById(tireid);
        if (carTire.isPresent()) {
            CarTire tire = carTire.get();

            tire.setTitle(title);
            if (image != null && image.getBytes().length > 0) {
                imageService.deleteFile(tire.getImage());
                String file = imageService.uploadNewDir(image, Path.getTire(tire.getCar().getModel().trim().toLowerCase()));
                tire.setImage(file);
            }
            return ResponseEntity.ok(carTireRepo.save(tire));

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი საბურავი ვერ მოიძებნა");
    }


    @PostMapping(path = "/admin/car/tire/delete/{tireid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> deleteTire(@PathVariable Long tireid) {

        Optional<CarTire> carTire = carTireRepo.findById(tireid);
        if (carTire.isPresent()) {

            imageService.deleteFile(carTire.get().getImage());
            carTireRepo.deleteById(tireid);
            return ResponseEntity.ok("წარმატებით წაიშალა");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი საბურავი ვერ მოიძებნა");

    }


}