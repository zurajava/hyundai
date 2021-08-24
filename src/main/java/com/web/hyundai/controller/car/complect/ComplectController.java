package com.web.hyundai.controller.car.complect;


import com.web.hyundai.model.FileUpload;
import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.car.Engine;
import com.web.hyundai.model.car.FuelUsage;
import com.web.hyundai.model.car.Photo360;
import com.web.hyundai.model.car.modif.*;
import com.web.hyundai.model.car.modif.web.EngineWithComplectWeb;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.car.EngineRepo;
import com.web.hyundai.repo.car.Photo360Repo;
import com.web.hyundai.repo.car.modif.CarComplectRepo;
import com.web.hyundai.repo.car.modif.CarTireRepo;
import com.web.hyundai.repo.car.modif.ComplectInterierRepo;
import com.web.hyundai.repo.car.modif.ComplectParamRepo;
import com.web.hyundai.service.ComplectService;
import com.web.hyundai.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@RestController
@Api(tags = "Car Complect")
@CrossOrigin("*")
public class ComplectController {


    @Autowired
    CarComplectRepo carComplectRepo;

    @Autowired
    EngineRepo engineRepo;

    @Autowired
    ComplectService complectService;

    @Autowired
    Photo360Repo photo360Repo;

    @Autowired
    CarTireRepo carTireRepo;

    @Autowired
    ComplectInterierRepo complectInterierRepo;

    @Autowired
    ImageService imageService;

    @Autowired
    ComplectParamRepo complectParamRepo;

    @Autowired
    CarRepo carRepo;



    @GetMapping(path = "/api/car/complect/getallengine/{carid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<List<EngineWithComplectWeb>> getAllEngineWithComplect(@PathVariable Long carid) {
        List<EngineWithComplectWeb> complectInfo = new ArrayList<>();
        engineRepo.findAllEnginesWithComplect(carid).forEach(o -> {
            complectInfo.add(new EngineWithComplectWeb(Long.valueOf(o[0].toString()), o[2].toString(), o[1].toString(),
                    o[4].toString(), Long.valueOf(o[3].toString())));
        });
        return ResponseEntity.ok(complectInfo);
    }







    @GetMapping(path = "/api/car/complect/getcomplect/{complectid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> getSingleComplect(
            @PathVariable Long complectid,
            @RequestHeader(value = "accepct-language", defaultValue = "en") String lang) {

        return complectService.createSingleComplect(lang, complectid);
    }


    @GetMapping(path = "/api/car/complect/getaftercomplect/{complectid}/{photo360id}/{tireid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> getAfterComplect(
            @PathVariable Long complectid,
            @PathVariable Long photo360id,
            @PathVariable Long tireid,
            @RequestParam ArrayList<Long> featureList,
            @RequestHeader(value = "accept-language", defaultValue = "en") String lang) {

        return complectService.createAfterComplect(lang, complectid, photo360id, tireid,featureList);
    }


