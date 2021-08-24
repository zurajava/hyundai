package com.web.hyundai.controller.car;




import com.web.hyundai.exception.BreakException;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.car.*;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.car.Photo360IntRepo;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Api(tags = "Car Photo 360 Interier")
@CrossOrigin("*")
public class Photo360IntController {


    @Autowired
    private CarRepo carRepo;

    @Autowired
    private Photo360IntRepo photo360IntRepo;

    @Autowired
    private ImageService imageService;





    @GetMapping(path="/admin/car/photo360int/findall/{carid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> photo360FindAll(@PathVariable Long carid) {
        Optional<Photo360Int> photo360 = photo360IntRepo.findByCarId(carid);
        if (photo360.isPresent())
            return ResponseEntity.ok().body(photo360.get());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ამ მანქანაზე ვერ მოიძებნა Photo360Int");
    }


    @PostMapping(path = "/admin/car/photo360int/create/{carid}", consumes = "multipart/form-data",produces = {"application/json;**charset=UTF-8**"})
    @ApiOperation("თუ ამ მანქანაზე არსებობს 360Photo ოპერაცია არ შესრულდება")
    public ResponseEntity<?> photo360Create(
            @PathVariable()
            @ApiParam(value = "თუ ამ აიდიზე არ არსებობს მანქანა ოპერაცია არ შესრულდება", required = true)
                    Long carid,
            @ApiParam(value = "სვაგერ 2-ს არაქვს მხარდაჭერა ფაილების ლისტის გასაგზავნად, მივა ცარიელი ან ერორი", required = true)
            @RequestParam("photos") ArrayList<MultipartFile> photos
    ) {


        Optional<Car> car = carRepo.findById(carid);
        Optional<Photo360Int> Photo360IntCheck = photo360IntRepo.findByCarId(carid);
        if (car.isPresent() && Photo360IntCheck.isEmpty()) {

            List<Photo360IntList> photo360List = photos.stream().map(file -> {
                Photo360IntList photo360List2 = new Photo360IntList();
                String folder = Path.getPhoto360Int(car.get().getModel().toLowerCase().trim());
                try {
                    String name = imageService.uploadNewDir(file, folder);
                    photo360List2.setPhoto(name);
                    return photo360List2;
                } catch (IOException e) {
                    throw new BreakException(e.getMessage());
                }
            }).collect(Collectors.toList());

            Photo360Int photo360 = new Photo360Int();
            photo360.setCar(car.get());
            photo360.setPhoto360IntList(photo360List);

            return ResponseEntity.ok().body(photo360IntRepo.save(photo360));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("ამ მანქანაზე უკვე არსებობს Photo360Int ან ასეთი მანქანა არ მოიძებნა");
    }

    @PostMapping(path = "/admin/car/photo360int/update/{photo360id}", consumes = "multipart/form-data",produces = {"application/json;**charset=UTF-8**"})
    @ApiOperation("თუ ამ მანქანაზე და ფერზე არსებობს 360Photo ოპერაცია არ შესრულდება")
    public ResponseEntity<?> photo360Update(
            @PathVariable()
            @ApiParam(value = "თუ ამ აიდიზე არ არსებობს მანქანა ოპერაცია არ შესრულდება", required = true)
                    Long photo360id,
            @ApiParam(value = "სვაგერ 2-ს არაქვს მხარდაჭერა ფაილების ლისტის გასაგზავნად, მივა ცარიელი ან ერორი", required = true)
            @RequestParam("photos") ArrayList<MultipartFile> photos
    ) {

        Optional<Photo360Int> photo360 = photo360IntRepo.findById(photo360id);
        if (photo360.isPresent()) {


            List<Photo360IntList> photo360List = photos.stream().map(file -> {
                Photo360IntList photo360List2 = new Photo360IntList();
                String folder = Path.getPhoto360Int(photo360.get().getCar().getModel().toLowerCase().trim());
                try {
                    String name = imageService.uploadNewDir(file, folder);
                    photo360List2.setPhoto(name);
                    photo360.get().getPhoto360IntList().forEach(photo360IntList -> {
                        imageService.deleteFile(photo360IntList.getPhoto());
                    });
                    return photo360List2;
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new BreakException(e.getMessage());
                }
            }).collect(Collectors.toList());


            photo360.get().setPhoto360IntList(photo360List);

            return ResponseEntity.ok().body(photo360IntRepo.save(photo360.get()));

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("ასეთი Photo360Int არ არსებობს");
    }


    @PostMapping(value = "/admin/car/photo360int/delete/{photo360id}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> photo360Delete(@PathVariable Long photo360id) {

        Optional<Photo360Int> photo360 = photo360IntRepo.findById(photo360id);
        if (photo360.isPresent()) {
            photo360.get().getPhoto360IntList().forEach(p -> {
                imageService.deleteFile(p.getPhoto());
            });
            photo360IntRepo.deleteById(photo360.get().getId());
            return ResponseEntity.ok("წარმატებით წაიშალა");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი  Photo360Int არ მოიძებნა");

    }
}

