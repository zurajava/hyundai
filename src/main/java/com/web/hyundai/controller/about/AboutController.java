package com.web.hyundai.controller.about;


import com.web.hyundai.model.FileUpload;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.about.About;
import com.web.hyundai.model.about.AboutFeature;
import com.web.hyundai.model.about.AboutWeb;
import com.web.hyundai.repo.about.AboutFeatureRepo;
import com.web.hyundai.repo.about.AboutRepo;
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
@Api(tags = "About Me")
@CrossOrigin("*")
public class AboutController {

    @Autowired
    AboutRepo aboutRepo;

    @Autowired
    ImageService imageService;

    @Autowired
    AboutFeatureRepo aboutFeatureRepo;




    @GetMapping(path = "/admin/about/get-update",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> getInfo() {

        List<About> infos = aboutRepo.findAll();
        if (infos.size() > 0) {
            return ResponseEntity.ok(new AboutWeb(infos.get(0)));

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ჩანაწერი არ არსებობს");
    }


    @GetMapping(path = "/api/about/get",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> getInfo(
            @RequestHeader(value = "accept-language",defaultValue = "en") String lang) {

        List<About> infos = aboutRepo.findAll();
        if (infos.size() > 0) {


            About info = infos.get(0);

            if (lang.equals("ka")) {
                info.setMainSliderTitle(info.getMainSliderTitleGEO());
                info.setMidSliderTitle(info.getMidSliderTitleGEO());
                info.setHistory1(info.getHistory1GEO());
                info.setHistory2(info.getHistory2GEO());
                info.setVision(info.getVisionGEO());
                info.getFeatures().forEach(aboutFeature -> {

                    aboutFeature.setDesc(aboutFeature.getDescGEO());
                    aboutFeature.setTitle(aboutFeature.getTitleGEO());
                });
            }

            return ResponseEntity.ok(info);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("არ მოიძებნა ჩანაწერი");
    }


    @PostMapping(path = "/admin/about/update",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> updateInfo(
            @RequestParam(value = "mainSlider", required = false) MultipartFile mainSlider,
            @RequestParam() String mainSliderTitle,
            @RequestParam() String mainSliderTitleGEO,
            @RequestParam(value = "midSlider", required = false) MultipartFile midSlider,
            @RequestParam() String midSliderTitle,
            @RequestParam() String midSliderTitleGEO,
            @RequestParam() String history1,
            @RequestParam() String history1GEO,
            @RequestParam() String history2,
            @RequestParam() String history2GEO,
            @RequestParam(value = "botSlider", required = false) MultipartFile botSlider,
            @RequestParam() String vision,
            @RequestParam() String visionGEO) throws IOException {

        List<About> info = aboutRepo.findAll();
        if (info.size() > 0) {

            About about = info.get(0);

            about.setMainSliderTitle(mainSliderTitle);
            about.setMainSliderTitleGEO(mainSliderTitleGEO);
            about.setMidSliderTitle(midSliderTitle);
            about.setMidSliderTitleGEO(midSliderTitleGEO);
            about.setHistory1(history1);
            about.setHistory1GEO(history1GEO);
            about.setHistory2(history2);
            about.setHistory2GEO(history2GEO);
            about.setVision(vision);
            about.setVisionGEO(visionGEO);

            if (mainSlider != null && mainSlider.getSize() > 0) {
                File file = new File(Path.folderPath() + about.getMainSlider());
                if (file.exists() && !file.getName().equals("sample.png")) file.delete();
                FileUpload fileUpload = imageService.uploadImage(mainSlider, Path.aboutPath());
                about.setMainSlider(Path.aboutPath() + fileUpload.getFile().getName());

            }
            if (midSlider != null && midSlider.getSize() > 0) {
                File file = new File(Path.folderPath() + about.getMidSlider());
                if (file.exists() && !file.getName().equals("sample.png")) file.delete();
                FileUpload fileUpload = imageService.uploadImage(midSlider, Path.aboutPath());
                about.setMidSlider(Path.aboutPath() + fileUpload.getFile().getName());

            }
            if (botSlider != null && botSlider.getSize() > 0) {
                File file = new File(Path.folderPath() + about.getBotSlider());
                if (file.exists() && !file.getName().equals("sample.png")) file.delete();
                FileUpload fileUpload = imageService.uploadImage(botSlider, Path.aboutPath());
                about.setBotSlider(Path.aboutPath() + fileUpload.getFile().getName());

            }


            return ResponseEntity.ok(aboutRepo.save(about));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ჩანაწერი ვერ მოიძებნა");
    }


    @PostMapping(path = "/admin/about-feature/create",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> featureCreate(
            @RequestParam() String title,
            @RequestParam() String titleGEO,
            @RequestParam() String desc,
            @RequestParam() String descGEO,
            @RequestParam() @ApiParam(value = "სექციების ტიპები მოვიდეს top ან bot",required = true) String type,
            @RequestParam(value = "image") MultipartFile image
    ) throws IOException {

        List<About> info = aboutRepo.findAll();
        if (info.size() > 0) {
            About about = info.get(0);

            AboutFeature aboutFeature = new AboutFeature(title, titleGEO, desc, descGEO);
            aboutFeature.setType("top");

            if (type.toLowerCase().equals("bot")) {
                aboutFeature.setType(type);
            }


            if (image != null && image.getSize() > 0) {
                //File file = new File(Path.folderPath() + aboutFeature.getImage());
                //if (file.exists()) file.delete();
                FileUpload fileUpload = imageService.uploadImage(image, Path.aboutPath());



                System.out.println(Path.aboutPath() + fileUpload.getFile().getName());
                aboutFeature.setImage(Path.aboutPath() + fileUpload.getFile().getName());
                System.out.println(aboutFeature.getImage());

            }
            AboutFeature saved = aboutFeatureRepo.save(aboutFeature);

            about.getFeatures().add(aboutFeature);
            aboutRepo.save(about);


            return ResponseEntity.ok(saved);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ჩანაწერი ვერ მოიძებნა");
    }


    @GetMapping("/api/about-feature/getall")
    public ResponseEntity<List<AboutFeature>> featureList(
            @RequestHeader(value = "accept-language",defaultValue = "en") String lang
    ) {
        List<AboutFeature> features = aboutFeatureRepo.findAll();

        if (lang.equals("ka")) {
            features.forEach(aboutFeature -> {
                aboutFeature.setDesc(aboutFeature.getDescGEO());
                aboutFeature.setTitle(aboutFeature.getTitleGEO());
            });
        }

        return ResponseEntity.ok(features);
    }


    @PostMapping(path = "/admin/about-feature/update/{featureid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> featureUpdate(
            @PathVariable() Long featureid,
            @RequestParam() String title,
            @RequestParam() String titleGEO,
            @RequestParam() String desc,
            @RequestParam() String descGEO,
            @RequestParam() @ApiParam(value = "სექციების ტიპები მოვიდეს top ან bot",required = true) String type,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {

        Optional<AboutFeature> feature = aboutFeatureRepo.findById(featureid);
        if (feature.isPresent()) {

            AboutFeature aboutFeature = feature.get();
            aboutFeature.setTitle(title);
            aboutFeature.setTitleGEO(titleGEO);
            aboutFeature.setDesc(desc);
            aboutFeature.setDescGEO(descGEO);

            if (type.toLowerCase().equals("bot") || type.toLowerCase().equals("top")) {
                aboutFeature.setType(type);
            }


            if (image != null && image.getSize() > 0) {
                File file = new File(Path.folderPath() + aboutFeature.getImage());
                if (file.exists()) file.delete();
                FileUpload fileUpload = imageService.uploadImage(image, Path.aboutPath());
                aboutFeature.setImage(Path.aboutPath() + fileUpload.getFile().getName());

            }
            AboutFeature saved = aboutFeatureRepo.save(aboutFeature);


            return ResponseEntity.ok(saved);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი feature ვერ მოიძებნა");
    }

    @PostMapping(path = "/admin/about-feature/delete/{featureid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> deleteFeature(@PathVariable() Long featureid) {

        Optional<AboutFeature> feature = aboutFeatureRepo.findById(featureid);
        List<About> info = aboutRepo.findAll();
        if (info.size() > 0 && feature.isPresent()) {
            About about = info.get(0);

            about.getFeatures().remove(feature.get());
            aboutRepo.save(about);
            aboutFeatureRepo.delete(feature.get());

            File file = new File(Path.folderPath() + feature.get().getImage());
            if (file.exists()) file.delete();


            return ResponseEntity.ok("წარმატებით წაიშალა");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("about ან feature ვერ მოიძებნა");
    }
}
