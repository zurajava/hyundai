package com.web.hyundai.controller.usedcars;


import com.web.hyundai.model.FileUpload;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.usedcars.UsedCar;
import com.web.hyundai.model.usedcars.UsedCarPhoto;
import com.web.hyundai.repo.usedcars.UsedCarPhotoRepo;
import com.web.hyundai.repo.usedcars.UsedCarRepo;
import com.web.hyundai.service.ImageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/admin/usedcars")
@Api(tags = "UsedCar Photos")
@CrossOrigin("*")

public class UsedCarPhotoController {


    @Autowired
    private UsedCarPhotoRepo usedCarPhotoRepo;

    @Autowired
    private ImageService imageService;

//    @Autowired
//    private UsedCarFeatureRepo featureRepo;

    @Autowired
    private UsedCarRepo usedCarRepo;


//    @PostMapping("/add-feature/{id}")
//    public ResponseEntity<UsedCar> addFeature(@RequestBody UsedCarFeature usedCarFeature,@PathVariable Long id){
//
//        Optional<UsedCar> car = usedCarRepo.findById(id);
//
//        if(car.isPresent()){
//            car.get().getUsedCarFeatures().add(featureRepo.save(usedCarFeature));
//            return ResponseEntity.ok(usedCarRepo.save(car.get()));
//        }
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UsedCar());
//
//    }
//
//
//
//
//    @PostMapping("/remove-feature/{carid}/{featureid}")
//    @Transactional
//    public ResponseEntity<String> removeFeature(@PathVariable Long carid,
//                                                @PathVariable Long featureid) {
//        Optional<UsedCarFeature> feature = featureRepo.findById(featureid);
//        Optional<UsedCar> usedcar = usedCarRepo.findById(carid);
//        if (usedcar.isPresent()){
//
//
//            System.out.println(usedcar.get().getUsedCarFeatures());
//
//            feature.ifPresent(usedCarFeature -> usedcar.get().getUsedCarFeatures().remove(usedCarFeature));
//            usedCarRepo.save(usedcar.get());
//            featureRepo.deleteById(featureid);
//            return ResponseEntity.ok("1");
//
//        }
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("0");
//
//    }
//
//    @PostMapping("/update-feature/{id}")
//    public ResponseEntity<UsedCarFeature> updateFeature(@PathVariable Long id,
//                                                        String value) {
//
//        Optional<UsedCarFeature> feature = featureRepo.findById(id);
//        if (feature.isPresent()) {
//            feature.get().setTitle(value);
//            return ResponseEntity.ok(featureRepo.save(feature.get()));
//        }
//
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UsedCarFeature());
//
//    }


///////////////////////////////////////////// PHOTOS //////////////////////////////////////////////////////////////////


    @PostMapping(path = "/add-photo/{id}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> addPhotos(@PathVariable Long id,
                                             @RequestParam(value = "file") MultipartFile file) throws IOException {


        Optional<UsedCar> car = usedCarRepo.findById(id);

        if (car.isPresent()) {
            System.out.println(car.get().getUsedCarPhotoList());

            FileUpload fileUpload = imageService.uploadImage(file, Path.usedCarPath());

            UsedCarPhoto newPhoto = new UsedCarPhoto(Path.usedCarPath() + fileUpload.getFile().getName());

            UsedCarPhoto photo = usedCarPhotoRepo.save(newPhoto);
            System.out.println(photo);
            car.get().getUsedCarPhotoList().add(photo);
            System.out.println(car.get().getUsedCarPhotoList());

            return ResponseEntity.ok(usedCarRepo.save(car.get()));

        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი მანქანა ვერ მოიძებნა");


    }


    @PostMapping(path="/update-photo/{photoId}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> updatePhotos(@PathVariable Long photoId,
                                                     @RequestParam(value = "file") MultipartFile file) throws IOException {


        Optional<UsedCarPhoto> photo = usedCarPhotoRepo.findById(photoId);

        if (photo.isPresent()) {
            File oldFile = new File(Path.folderPath() + photo.get().getName());
            if (oldFile.exists()) oldFile.delete();
            FileUpload fileUpload = imageService.uploadImage(file, Path.usedCarPath());

            photo.get().setName(Path.usedCarPath() + fileUpload.getFile().getName());
            return ResponseEntity.ok(usedCarPhotoRepo.save(photo.get()));

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი ფოტო ვერ მოიძებნა");
    }


    @PostMapping(path = "/delete-photo/{carid}/{photoId}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> deletePhotos(@PathVariable Long carid,
                                               @PathVariable Long photoId) {


        Optional<UsedCarPhoto> photo = usedCarPhotoRepo.findById(photoId);
        Optional<UsedCar> car = usedCarRepo.findById(carid);


        if (car.isPresent() && photo.isPresent()) {
            File oldFile = new File(Path.folderPath() + photo.get().getName());
            if (oldFile.exists()) oldFile.delete();

            car.get().getUsedCarPhotoList().remove(photo.get());
            usedCarRepo.save(car.get());
            usedCarPhotoRepo.deleteById(photoId);
            return ResponseEntity.ok("1");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი მანქანა ან ფოტო ვერ მოიძებნა");
    }
}