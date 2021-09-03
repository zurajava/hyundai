package com.web.hyundai.controller.carservices;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.web.hyundai.model.FileUpload;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.carservices.CarService;
import com.web.hyundai.model.carservices.CarServiceWeb;
import com.web.hyundai.repo.carservice.CarServiceRepo;
import com.web.hyundai.service.ImageService;
import com.web.hyundai.service.car.CarBuildService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Api(tags = "CarServices")
@CrossOrigin("*")
public class CarServiceController {

  private final String JSON_FILTER_NAME = "serviceFilter";
  private final Set<String> EXCLUDED_FIELDS = Stream.of("titleGEO", "textGEO", "nameGEO")
      .collect(Collectors.toSet());


  @Autowired
  private ImageService imageService;

  @Autowired
  private CarServiceRepo carServiceRepo;

  @Autowired
  private CarBuildService carBuildService;


  @GetMapping(path = "/api/services/title/get/{serviceid}", produces = {
      "application/json;**charset=UTF-8**"})
  public ResponseEntity<String> getServiceTitles(
      @PathVariable Long serviceid,
      @RequestHeader(value = "accept-language", defaultValue = "en") String lang
  ) throws JsonProcessingException {

    Optional<CarService> service = carServiceRepo.findById(serviceid);
    if (service.isPresent()) {
      String title = service.get().getTitle();
        if (lang.equals("ka")) {
            title = service.get().getTitleGEO();
        }
      return ResponseEntity.ok(carBuildService.jsonExcludeFilter
          (JSON_FILTER_NAME, EXCLUDED_FIELDS, new CarServiceWeb(service.get().getId(), title)));

    }

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი სერვისი არ არსებობს");

  }


  @GetMapping(path = "/api/services/findall", produces = {"application/json;**charset=UTF-8**"})
  public ResponseEntity<String> getServices(
      @RequestHeader(value = "accept-language", defaultValue = "en") String lang)
      throws JsonProcessingException {
    List<CarService> services = carServiceRepo.findAll();
    if (lang.equals("ka")) {
      services.forEach(carService -> {
        carService.setText(carService.getTextGEO());
        carService.setTitle(carService.getTitleGEO());
        carService.getCarServiceDetailsList().forEach(carServiceDetails -> {
          carServiceDetails.setName(carServiceDetails.getNameGEO());
        });
      });
    }
    return ResponseEntity.ok(
        carBuildService.jsonExcludeFilter(JSON_FILTER_NAME, EXCLUDED_FIELDS, services));
  }


  @GetMapping(path = "/admin/services/findall", produces = {"application/json;**charset=UTF-8**"})
  public ResponseEntity<String> getServicesAdmin() throws JsonProcessingException {
    List<CarService> services = carServiceRepo.findAll();
    return ResponseEntity.ok(carBuildService.jsonFilter(JSON_FILTER_NAME, services));
  }


  @GetMapping(path = "/api/services/titles", produces = {"application/json;**charset=UTF-8**"})
  public ResponseEntity<String> getServiceTitle(
      @RequestHeader(value = "accept-language", defaultValue = "en") String lang
  ) throws JsonProcessingException {

    List<CarService> services = carServiceRepo.findAll();
    List<CarServiceWeb> serviceTitle = new ArrayList<>();

    services.forEach(carService -> {
      String title = carService.getTitle();
        if (lang.equals("ka")) {
            title = carService.getTitleGEO();
        }
      serviceTitle.add(new CarServiceWeb(carService.getId(), title));
    });

    return ResponseEntity.ok(
        carBuildService.jsonExcludeFilter(JSON_FILTER_NAME, EXCLUDED_FIELDS, serviceTitle));
  }


  @PostMapping(path = "/admin/services/create", produces = "application/json;**charset=UTF-8**")
  public ResponseEntity<?> carServiceCreate(
      @RequestParam("logo") MultipartFile logo,
      @RequestParam() String title,
      @RequestParam() String titleGEO,
      @RequestParam() String text,
      @RequestParam() String textGEO
  ) throws IOException {
    CarService carService = new CarService(title, titleGEO, text, textGEO);

    if (logo.getSize() > 1) {

      FileUpload img1 = imageService.uploadImage(logo, Path.CAR_PATH_SERVICE);
      carService.setLogo(Path.CAR_PATH_SERVICE + img1.getFile().getName());

      return ResponseEntity.ok(
          carBuildService.jsonFilter(JSON_FILTER_NAME, carServiceRepo.save(carService)));
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("სურათის ველი ცარიელია ან მეტადატა არ მოყვება");
  }


  @PostMapping(path = "/admin/services/update/{serviceid}", produces = "application/json;**charset=UTF-8**")
  public ResponseEntity<?> carServiceUpdate(
      @PathVariable Long serviceid,
      @RequestParam(value = "logo", required = false) MultipartFile logo,
      @RequestParam() String title,
      @RequestParam() String titleGEO,
      @RequestParam() String text,
      @RequestParam() String textGEO
  ) throws IOException {
    Optional<CarService> carService = carServiceRepo.findById(serviceid);

    if (carService.isPresent()) {

      carService.get().setTitle(title);
      carService.get().setTitleGEO(titleGEO);
      carService.get().setText(text);
      carService.get().setTextGEO(textGEO);

      if (null != logo && logo.getSize() > 1) {

        FileUpload img1 = imageService.uploadImage(logo, Path.CAR_PATH_SERVICE);

        File file = new File(Path.folderPath() + carService.get().getLogo());
          if (file.exists()) {
              file.delete();
          }
        carService.get().setLogo(Path.CAR_PATH_SERVICE + img1.getFile().getName());


      }

      return ResponseEntity.ok(
          carBuildService.jsonFilter(JSON_FILTER_NAME, carServiceRepo.save(carService.get())));
    }

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი სერვისი არ არსებობს");
  }


  @PostMapping(path = "/admin/services/delete/{serviceid}", produces = "application/json;**charset=UTF-8**")
  public ResponseEntity<String> carServiceDelete(@PathVariable() Long serviceid) {

    Optional<CarService> service = carServiceRepo.findById(serviceid);
    if (service.isPresent()) {
      carServiceRepo.delete(service.get());
      return ResponseEntity.ok("წარმატებით წაიშალა");
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი სერვისი არ არსებობს");

  }


}