    @PostMapping(path = "/admin/car/complect/create/{engineid}/{complectinterierid}", produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> complectCreate(
            @PathVariable @ApiParam(value = "ერთ კომპლექტზე ერთი ძრავი, წინააღმდეგ შემთხვევაში ოპერაცია არ შესრულდება",
                    required = true) Long engineid,
            @PathVariable("complectinterierid") @ApiParam(value = "მისამაგრებელი ComplectInterier- ის აიდი",
                    required = true) Long complectinterierid,
            @RequestParam("photoidlist") @ApiParam(value = "მისამაგრებელი Photo360-ების აიდების ლისტი",
                    required = true) ArrayList<Long> photoidlist,
            @RequestParam("cartirelist") @ApiParam(value = "მისამაგრებელი CarTire-ების აიდების ლისტი",
                    required = true) ArrayList<Long> cartirelist,
//            @RequestParam("complectparamlist") @ApiParam(value = "მისამაგრებელი ComplectParam-ების აიდების ლისტი",
//                    required = true) ArrayList<Long> complectparam,
            @ApiParam(value = "კომპლექტაციის სახელი", required = true)
            @RequestParam String name,
            @RequestParam String nameGEO,
            @RequestParam MultipartFile pdfFile,
            @RequestHeader(value = "accepct-language", defaultValue = "en") String lang

    ) throws IOException {


        //Set<ComplectParam> complectParamList = complectParamRepo.findAllByIdList(complectparam);
        Optional<Engine> engine = engineRepo.findById(engineid);
        List<Photo360> photos = photo360Repo.findAllByIdList(photoidlist);
        Set<CarTire> tires = carTireRepo.findAllByIdList(cartirelist);
        Optional<ComplectInterier> compInt = complectInterierRepo.findWhereComplectInterierIsEmpty(complectinterierid);

        boolean check = engine.isPresent() && carComplectRepo.findByEngineId(engineid).isEmpty()
                && photos.size() == photoidlist.size() && tires.size() == cartirelist.size() && compInt.isPresent();
                //&& complectparam.size() == complectParamList.size();
        if (check) {
            CarComplect carComplect = new CarComplect();
            Engine engine_ = engine.get();
            FuelUsage fuelUsage = engine_.getFuelUsage();
            carComplect.setName(name);
            carComplect.setNameGEO(nameGEO);
            carComplect.setEngine(engine.get());
            carComplect.setPhoto360(photos);
            carComplect.setCarTires(tires);

            if (pdfFile.getBytes().length > 0) {
            FileUpload pdf = imageService.uploadImage(pdfFile, "/pdf/");
            carComplect.setPdfFile("/pdf/" + pdf.getFile().getName());
            }

            //carComplect.setComplectParams(complectParamList);
            Set<ComplectParamDetail> complectParamDetails = new HashSet<>(Arrays.asList(
                    new ComplectParamDetail("Horse Power", "ცხენის ძალა",
                            String.valueOf(engine_.getHp()), String.valueOf(engine_.getHp())),
                    new ComplectParamDetail("Fuel Consumption (100 km)", "საწვავის წვა (100 კმ)",
                            fuelUsage.getHundred(), fuelUsage.getHundred()),
                    new ComplectParamDetail("Fuel Consumption (city)", "საწვავის წვა (ქალაქში)",
                            fuelUsage.getCity(), fuelUsage.getCity()),
                    new ComplectParamDetail("Fuel Consumption (highway)", "საწვავის წვა (ქალაქგარეთ)",
                            fuelUsage.getOutCity(), fuelUsage.getOutCity())
            ));
            carComplect.getComplectParams().add(new ComplectParam("engine", "ძრავი", complectParamDetails));
            CarComplect savedComp = carComplectRepo.save(carComplect);
            compInt.get().setCarComplect(savedComp);
            complectInterierRepo.save(compInt.get());

            String compName = savedComp.getName();
            if (lang.equals("ka")) {
                compName = savedComp.getNameGEO();
            }
            EngineWithComplectWeb comp = new EngineWithComplectWeb(
                            engineid,
                            compName,
                            engine_.getTitle(),
                            String.valueOf(engine_.getPrice()),
                            savedComp.getId());
            return ResponseEntity.ok(comp);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("ამ მოთხოვნებიდან არ შესრულდა ერთ-ერთი: " +
                        "ძრავი ვერ მოიძებნა, " +
                        "ამ ძრავზე უკვე არსებობს კომპლექტი, " +
                        "რომელიმე Photo360 ან CarTire უკვე მიბმულია კომპლექტზე " +
                        "ComplectInterier ვერ მოიძებნა ან უკვე მასზე არსებობს ჩანაწერი");
    }


    //@PostMapping(path = "/admin/car/complect/update/{engineid}/{complectid}/{complectinterierid}", produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> complectUpdate(
            @PathVariable @ApiParam(value = "ერთ კომპლექტზე ერთი ძრავი, წინააღმდეგ შემთხვევაში ოპერაცია არ შესრულდება",
                    required = true) Long engineid,
            @PathVariable("complectinterierid") @ApiParam(value = "მისამაგრებელი ComplectInterier- ის აიდი",
                    required = true) Long complectinterierid,
            @PathVariable Long complectid,
            @RequestParam("photoidlist") @ApiParam(value = "მისამაგრებელი Photo360-ების აიდების ლისტი",
                    required = true) ArrayList<Long> photoidlist,
            @RequestParam("cartirelist") @ApiParam(value = "მისამაგრებელი CarTire-ების აიდების ლისტი",
                    required = true) ArrayList<Long> cartirelist,
//            @RequestParam("complectparamlist") @ApiParam(value = "მისამაგრებელი ComplectParam-ების აიდების ლისტი",
//                    required = true) ArrayList<Long> complectparam,
            @RequestParam String name,
            @RequestParam String nameGEO,
            @RequestParam(required = false) MultipartFile pdfFile,
            @RequestHeader(value = "accepct-language", defaultValue = "en") String lang

    ) throws IOException {
        //Set<ComplectParam> complectParamList = complectParamRepo.findAllByIdList(complectparam);
        Optional<CarComplect> oldComplect = carComplectRepo.findById(complectid);
        Optional<Engine> engine = engineRepo.findById(engineid);
        List<Photo360> photos = photo360Repo.findAllByIdList(photoidlist);
        Set<CarTire> tires = carTireRepo.findAllByIdList(cartirelist);
        Optional<ComplectInterier> compIntNew = complectInterierRepo.findWhereComplectInterierIsEmpty(complectinterierid);
        Optional<ComplectInterier> compInt = complectInterierRepo.findById(complectinterierid);
        if (oldComplect.isPresent()) {
            boolean check = engine.isPresent() && photos.size() == photoidlist.size() && tires.size() == cartirelist.size()
                    && compInt.isPresent() ;
                    //&& complectparam.size() == complectParamList.size();
            if (!oldComplect.get().getEngine().getId().equals(engineid) && check) {
                check = carComplectRepo.findByEngineId(engineid).isEmpty();
            }

            if (check) {
                if (complectInterierRepo.findByComplectAndInterierId(complectinterierid, oldComplect.get().getId()).isEmpty() && compIntNew.isPresent()) {
                    compInt.get().setCarComplect(oldComplect.get());
                    complectInterierRepo.save(compInt.get());
                }

                oldComplect.get().setName(name);
                oldComplect.get().setNameGEO(nameGEO);
                //oldComplect.get().setComplectParams(complectParamList);
                oldComplect.get().setEngine(engine.get());
                oldComplect.get().setPhoto360(photos);
                oldComplect.get().setCarTires(tires);
                if (pdfFile != null && pdfFile.getBytes().length > 0) {
                    FileUpload pdf = imageService.uploadImage(pdfFile, "/pdf/");
                    oldComplect.get().setPdfFile("/pdf/" + pdf.getFile().getName());
                }

                String compName = oldComplect.get().getName();
                if (lang.equals("ka")) {
                    compName = oldComplect.get().getNameGEO();
                }
                EngineWithComplectWeb comp = new EngineWithComplectWeb(
                        engineid,
                        compName,
                        oldComplect.get().getEngine().getTitle(),
                        String.valueOf(oldComplect.get().getEngine().getPrice()),
                        oldComplect.get().getId());


                
                return ResponseEntity.ok(comp);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("ამ მოთხოვნებიდან არ შესრულდა ერთ-ერთი: " +
                        "ძრავი ვერ მოიძებნა, " +
                        "თუ ხდება ძრავის შეცვლა მაშინ ამ ძრავზე უკვე არსებობს კომპლექტი, " +
                        "რომელიმე Photo360 ან CarTire არ მოიძებნა ბაზაში" +
                        "CompInterier ვერ მოიძებნა");
    }


    @PostMapping(path = "/admin/car/complect/delete/{complectid}", produces = "application/json;**charset=UTF-8**")
    @Transactional
    public ResponseEntity<String> complectDelete(@PathVariable Long complectid) {
        Optional<CarComplect> oldComplect = carComplectRepo.findById(complectid);
        if (oldComplect.isPresent()) {

           // oldComplect.get().getCarTires().forEach(carTire -> carTireRepo.deleteById(carTire.getId()));
            //oldComplect.get().getPhoto360().forEach(photo360 -> photo360Repo.deleteById(photo360.getId()));


            Optional<ComplectInterier> interier = complectInterierRepo.findByComplectId(oldComplect.get().getId());
            interier.ifPresent(complectInterier -> complectInterierRepo.deleteById(complectInterier.getId()));
            imageService.deleteFile(oldComplect.get().getPdfFile());
            carComplectRepo.delete(oldComplect.get());


            return ResponseEntity.ok().body("წარმატებით წაიშალა");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი კომპლექტაცია ვერ მოიძებნა");
    }


}
