package com.web.hyundai.controller.car;

import com.web.hyundai.model.FileUpload;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.car.DesignType;
import com.web.hyundai.model.car.ModedPhoto;
import com.web.hyundai.model.car.ModedPhotoDetails;
import com.web.hyundai.repo.car.ModedPhotoDetailsRepo;
import com.web.hyundai.repo.car.ModedPhotoRepo;
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
import java.util.Optional;

//@RestController
//@CrossOrigin("*")
//@Api(tags = "Car ModedPhoto Detail | პლიუსებიანი სურათის დეტალები")
public class ModedPhotoDetailController {


    @Autowired
    ModedPhotoDetailsRepo modedPhotoDetailsRepo;

    @Autowired
    ModedPhotoRepo modedPhotoRepo;

    @Autowired
    ImageService imageService;


    // typeshi movides top interier an modification
    @PostMapping("/admin/car-gallerydetail/create/{photoid}")
    public ResponseEntity<String> photoDetailCreate(@PathVariable Long photoid,
                                                    @RequestParam("photo") MultipartFile photo,
                                                    @RequestParam()
                                                        @ApiParam(value = "დასმული წერტილის განედი",required = true) int width,
                                                    @RequestParam()
                                                        @ApiParam(value = "დასმული წერტილის გრძედი",required = true)int height,
                                                    @RequestParam() String title,
                                                    @RequestParam() String titleGEO,
                                                    @RequestParam()
                                                        @ApiParam(value = "რა ნაწილს მიეკუთვნის: რადიო,ფარები,საჭე...",required = true)String part,
                                                    @RequestParam()
                                                        @ApiParam(value = "ფეიჯის რომელ ნაწილს მიეკუთვნის: int,modif,top",required = true)String type
                                                    ) throws IOException {
        DesignType designType1 = DesignType.TOP;


        Optional<ModedPhoto> mainPhoto = modedPhotoRepo.findById(photoid);

        if (mainPhoto.isPresent()) {
            ModedPhotoDetails modedPhotoDetails = new ModedPhotoDetails();
            modedPhotoDetails.setWidth(width);
            modedPhotoDetails.setHeight(height);
            modedPhotoDetails.setTitle(title);
            modedPhotoDetails.setTitleGEO(titleGEO);
            modedPhotoDetails.setPart(part);

            if (type.toLowerCase().equals("int")) designType1 = DesignType.INTERIER;
            if (type.toLowerCase().equals("modif")) designType1 = DesignType.MODIFICATION;
            modedPhotoDetails.setDesignType(designType1);

            if (photo.getBytes().length > 0) {

                modedPhotoDetails.setImage(imageService.uploadNewDir(photo, Path.CAR_GALLERY));

            }
            modedPhotoDetailsRepo.save(modedPhotoDetails);
            return ResponseEntity.ok().body("created");


        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");


    }

    @PostMapping("/admin/car-gallerydetail/update/{detailid}")
    public ResponseEntity<String> photoDetailUpdate(@PathVariable Long detailid,
                                                    @RequestParam(value = "photo",required = false) MultipartFile photo,
                                                    @RequestParam()
                                                        @ApiParam(value = "დასმული წერტილის განედი",required = true) int width,
                                                    @RequestParam()
                                                        @ApiParam(value = "დასმული წერტილის გრძედი",required = true)int height,
                                                    @RequestParam() String title,
                                                    @RequestParam() String titleGEO,
                                                    @RequestParam()
                                                        @ApiParam(value = "რა ნაწილს მიეკუთვნის: რადიო,ფარები,საჭე...",required = true)String part,
                                                    @RequestParam()
                                                        @ApiParam(value = "ფეიჯის რომელ ნაწილს მიეკუთვნის: int,modif,top",required = true)String type) throws IOException {


        Optional<ModedPhotoDetails> modedPhotoDetails = modedPhotoDetailsRepo.findById(detailid);

        if (modedPhotoDetails.isPresent()) {
            DesignType designType1 = modedPhotoDetails.get().getDesignType();


            modedPhotoDetails.get().setWidth(width);
            modedPhotoDetails.get().setHeight(height);
            modedPhotoDetails.get().setTitle(title);
            modedPhotoDetails.get().setTitleGEO(titleGEO);
            modedPhotoDetails.get().setPart(part);

            if (type.toLowerCase().equals("int")) designType1 = DesignType.INTERIER;
            if (type.toLowerCase().equals("modif")) designType1 = DesignType.MODIFICATION;
            if (type.toLowerCase().equals("top")) designType1 = DesignType.TOP;

            modedPhotoDetails.get().setDesignType(designType1);

            if (photo.getBytes().length > 0) {
                modedPhotoDetails.get().setImage(imageService.uploadNewDir(photo, Path.CAR_GALLERY));

            }
            modedPhotoDetailsRepo.save(modedPhotoDetails.get());
            return ResponseEntity.ok().body("updated");


        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");


    }

    @PostMapping("/admin/car-gallerydetail/delete/{detailid}")
    public ResponseEntity<String> photoDetailDelete(@PathVariable Long detailid) {
        Optional<ModedPhotoDetails> detail = modedPhotoDetailsRepo.findById(detailid);
        if (detail.isPresent()) {


            File oldFile = new File(Path.folderPath() + detail.get().getImage());
            if (oldFile.exists()) oldFile.delete();


            modedPhotoDetailsRepo.delete(detail.get());

            return ResponseEntity.ok().body("deleted");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");


    }


}
