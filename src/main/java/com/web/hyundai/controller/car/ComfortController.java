package com.web.hyundai.controller.car;

import com.web.hyundai.model.FileUpload;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.car.Comfort;
import com.web.hyundai.model.car.Feature;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.car.ComfortRepo;
import com.web.hyundai.service.ImageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = "Car-Comfort")
@CrossOrigin("*")
public class ComfortController {
    @Autowired
    CarRepo carRepo;

    @Autowired
    ImageService imageService;

    @Autowired
    ComfortRepo comfortRepo;



    @GetMapping(path = "/admin/car-comfort/findall/{carid}",produces = "application/json;**charset=UTF-8**")
    public List<Comfort> findAllComfortByCar(@PathVariable Long carid){
        return comfortRepo.findAllByCarId(carid);
    }

    @GetMapping(path = "/admin/car-comfort/get/{comfortid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> findComfortById(@PathVariable Long comfortid){
        Optional<Comfort> comfort = comfortRepo.findById(comfortid);
        if (comfort.isPresent()) {
            return ResponseEntity.ok(comfort);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ვერ მოიძებნა");

    }






    @PostMapping(path="/admin/car-comfort/create/{carid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> comfortCreate(@PathVariable Long carid,
                                                @RequestParam("file") MultipartFile file,
                                                @RequestParam() String title,
                                                @RequestParam() String titleGEO,
                                                @RequestParam() String desc,
                                                @RequestParam() String descGEO) throws IOException {
        Optional<Car> car = carRepo.findById(carid);
        if (car.isPresent()) {
            Comfort comfort = new Comfort();
            comfort.setTitle(title);
            comfort.setTitleGEO(titleGEO);
            comfort.setDesc(desc);
            comfort.setDescGEO(descGEO);
            comfort.setCar(car.get());

            comfort.setImage(imageService.uploadNewDir(file, Path.CAR_PATH_COMFORT));


            return ResponseEntity.ok().body(comfortRepo.save(comfort));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი მანქანა არ მოიძებნა");


    }



    @PostMapping("/admin/car-comfort/update/{comfortid}")
    public ResponseEntity<?> comfortUpdate(
            @PathVariable Long comfortid,
            @RequestParam() String title,
            @RequestParam() String titleGEO,
            @RequestParam() String desc,
            @RequestParam() String descGEO,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        Optional<Comfort> comfort = comfortRepo.findById(comfortid);

        if (comfort.isPresent()) {
            comfort.get().setTitle(title);
            comfort.get().setTitleGEO(titleGEO);
            comfort.get().setDesc(desc);
            comfort.get().setDescGEO(descGEO);

            if (file != null && file.getBytes().length > 0) {
                File oldFile = new File(Path.folderPath() + comfort.get().getImage());
                if (oldFile.exists()) oldFile.delete();
                comfort.get().setImage(imageService.uploadNewDir(file, Path.CAR_PATH_COMFORT));

            }

            return ResponseEntity.ok().body(comfortRepo.save(comfort.get()));

        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");


    }


    @PostMapping("/admin/car-comfort/delete/{comfortid}")
    public ResponseEntity<String> comfortDelete(@PathVariable Long comfortid) {
        Optional<Comfort> comfort = comfortRepo.findById(comfortid);
        if (comfort.isPresent()) {


            File oldFile = new File(Path.folderPath() + comfort.get().getImage());
            if (oldFile.exists()) oldFile.delete();


            comfortRepo.delete(comfort.get());

            return ResponseEntity.ok().body("deleted");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");


    }
}
