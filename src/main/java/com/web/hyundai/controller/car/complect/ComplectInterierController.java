package com.web.hyundai.controller.car.complect;

import com.web.hyundai.model.Path;
import com.web.hyundai.model.car.modif.ComplectInterier;
import com.web.hyundai.model.car.modif.ComplectInterierFeature;
import com.web.hyundai.model.car.modif.web.EngineWithComplectWeb;
import com.web.hyundai.repo.car.EngineRepo;
import com.web.hyundai.repo.car.modif.CarComplectRepo;
import com.web.hyundai.repo.car.modif.ComplectInterierFeatureRepo;
import com.web.hyundai.repo.car.modif.ComplectInterierRepo;
import com.web.hyundai.service.ImageService;
import com.web.hyundai.service.car.CarBuildService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Api(tags = "Complect Interier")
@CrossOrigin("*")
public class ComplectInterierController {

    private final String  FILTER_NAME = "InterierFilter";
    private final Set<String> EXCLUDED_FIELDS = Stream.of("carComplect").collect(Collectors.toSet());


    @Autowired
    private ComplectInterierRepo complectInterierRepo;

    @Autowired
    private ComplectInterierFeatureRepo complectInterierFeatureRepo;

    @Autowired
    private CarComplectRepo carComplectRepo;

    @Autowired
    private ImageService imageService;
    @Autowired
    private CarBuildService carBuildService;

    @Autowired
    EngineRepo engineRepo;


    @GetMapping(path = "/admin/complect/interier/findbycomplect/{complectid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> ComplectInterierFindByComplect(@PathVariable Long complectid) throws IOException {

        Optional<ComplectInterier> complectInt = complectInterierRepo.findByComplectId(complectid);
        if (complectInt.isPresent()) {
            return ResponseEntity.ok().body(carBuildService.jsonExcludeFilter(FILTER_NAME,EXCLUDED_FIELDS,complectInt.get()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("სურათი ცარიელია");

    }

    @PostMapping(path = "/admin/complect/interier/create", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> ComplectInterierCreate(@RequestParam MultipartFile image) throws IOException {


        if (image != null && image.getBytes().length > 0) {
            String img = imageService.uploadNewDir(image, Path.getComplectIntPath());


            return ResponseEntity.ok().body(carBuildService.jsonExcludeFilter(FILTER_NAME,EXCLUDED_FIELDS,complectInterierRepo.save(new ComplectInterier(img))));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("სურათი ცარიელია");

    }



    @GetMapping(path = "/admin/complect/interier/findall", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> ComplectInterierFindAll() throws IOException {

        return ResponseEntity.ok().body(carBuildService.jsonExcludeFilter(FILTER_NAME,EXCLUDED_FIELDS,complectInterierRepo.findAll()));

    }

    @PostMapping(path = "/admin/complect/interier/update/{complectintid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> ComplectInterierUpdate(@PathVariable Long complectintid,
                                                         @RequestParam MultipartFile image) throws IOException {

        Optional<ComplectInterier> comp = complectInterierRepo.findById(complectintid);
        if (comp.isPresent()) {
            if (image != null && image.getBytes().length > 0) {
                String img = imageService.uploadNewDir(image, Path.getComplectIntPath());
                imageService.deleteFile(comp.get().getImage());
                comp.get().setImage(img);

                return ResponseEntity.ok().body(carBuildService.jsonExcludeFilter(FILTER_NAME,EXCLUDED_FIELDS,complectInterierRepo.save(comp.get())));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("სურათი ცარიელია");

        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი კომპლექტაცია ვერ მოიძებნა");


    }

    @PostMapping(path = "/admin/complect/interier/delete/{complectintid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> ComplectInterierRemove(@PathVariable Long complectintid) throws IOException {

        Optional<ComplectInterier> comp = complectInterierRepo.findById(complectintid);
        if (comp.isPresent()) {
            ComplectInterier oldComp = comp.get();
            complectInterierFeatureRepo.deleteAll(oldComp.getComplectInterierFeatures());
            complectInterierRepo.deleteById(complectintid);
            imageService.deleteFile(oldComp.getImage());
            return ResponseEntity.ok().body("წარმატებით წაიშალა");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი კომპლექტაცია ვერ მოიძებნა");


    }

    @GetMapping(path = "/admin/complect/interier/feature/findall/{compintid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> ComplectInterierFeatureFindAll(@PathVariable Long compintid) throws IOException {

        Optional<ComplectInterier> compInt = complectInterierRepo.findById(compintid);
        if (compInt.isPresent()) {
            return ResponseEntity.ok().body(carBuildService.jsonExcludeFilter(FILTER_NAME,EXCLUDED_FIELDS,compInt.get().getComplectInterierFeatures()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი ComplectInterier ვერ მოიძებნა");


    }

    @GetMapping(path = "/admin/complect/interier/feature/get/{featureid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> ComplectInterierFeatureGet(@PathVariable Long featureid) throws IOException {

        Optional<ComplectInterierFeature> compIntFeat = complectInterierFeatureRepo.findById(featureid);
        if (compIntFeat.isPresent()) {
            return ResponseEntity.ok().body(carBuildService.jsonFilter(FILTER_NAME,compIntFeat.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი ComplectInterier ვერ მოიძებნა");


    }

    @PostMapping(path = "/admin/complect/interier/feature/create/{compintid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> ComplectInterierFeatureCreate(@PathVariable Long compintid,
                                                                @RequestParam String feature,
                                                                @RequestParam String featureGEO) throws IOException {

        Optional<ComplectInterier> compInt = complectInterierRepo.findById(compintid);
        if (compInt.isPresent()) {
            compInt.get().getComplectInterierFeatures().add(new ComplectInterierFeature(feature, featureGEO));

            return ResponseEntity.ok().body(carBuildService.jsonFilter(FILTER_NAME,complectInterierRepo.save(compInt.get())));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი ComplectInterier ვერ მოიძებნა");


    }


    @PostMapping(path = "/admin/complect/interier/feature/update/{featureid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> ComplectInterierFeatureUpdate(@PathVariable Long featureid,
                                                                @RequestParam String feature,
                                                                @RequestParam String featureGEO) throws IOException {

        Optional<ComplectInterierFeature> oldFeature = complectInterierFeatureRepo.findById(featureid);
        if (oldFeature.isPresent()) {
            oldFeature.get().setFeature(feature);
            oldFeature.get().setFeatureGEO(featureGEO);
            return ResponseEntity.ok().body(carBuildService.jsonFilter(FILTER_NAME,complectInterierFeatureRepo.save(oldFeature.get())));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი feature ვერ მოიძებნა");


    }

    @PostMapping(path = "/admin/complect/interier/feature/delete/{featureid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> ComplectInterierFeatureRemove(@PathVariable Long featureid) throws IOException {

        Optional<ComplectInterierFeature> feature = complectInterierFeatureRepo.findById(featureid);
        if (feature.isPresent()) {
            complectInterierFeatureRepo.deleteById(featureid);
            return ResponseEntity.ok().body("წარმატებით წაიშალა");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი feature ვერ მოიძებნა");


    }
}
