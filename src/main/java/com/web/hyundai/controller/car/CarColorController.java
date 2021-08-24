package com.web.hyundai.controller.car;

import com.web.hyundai.model.car.CarColor;
import com.web.hyundai.model.car.Photo360;
import com.web.hyundai.repo.car.CarColorRepo;
import com.web.hyundai.repo.car.Photo360Repo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = "Car Color")
@CrossOrigin("*")
public class CarColorController {


    @Autowired
    private CarColorRepo carColorRepo;
    @Autowired
    private Photo360Repo photo360Repo;


    @GetMapping("/api/car/colors/findall")
    public List<CarColor> getAllCarColors() {
        return carColorRepo.findAll();
    }

    @PostMapping("/admin/car/colors/create")
    public CarColor createCarColor(@RequestParam String colorName,
                                   @RequestParam String colorCode) {
        return carColorRepo.save(new CarColor(colorName, colorCode));
    }


    @PostMapping(path="/admin/car/colors/update/{colorid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> updateCarColor(
            @PathVariable Long colorid,
            @RequestParam String colorName,
            @RequestParam String colorCode) {

        Optional<CarColor> color = carColorRepo.findById(colorid);
        if (color.isPresent()) {
            color.get().setColorName(colorName);
            color.get().setColorCode(colorCode);
            return ResponseEntity.ok().body(carColorRepo.save(color.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ფერი ვერ მოიძებნა");
    }


    @PostMapping(path="/admin/car/colors/delete/{colorid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> updateCarColor(
            @PathVariable @ApiParam(value = "თუ არცერთ ფოტოს არ ეკუთვნის ეს ფერი მხოლოდ იმ შემთხვევაში წაიშლება",
                    required = true) Long colorid) {

        Optional<CarColor> color = carColorRepo.findById(colorid);
        List<Photo360> photo360List = photo360Repo.findAllByColorId(colorid);
        if (color.isPresent() && photo360List.size() < 1) {
            carColorRepo.deleteById(colorid);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ამ ფერზე ფოტოები ან თვითონ ფერი ვერ მოიძებნა");
    }
}
