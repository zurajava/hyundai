package com.web.hyundai.controller.car;


import com.web.hyundai.model.FileUpload;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.car.ModedPhoto;
import com.web.hyundai.model.car.ModedPhotoDetails;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.car.ModedPhotoDetailsRepo;
import com.web.hyundai.repo.car.ModedPhotoRepo;
import com.web.hyundai.service.ImageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

//@RestController
//@Api(tags = "Car-ModedPhoto | პლიუსებიანი სურათი")
//@CrossOrigin("*")

public class ModedPhotoController {



    @Autowired
    CarRepo carRepo;

    @Autowired
    ImageService imageService;

    @Autowired
    ModedPhotoRepo modedPhotoRepo;

    @Autowired
    ModedPhotoDetailsRepo modedPhotoDetailsRepo;



    @PostMapping("/admin/car-gallery/create/{carid}")
    public ResponseEntity<String> galleryCreate(@PathVariable Long carid,
                                                    @RequestParam("file") MultipartFile file) {
        Optional<Car> car = carRepo.findById(carid);
        if (car.isPresent()) {


                ModedPhoto gallery = new ModedPhoto();
                gallery.setCar(car.get());

                try {
                    gallery.setImage(imageService.uploadNewDir(file, Path.CAR_GALLERY));
                    modedPhotoRepo.save(gallery);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            return ResponseEntity.ok().body("created");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");


    }


    @PostMapping("/admin/car-gallery/update/{galeryid}")
    public ResponseEntity<String> galleryUpdate(@PathVariable Long galeryid,
                                                    @RequestParam("file") MultipartFile file) throws IOException {
        Optional<ModedPhoto> photo = modedPhotoRepo.findById(galeryid);
        if (photo.isPresent()) {

            photo.get().setImage(imageService.uploadNewDir(file, Path.CAR_GALLERY));

            File oldFile = new File(Path.folderPath() + photo.get().getImage());
            if (oldFile.exists()) oldFile.delete();

            modedPhotoRepo.save(photo.get());

            return ResponseEntity.ok().body("created");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");


    }


    @PostMapping("/admin/car-gallery/delete/{galeryid}")
    public ResponseEntity<String> galleryDelete(@PathVariable Long galeryid) {
        Optional<ModedPhoto> photo = modedPhotoRepo.findById(galeryid);
        if (photo.isPresent()) {

            //delete childs
            List<ModedPhotoDetails> details = modedPhotoDetailsRepo.findAllByPhotoId(photo.get().getId());
            modedPhotoDetailsRepo.deleteAll(details);
            details.forEach(modedPhotoDetails -> imageService.deleteFile(modedPhotoDetails.getImage()));
            //delete parent
            modedPhotoRepo.delete(photo.get());
            imageService.deleteFile(photo.get().getImage());


            return ResponseEntity.ok().body("removed");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");


    }


}
