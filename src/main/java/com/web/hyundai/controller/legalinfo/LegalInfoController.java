package com.web.hyundai.controller.legalinfo;


import com.web.hyundai.model.FileUpload;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.legalinfo.LegalInfo;
import com.web.hyundai.model.legalinfo.LegalSpoiler;
import com.web.hyundai.repo.legalinfo.LegalInfoRepo;
import com.web.hyundai.repo.legalinfo.LegalSpoilerRepo;
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

//@RestController
//@Api(tags = "Legal Info")
//@CrossOrigin("*")

public class LegalInfoController {



    @Autowired
    LegalInfoRepo legalInfoRepo;

    @Autowired
    ImageService imageService;

    @Autowired
    LegalSpoilerRepo legalSpoilerRepo;


    @GetMapping("/api/legalinfo/get")
    public ResponseEntity<LegalInfo>getInfo(){

        Optional<LegalInfo> info = legalInfoRepo.findById(1L);
        if (info.isPresent()) {
            return ResponseEntity.ok(info.get());

        }
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LegalInfo());
    }


    @PostMapping("/admin/legalinfo/update")
    public ResponseEntity<LegalInfo>updateInfo(
            @RequestParam() String title,
            @RequestParam() String titleGEO,
            @RequestParam() String text,
            @RequestParam() String textGEO,
            @RequestParam(value = "image",required = false)MultipartFile image) throws IOException {

        Optional<LegalInfo> info = legalInfoRepo.findById(1L);
        if (info.isPresent()) {

            info.get().setTitle(title);
            info.get().setTitleGEO(titleGEO);
            info.get().setText(text);
            info.get().setTextGEO(textGEO);

            if(image != null){
                File file = new File(Path.folderPath() + info.get().getImage());
                if (file.exists() && !file.getName().equals("sample.png")) file.delete();
                FileUpload fileUpload = imageService.uploadImage(image, Path.legalInfoPath());
                info.get().setImage(Path.legalInfoPath() + fileUpload.getFile().getName());

            }


            return ResponseEntity.ok(legalInfoRepo.save(info.get()));
        }
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LegalInfo());
    }


    @PostMapping("/admin/legalinfo/add-spoiler")
    public ResponseEntity<LegalInfo>addSpoiler(
            @RequestParam() String title,
            @RequestParam() String titleGEO,
            @RequestParam(value = "image")MultipartFile image,
            @RequestParam(value = "logo")MultipartFile logo) throws IOException {

        Optional<LegalInfo> info = legalInfoRepo.findById(1L);
        if (info.isPresent()) {

            LegalSpoiler legalSpoiler = new LegalSpoiler();
            legalSpoiler.setTitle(title);
            legalSpoiler.setTitleGEO(titleGEO);

            FileUpload logoUpload = imageService.uploadImage(logo, Path.legalInfoPath());
            legalSpoiler.setLogo(Path.legalInfoPath() + logoUpload.getFile().getName());

            imageService.uploadFile(Path.legalInfoPath(),image);
            legalSpoiler.setFile(Path.legalInfoPath() +  image.getOriginalFilename());
            legalSpoiler.setFileSize(image.getSize());

            legalSpoilerRepo.save(legalSpoiler);
            info.get().getLegalSpoilers().add(legalSpoiler);

            return ResponseEntity.ok(legalInfoRepo.save(info.get()));
        }
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LegalInfo());
    }

    @PostMapping("/admin/legalinfo/remove-spoiler/{id}")
    public ResponseEntity<String>removeSpoiler(@PathVariable Long id)  {

        Optional<LegalInfo> info = legalInfoRepo.findById(1L);
        if (info.isPresent()) {

            Optional<LegalSpoiler> spoiler = legalSpoilerRepo.findById(id);
            if (spoiler.isPresent()) {

                File file = new File(Path.folderPath() + spoiler.get().getFile());
                File logo = new File(Path.folderPath() + spoiler.get().getLogo());



                if (file.exists()) file.delete();
                if (logo.exists()) logo.delete();

                info.get().getLegalSpoilers().remove(spoiler.get());
                legalInfoRepo.save(info.get());
                legalSpoilerRepo.delete(spoiler.get());
                return ResponseEntity.ok("1");


            }


        }
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("0");
    }









}
