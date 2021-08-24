package com.web.hyundai.controller.car.complect;

import com.web.hyundai.model.car.modif.CarComplect;
import com.web.hyundai.model.car.modif.ComplectParam;
import com.web.hyundai.model.car.modif.ComplectParamDetail;
import com.web.hyundai.repo.car.modif.CarComplectRepo;
import com.web.hyundai.repo.car.modif.ComplectParamDetailRepo;
import com.web.hyundai.repo.car.modif.ComplectParamRepo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Api(tags = "Complect Params")
@CrossOrigin("*")
public class ComplectParamController {

    @Autowired
    private ComplectParamRepo complectParamRepo;

    @Autowired
    private ComplectParamDetailRepo complectParamDetailRepo;

    @Autowired
    CarComplectRepo carComplectRepo;


    @GetMapping(path = "/admin/complect/param/get/{complectid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> getComplectParamByComplectId(@PathVariable Long complectid){

        Optional<CarComplect> complect = carComplectRepo.findById(complectid);
        if (complect.isPresent()) {
            return ResponseEntity.ok(complectParamRepo.findAllByComplectId(complectid));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი კომპლექტაცია ვერ მოიძებნა");
    }


    @PostMapping(path = "/admin/complect/param/create/{complectid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> createComplectParam(@PathVariable Long complectid,
                                                             @RequestParam String paramname,
                                                             @RequestParam String paramnameGEO){

        Optional<CarComplect> complect = carComplectRepo.findById(complectid);
        if (complect.isPresent()) {
            ComplectParam complectParam = complectParamRepo.save(new ComplectParam(paramname, paramnameGEO));
            complect.get().getComplectParams().add(complectParam);
            carComplectRepo.save(complect.get());
            return ResponseEntity.ok(complectParam);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი კომპლექტაცია ვერ მოიძებნა");
    }

    @PostMapping(path = "/admin/complect/param/update/{complectparamid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> updateComplectParam(@PathVariable Long complectparamid,
                                                 @RequestParam String paramname,
                                                 @RequestParam String paramnameGEO){

        Optional<ComplectParam> complectParam = complectParamRepo.findById(complectparamid);
        if (complectParam.isPresent()) {
            complectParam.get().setParamName(paramname);
            complectParam.get().setParamNameGEO(paramnameGEO);
            return ResponseEntity.ok(complectParamRepo.save(complectParam.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი complect_param ვერ მოიძებნა");
    }

    @PostMapping(path = "/admin/complect/param/delete/{complectparamid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> deleteComplectParam(@PathVariable Long complectparamid){

        Optional<ComplectParam> complectParam = complectParamRepo.findById(complectparamid);
        if (complectParam.isPresent()) {
            complectParamRepo.deleteById(complectparamid);
            return ResponseEntity.ok(complectParam.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი complect_param_detail ვერ მოიძებნა");
    }

    @GetMapping(path = "/admin/complect/param-detail/get/{complectparamid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> getComplectParamDetailByParamId(@PathVariable Long complectparamid){

        Optional<ComplectParam> complectParam = complectParamRepo.findById(complectparamid);
        if (complectParam.isPresent()) {
            return ResponseEntity.ok(complectParamDetailRepo.findAllByParamId(complectparamid));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი კომპლექტაცია ვერ მოიძებნა");
    }

    @PostMapping(path = "/admin/complect/param-detail/create/{complectparamid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> createComplectParamDetail(@PathVariable Long complectparamid,
                                                       @RequestParam String field,
                                                       @RequestParam String fieldGEO,
                                                       @RequestParam String value,
                                                       @RequestParam String valueGEO){

        Optional<ComplectParam> complectParam = complectParamRepo.findById(complectparamid);
        if (complectParam.isPresent()) {
            ComplectParamDetail complectParamDetail = complectParamDetailRepo
                    .save(new ComplectParamDetail(field, fieldGEO, value, valueGEO));
            complectParam.get().getComplectParamDetails().add(complectParamDetail);
            complectParamRepo.save(complectParam.get());
            return ResponseEntity.ok(complectParamDetail);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი complect_param_detail ვერ მოიძებნა");
    }

    @PostMapping(path = "/admin/complect/param-detail/update/{detailid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> updateComplectParamDetail(@PathVariable Long detailid,
                                                       @RequestParam String field,
                                                       @RequestParam String fieldGEO,
                                                       @RequestParam String value,
                                                       @RequestParam String valueGEO){

        Optional<ComplectParamDetail> complectParamDetail = complectParamDetailRepo.findById(detailid);
        if (complectParamDetail.isPresent()) {
            complectParamDetail.get().setValue(value);
            complectParamDetail.get().setValueGEO(valueGEO);
            complectParamDetail.get().setField(field);
            complectParamDetail.get().setFieldGEO(fieldGEO);
            return ResponseEntity.ok(complectParamDetail.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი complect_param_detail ვერ მოიძებნა");
    }

    @PostMapping(path = "/admin/complect/param-detail/delete/{detailid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> deleteComplectParamDetail(@PathVariable Long detailid){

        Optional<ComplectParamDetail> complectParamDetail = complectParamDetailRepo.findById(detailid);
        if (complectParamDetail.isPresent()) {
            complectParamDetailRepo.deleteById(detailid);
            return ResponseEntity.ok(complectParamDetail.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი complect_param_detail ვერ მოიძებნა");
    }




}
