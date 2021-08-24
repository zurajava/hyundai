package com.web.hyundai.controller.car;


import com.web.hyundai.exception.BreakException;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.car.CarColor;
import com.web.hyundai.model.car.Photo360;
import com.web.hyundai.model.car.Photo360List;
import com.web.hyundai.repo.car.CarColorRepo;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.car.Photo360Repo;
import com.web.hyundai.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Api(tags = "Car Photo 360")
@CrossOrigin("*")
public class Photo360Controller {


    @Autowired
    private CarRepo carRepo;

    @Autowired
    private Photo360Repo photo360Repo;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CarColorRepo carColorRepo;

    // colorid=photo360id
    @GetMapping(path="/api/car/photo360/findbycolor/{carid}/{photo360id}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> photo360FindByColorConfig(@PathVariable Long carid, @PathVariable Long photo360id) {

        //Optional<Photo360> photo360 = photo360Repo.findByColorIdAndCarId(colorid, carid);
        Optional<Photo360> photo360 = photo360Repo.findById(photo360id);
        if (photo360.isPresent()) return ResponseEntity.ok(photo360);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი photo360 ვერ მოიძებნა");
    }

    @GetMapping(path="/api/car/photo360/findbycolordetail/{carid}/{colorid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> photo360FindByColorDetail(@PathVariable Long carid, @PathVariable Long colorid) {

        List<Photo360> photo360 = photo360Repo.findByColorIdAndCarId(colorid, carid);
        //Optional<Photo360> photo360 = photo360Repo.findById(photo360id);
        if (photo360.size() > 0) return ResponseEntity.ok(photo360.get(0));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი photo360 ვერ მოიძებნა");
    }


    @GetMapping("/admin/car/photo360/findall/{carid}")
    public List<Photo360> photo360FindAll(@PathVariable Long carid) {
        return photo360Repo.findAllByCarId(carid);
    }


    @PostMapping(path = "/admin/car/photo360/create/{carid}/{colorid}", consumes = "multipart/form-data",produces = {"application/json;**charset=UTF-8**"})
    @ApiOperation("თუ ამ მანქანაზე და ფერზე არსებობს 360Photo ოპერაცია არ შესრულდება")
    public ResponseEntity<?> photo360Create(
            @PathVariable()
            @ApiParam(value = "თუ ამ აიდიზე არ არსებობს მანქანა ოპერაცია არ შესრულდება", required = true)
                    Long carid,
            @PathVariable()
            @ApiParam(value = "თუ ამ აიდიზე არ არსებობს ფერი ოპერაცია არ შესრულდება", required = true)
                    Long colorid,
            @ApiParam(value = "სვაგერ 2-ს არაქვს მხარდაჭერა ფაილების ლისტის გასაგზავნად, მივა ცარიელი ან ერორი", required = true)
            @RequestParam("photos") ArrayList<MultipartFile> photos
    ) {


        Optional<Car> car = carRepo.findById(carid);
        Optional<CarColor> carColor = carColorRepo.findById(colorid);
        //Optional<Photo360> check360 = photo360Repo.findByColorIdAndCarId(colorid, carid);
        if (car.isPresent() && carColor.isPresent()) {

            List<Photo360List> photo360List = photos.stream().map(file -> {
                Photo360List photo360List2 = new Photo360List();
                String folder = Path.getPhoto360(car.get().getModel().toLowerCase().trim(),
                        carColor.get().getColorName().toLowerCase().trim());
                try {
                    String name = imageService.uploadNewDir(file, folder);
                    photo360List2.setPhoto(name);
                    return photo360List2;
                } catch (IOException e) {
                    throw new BreakException(e.getMessage());
                }
            }).collect(Collectors.toList());

            Photo360 photo360 = new Photo360();
            photo360.setCar(car.get());
            photo360.setCarColor(carColor.get());
            photo360.setPhoto360List(photo360List);

            return ResponseEntity.ok().body(photo360Repo.save(photo360));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("ამ ფერზე და მანქანაზე უკვე არსებობს Photo360 ან ასეთი მანქანა და ფერი არ მოიძებნა");
    }

    @PostMapping(path = "/admin/car/photo360/update/{photo360id}/{colorid}", consumes = "multipart/form-data",produces = {"application/json;**charset=UTF-8**"})
    @ApiOperation("თუ ამ მანქანაზე და ფერზე არსებობს 360Photo ოპერაცია არ შესრულდება")
    public ResponseEntity<?> photo360Update(
            @PathVariable()
            @ApiParam(value = "თუ ამ აიდიზე არ არსებობს მანქანა ოპერაცია არ შესრულდება", required = true)
                    Long photo360id,
            @PathVariable()
            @ApiParam(value = "თუ ამ აიდიზე არ არსებობს ფერი ოპერაცია არ შესრულდება", required = true)
                    Long colorid,
            @ApiParam(value = "სვაგერ 2-ს არაქვს მხარდაჭერა ფაილების ლისტის გასაგზავნად, მივა ცარიელი ან ერორი", required = true)
            @RequestParam("photos") ArrayList<MultipartFile> photos
    ) {

        boolean check360 = true;
        Optional<Photo360> photo360 = photo360Repo.findById(photo360id);
        Optional<CarColor> carColor = carColorRepo.findById(colorid);
        if (photo360.isPresent() && carColor.isPresent()) {

            //if(!photo360.get().getCarColor().getId().equals(colorid))
                //check360 = photo360Repo.findByColorIdAndCarId(colorid, photo360.get().getCar().getId()).isEmpty();

            if (photos.size() > 0) {

                List<Photo360List> photo360List = photos.stream().map(file -> {
                    Photo360List photo360List2 = new Photo360List();
                    String folder = Path.getPhoto360(photo360.get().getCar().getModel().toLowerCase().trim(),
                            carColor.get().getColorName().toLowerCase().trim());
                    try {
                        String name = imageService.uploadNewDir(file, folder);
                        photo360List2.setPhoto(name);
                        photo360.get().getPhoto360List().forEach(photo360IntList -> {
                            imageService.deleteFile(photo360IntList.getPhoto());
                        });
                        return photo360List2;
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new BreakException(e.getMessage());
                    }
                }).collect(Collectors.toList());
                photo360.get().setPhoto360List(photo360List);
            }

                photo360.get().setCarColor(carColor.get());

                return ResponseEntity.ok().body(photo360Repo.save(photo360.get()));

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("ამ ფერზე და მანქანაზე არსებობს Photo360 ან ასეთი photo360 და ფერი არ მოიძებნა");
    }


    @PostMapping(path = "/admin/car/photo360/delete/{photo360id}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> photo360Delete(@PathVariable Long photo360id) {

        Optional<Photo360> photo360 = photo360Repo.findById(photo360id);
        if (photo360.isPresent()) {
            photo360.get().getPhoto360List().forEach(p -> {
                imageService.deleteFile(p.getPhoto());
            });
            photo360Repo.deleteById(photo360.get().getId());
            return ResponseEntity.ok("წარმატებით წაიშალა");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი  Photo360 არ მოიძებნა");

    }
}
