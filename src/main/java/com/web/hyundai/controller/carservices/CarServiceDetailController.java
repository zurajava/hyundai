package com.web.hyundai.controller.carservices;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.web.hyundai.model.carservices.CarService;
import com.web.hyundai.model.carservices.CarServiceDetails;
import com.web.hyundai.repo.carservice.CarServiceDetailRepo;
import com.web.hyundai.repo.carservice.CarServiceRepo;
import com.web.hyundai.service.car.CarBuildService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Api(tags = "CarServiceDetail")
@CrossOrigin("*")
public class CarServiceDetailController {

    @Autowired
    private CarServiceDetailRepo carServiceDetailRepo;

    @Autowired
    private CarServiceRepo carServiceRepo;

    @Autowired
    private CarBuildService carBuildService;

    private final String JSON_FILTER_NAME = "serviceFilter";


    @PostMapping(path = "/admin/servicedetail/create/{serviceid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> serviceDetailCreate(
            @PathVariable() Long serviceid,
            @RequestParam String name,
            @RequestParam String nameGEO
    ) throws JsonProcessingException {

        Optional<CarService> service = carServiceRepo.findById(serviceid);
        if (service.isPresent()) {
            service.get().getCarServiceDetailsList().add(new CarServiceDetails(name, nameGEO));
            return ResponseEntity.ok(carBuildService.jsonFilter(JSON_FILTER_NAME,carServiceRepo.save(service.get())));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი სერვისი ვერ მოიძებნა");

    }

    @PostMapping(path = "/admin/servicedetail/update/{detailid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> serviceDetailUpdate(
            @PathVariable() Long detailid,
            @RequestParam String name,
            @RequestParam String nameGEO
    ) throws JsonProcessingException {

        Optional<CarServiceDetails> details = carServiceDetailRepo.findById(detailid);
        if (details.isPresent()) {
            details.get().setName(name);
            details.get().setNameGEO(nameGEO);

            return ResponseEntity.ok(carBuildService.jsonFilter(JSON_FILTER_NAME,carServiceDetailRepo.save(details.get())));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი detail ვერ მოიძებნა");

    }

    @PostMapping(path = "/admin/servicedetail/delete/{detailid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> serviceDetailDelete(@PathVariable() Long detailid) {

        Optional<CarServiceDetails> details = carServiceDetailRepo.findById(detailid);
        if (details.isPresent()) {
            carServiceDetailRepo.delete(details.get());
            return ResponseEntity.ok("წარმატებით წაიშალა");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი detail ვერ მოიძებნა");

    }

}
