package com.web.hyundai.controller.usedcars;


import com.web.hyundai.model.usedcars.UsedCar;
import com.web.hyundai.model.usedcars.UsedCarModel;
import com.web.hyundai.repo.usedcars.UsedCarModelRepo;
import com.web.hyundai.repo.usedcars.UsedCarRepo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = "UsedCar Models")
@CrossOrigin("*")
public class UsedCarModelController {
    @Autowired
    UsedCarModelRepo usedCarModelRepo;

    @Autowired
    UsedCarRepo usedCarRepo;

    @GetMapping("/api/usedcars/model/findall")
    public List<UsedCarModel> findAllUsedCarModels() {
        return usedCarModelRepo.findAll();
    }

    @PostMapping(path = "/admin/usedcars/model/create",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> usedCarModelCreate(
            @RequestParam
            @ApiParam(value = "თუ ასეთი მოდელი არსებობს, ოპერაცია არ შესრულდება", required = true)
                    String model) {
        List<UsedCarModel> models = usedCarModelRepo.findAllByModelName(model.toLowerCase());
        if (models.size() < 1)
            return ResponseEntity.ok(usedCarModelRepo.save(new UsedCarModel(model.toLowerCase())));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი მოდელი არ მოიძებნა");
    }

    @PostMapping(path = "/admin/usedcars/model/update/{modelid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> usedCarModelUpdate(
            @PathVariable Long modelid,
            @ApiParam(value = "თუ ასეთი მოდელი არსებობს, ოპერაცია არ შესრულდება", required = true)
            @RequestParam String model) {
        List<UsedCarModel> models = usedCarModelRepo.findAllByModelName(model.toLowerCase());
        Optional<UsedCarModel> oldModel = usedCarModelRepo.findById(modelid);
        if (models.size() < 1 && oldModel.isPresent()) {
            oldModel.get().setModelName(model.toLowerCase());
            return ResponseEntity.ok(usedCarModelRepo.save(oldModel.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი მოდელი არ მოიძებნა");
    }


    @PostMapping(path = "/admin/usedcars/model/remove/{modelid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> usedCarModelRemove(
            @PathVariable
            @ApiParam(value = "თუ მანქანა არსებობს ამ მოდელით, ოპერაცია არ შესრულდება", required = true) Long modelid) {

        List<UsedCar> cars = usedCarRepo.findallByModelId(modelid);
        Optional<UsedCarModel> oldModel = usedCarModelRepo.findById(modelid);

        if (cars.size() < 1 && oldModel.isPresent()) {
            usedCarModelRepo.deleteById(modelid);
            return ResponseEntity.ok("წარმატებით წაიშალა");
        }

        return ResponseEntity.badRequest().body("მანქანა უკვა არსებობს ასეთ მოდელზე ან მოდელი არ მოიძებნა");
    }


}
