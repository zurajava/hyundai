package com.web.hyundai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.web.hyundai.model.car.CarColor;
import com.web.hyundai.model.car.Photo360;
import com.web.hyundai.model.car.modif.CarComplect;
import com.web.hyundai.model.car.modif.CarTire;
import com.web.hyundai.model.car.modif.ComplectInterier;
import com.web.hyundai.model.car.modif.web.AfterComplectWeb;
import com.web.hyundai.model.car.modif.web.SingleComplectWeb;
import com.web.hyundai.repo.car.CarColorRepo;
import com.web.hyundai.repo.car.Photo360Repo;
import com.web.hyundai.repo.car.modif.CarComplectRepo;
import com.web.hyundai.repo.car.modif.CarTireRepo;
import com.web.hyundai.repo.car.modif.ComplectInterierRepo;
import com.web.hyundai.service.car.CarBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ComplectService {

    private final Set<String> EXCLUDED_FIELDS = Stream.of("featureGEO","carComplect").collect(Collectors.toSet());

    @Autowired
    private CarComplectRepo carComplectRepo;
    @Autowired
    private CarColorRepo carColorRepo;
    @Autowired
    private CarTireRepo carTireRepo;
    @Autowired
    private Photo360Repo photo360Repo;
    @Autowired
    private ComplectInterierRepo complectInterierRepo;
    @Autowired
    private CarBuildService carBuildService;

    //todo frontma gmamomayolos photo360
    public ResponseEntity<?> createAfterComplect(String lang, Long complectid, Long photo360id, Long tireid, ArrayList<Long> featureList) {
        Optional<CarComplect> complect = carComplectRepo.findById(complectid);
        Optional<Photo360> photo360 = photo360Repo.findById(photo360id);
        Optional<CarTire> tire = carTireRepo.findById(tireid);
        if (photo360.isPresent() && tire.isPresent() && complect.isPresent()) {
                log.debug(complect.get().getComplectParams().size() +" ++++++++++");
                complect.get().getComplectParams().forEach(complectParam -> {
                    log.debug(complectParam.getComplectParamDetails().size() +" ____");
                });

            AfterComplectWeb afterComplectWeb = new AfterComplectWeb();
            afterComplectWeb.setCarTire(tire.get());
            photo360.ifPresent(afterComplectWeb::setPhoto360);
            return complect.map(carComplect -> {

                afterComplectWeb.setComplectParams(carComplect.getComplectParams());
                afterComplectWeb.setEngineTitle(carComplect.getEngine().getTitle());
                afterComplectWeb.setPrice(String.valueOf(carComplect.getEngine().getPrice()));
                afterComplectWeb.setModel(carComplect.getEngine().getCar().getModel());
                afterComplectWeb.setComplectName(carComplect.getName());
                afterComplectWeb.setFeatureList(featureList);


                if (lang.equals("ka")) {
                    afterComplectWeb.setComplectName(carComplect.getNameGEO());
                    afterComplectWeb.getComplectParams().forEach(complectParam -> {
                        complectParam.setParamName(complectParam.getParamNameGEO());
                        complectParam.getComplectParamDetails().forEach(complectParamDetail -> {
                            complectParamDetail.setField(complectParamDetail.getFieldGEO());
                            complectParamDetail.setValue(complectParamDetail.getValueGEO());
                        });
                    });
                }

                return ResponseEntity.ok().body(afterComplectWeb);

            }).orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.badRequest().body("ასეთი ფერი, საბურავი ან კომპლექტი ვერ მოიძებნა");
    }


    public ResponseEntity<?> createSingleComplect(String lang, Long complectid) {

        Optional<CarComplect> complect = carComplectRepo.findById(complectid);
        SingleComplectWeb singleComplectWeb = new SingleComplectWeb();
        if (complect.isPresent()) {


            return complect.map(carComplect -> {

                complectInterierRepo.findByComplectId(complectid).ifPresent(singleComplectWeb::setComplectInterier);
                singleComplectWeb.setComplectid(carComplect.getId());
                singleComplectWeb.setEngineid(carComplect.getEngine().getId());
                singleComplectWeb.setPhoto360(photo360Repo.findAllByComplectId(carComplect.getId()));
                singleComplectWeb.setCarTires(carComplect.getCarTires());
                singleComplectWeb.setName(carComplect.getName());
                singleComplectWeb.setPdfFile(carComplect.getPdfFile());
                if (lang.equals("ka")) singleComplectWeb.setName(carComplect.getNameGEO());
                System.out.println(singleComplectWeb.getPhoto360().size() + " ++++++");

                if (lang.equals("ka")) {
                    singleComplectWeb.getComplectInterier().getComplectInterierFeatures().forEach(complectInterierFeature -> {
                        complectInterierFeature.setFeature(complectInterierFeature.getFeatureGEO());
                    });
                }
                try {
                    return ResponseEntity.ok().body(
                            carBuildService.jsonExcludeFilter("InterierFilter",EXCLUDED_FIELDS,singleComplectWeb));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("cant parse json " + e.getMessage() );
                }
            }).orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი კომპლექტი ვერ მოიძებნა");
    }


}
