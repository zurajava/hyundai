package com.web.hyundai.controller.car;

import com.web.hyundai.model.Path;
import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.car.Comfort;
import com.web.hyundai.model.car.PhotoFeatures;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.car.PhotoFeaturesRepo;
import com.web.hyundai.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = "Car Interior and Exterior features")
@CrossOrigin("*")

public class PhotoFeatureController {


    @Autowired
    CarRepo carRepo;

    @Autowired
    ImageService imageService;

    @Autowired
    PhotoFeaturesRepo photoFeaturesRepo;


    @GetMapping(path = "/admin/car-photofeature/findall/{carid}",produces = "application/json;**charset=UTF-8**")
    public List<PhotoFeatures> findAllPhotoFeaturetByCar(@PathVariable Long carid){
        return photoFeaturesRepo.findAllByCarId(carid);
    }

    @GetMapping(path = "/admin/car-photofeature/get/{featureid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> findPhotoFeaturetById(@PathVariable Long featureid){
        Optional<PhotoFeatures> feature = photoFeaturesRepo.findById(featureid);
        if (feature.isPresent()) {
            return ResponseEntity.ok(feature);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ვერ მოიძებნა");

    }




    // dropdown asarchevi type "int" an "ext"  ;
    @PostMapping(path = "/admin/car-photofeature/create/{carid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> photoFeatureCreate(@PathVariable Long carid,
                                                     @RequestParam("file") MultipartFile file,
                                                     @RequestParam()String title,
                                                     @RequestParam()String titleGEO,
                                                     @RequestParam()String desc,
                                                     @RequestParam()String descGEO,
                                                     @RequestParam()
                                                         @ApiParam(value = "მოვიდეს int ინტერიერისთვის და ext ექსტერიერისთვის",required = true)
                                                             String type) throws IOException {
        Optional<Car> car = carRepo.findById(carid);
        if (car.isPresent()) {

            PhotoFeatures photoFeatures = new PhotoFeatures();
            photoFeatures.setTitle(title);
            photoFeatures.setTitleGEO(titleGEO);
            photoFeatures.setDesc(desc);
            photoFeatures.setDescGEO(descGEO);
            if (type.toLowerCase().equals("int") || type.toLowerCase().equals("ext")) photoFeatures.setType(type);
            photoFeatures.setCar(car.get());


            photoFeatures.setImage(imageService.uploadNewDir(file, Path.CAR_PATH_PHOTOFEATURE));



            return ResponseEntity.ok().body(photoFeaturesRepo.save(photoFeatures));

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი მანქანა არ მოიძებნა");


    }


    @PostMapping(path = "/admin/car-photofeature/update/{featureid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> PhotoFeatureUpdate(
            @PathVariable Long featureid,
            @RequestParam(value = "file",required = false) MultipartFile file,
            @RequestParam()String title,
            @RequestParam()String titleGEO,
            @RequestParam()String desc,
            @RequestParam()String descGEO,
            @RequestParam()
            @ApiParam(value = "მოვიდეს int ინტერიერისთვის და ext ექსტერიერისთვის",required = true)
                    String type) throws IOException {

        Optional<PhotoFeatures> feature = photoFeaturesRepo.findById(featureid);
        if (feature.isPresent()) {

            if(file != null && file.getBytes().length > 0) {
                File oldFile = new File(Path.folderPath() + feature.get().getImage());
                if (oldFile.exists()) oldFile.delete();
                feature.get().setImage(imageService.uploadNewDir(file, Path.CAR_PATH_PHOTOFEATURE));

            }
            feature.get().setTitle(title);
            feature.get().setTitleGEO(titleGEO);
            feature.get().setDesc(desc);
            feature.get().setDescGEO(descGEO);
            if (type.toLowerCase().equals("int") || type.toLowerCase().equals("ext")) feature.get().setType(type);


            return ResponseEntity.ok().body(photoFeaturesRepo.save(feature.get()));

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი PhotoFeature არ მოიძებნა");


    }


    @PostMapping(path = "/admin/car-photofeature/delete/{featureid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> photoFeatureDelete(@PathVariable Long featureid) {
        Optional<PhotoFeatures> photo = photoFeaturesRepo.findById(featureid);
        if (photo.isPresent()) {


            File oldFile = new File(Path.folderPath() + photo.get().getImage());
            if (oldFile.exists()) oldFile.delete();


            photoFeaturesRepo.delete(photo.get());

            return ResponseEntity.ok().body("წარმატებით წაიშალა");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი PhotoFeature ვერ მოიძებნა");


    }


}
