package com.web.hyundai.controller.home;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.web.hyundai.model.FileUpload;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.car.web.HomeCarWeb;
import com.web.hyundai.model.home.CarSizeList;
import com.web.hyundai.model.home.Home;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.home.HomeRepo;
import com.web.hyundai.repo.news.NewsRepo;
import com.web.hyundai.service.ImageService;
import com.web.hyundai.service.car.CarBuildService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "Home Slider")
@CrossOrigin("*")

public class HomeController {


  private final String JSON_FILTER_NAME = "homeFilter";
  private final Set<String> EXCLUDED_FIELDS = Stream.of("titleGEO").collect(Collectors.toSet());


  @Autowired
  HomeRepo homeRepo;

  @Autowired
  NewsRepo newsRepo;

  @Autowired
  ImageService imageService;

  @Autowired
  CarRepo carRepo;

  @Autowired
  CarBuildService carBuildService;


  //ka movides tu qartulad unda
  // config an home
  @GetMapping("/api/home/getcars")
  public List<HomeCarWeb> getAllCars(@RequestHeader(value = "accept-language", defaultValue = "en")
  @ApiParam(value = "ka მოვიდეს თუ ქართულია დეფაულტად არის en")
      String lang,
      @RequestParam(value = "key", defaultValue = "home") String key) {
    return carBuildService.getCars(lang, key);
  }

  @GetMapping("/api/home/getcarsbymodel")
  public List<HomeCarWeb> getCarsByModel(
      @RequestHeader(value = "accept-language", defaultValue = "en")
      @ApiParam(value = "ka მოვიდეს თუ ქართულია დეფაულტად არის en")
          String lang,
      @RequestParam String model) {

    return carBuildService.getCarsByModel(lang, model);
  }

  @GetMapping("/api/home/getcarsize")
  public List<CarSizeList> getCarSize() {
    return carBuildService.getSizeList();
  }


  @GetMapping("/api/home/search")
  public ResponseEntity<List<Car>> searchCars(
      @RequestParam("search") @ApiParam(value = "ტესტირებისთვის დასერჩე: son", required = true) String search,
      @RequestHeader(value = "accept-language", defaultValue = "en") String lang) {

    List<Car> cars = carRepo.findByModelIsContainingIgnoreCase(search);
    if (lang.equals("ka")) {
      cars.forEach(car -> {
        car.setTitle(car.getTitleGEO());
      });
    }

    return ResponseEntity.ok(cars);
  }


  @GetMapping(path = "/api/home/get-sliders", produces = {"application/json;**charset=UTF-8**"})
  public ResponseEntity<String> getSliders(
      @RequestHeader(value = "accept-language", defaultValue = "en") String lang)
      throws JsonProcessingException {

    List<Home> sliders = homeRepo.findAllByOrderBySortAsc();
    if (lang.equals("ka")) {
      sliders.forEach(home -> {
        home.setTitle(home.getTitleGEO());
      });
    }
    return ResponseEntity.ok(
        carBuildService.jsonExcludeFilter(JSON_FILTER_NAME, EXCLUDED_FIELDS, sliders));

  }

  @GetMapping(path = "/admin/home/get-sliders", produces = {"application/json;**charset=UTF-8**"})
  public ResponseEntity<String> getSlidersAdmin() throws JsonProcessingException {

    List<Home> sliders = homeRepo.findAllByOrderBySortAsc();

    return ResponseEntity.ok(carBuildService.jsonFilter(JSON_FILTER_NAME, sliders));

  }

  @GetMapping(path = "/api/home/get-slider/{id}", produces = {"application/json;**charset=UTF-8**"})
  public ResponseEntity<String> getSlider(
      @PathVariable Long id,
      @RequestHeader(value = "accept-language", defaultValue = "en") String lang)
      throws JsonProcessingException {

    Optional<Home> slider = homeRepo.findById(id);
    if (slider.isPresent()) {

      if (lang.equals("ka")) {
        slider.get().setTitle(slider.get().getTitleGEO());
      }

      return ResponseEntity.ok(
          carBuildService.jsonExcludeFilter(JSON_FILTER_NAME, EXCLUDED_FIELDS, slider));
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("სლაიდერი ვერ მოიძებნა");
  }


  @PostMapping(path = "/admin/home/update-slider/{id}", produces = {
      "application/json;**charset=UTF-8**"})
  public ResponseEntity<String> updateSlider(@PathVariable
  @ApiParam(value = "სლაიდერის აიდი", required = true) Long id,
      @RequestParam(value = "title") String title,
      @RequestParam(value = "titleGEO") String titleGEO,
      @RequestParam(value = "sort")
      @ApiParam(value = "ციფრი სორტირებისთვის", required = true) Integer sort,
      @RequestParam(value = "link")
      @ApiParam(value = "ფეიჯის ლინკი", required = true) String link,
      @RequestParam(value = "image", required = false) MultipartFile file) throws IOException {

    Optional<Home> slider = homeRepo.findById(id);
    if (slider.isPresent()) {
      slider.get().setTitle(title);
      slider.get().setTitleGEO(titleGEO);
      slider.get().setSort(sort);
      slider.get().setLink(link);

      if (file != null && file.getSize() > 0) {
        File delFile = new File(Path.folderPath() + slider.get().getImage());
        if (delFile.exists() && !delFile.getName().equals("sample.png")) {
          delFile.delete();
        }
        FileUpload fileUpload = imageService.uploadImage(file, Path.home);
        slider.get()
            .setImage(imageService.removeDoubleSlash(Path.home + fileUpload.getFile().getName()));
      }
      homeRepo.save(slider.get());
      return ResponseEntity.ok(carBuildService.jsonFilter(JSON_FILTER_NAME, slider.get()));
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("სლაიდერი ვერ მოიძებნა");

  }

  @PostMapping(path = "/admin/home/create-slider", produces = {
      "application/json;**charset=UTF-8**"})
  public ResponseEntity<String> createSlider(
      @RequestParam(value = "title") String title,
      @RequestParam(value = "titleGEO") String titleGEO,
      @RequestParam(value = "sort")
      @ApiParam(value = "ციფრი სორტირებისთვის", required = true) Integer sort,
      @RequestParam(value = "link")
      @ApiParam(value = "ფეიჯის ლინკი", required = true) String link,
      @RequestParam(value = "image", required = false) MultipartFile file) throws IOException {

    Home slider = new Home();

    slider.setTitle(title);
    slider.setTitleGEO(titleGEO);
    slider.setSort(sort);
    slider.setLink(link);

    if (file != null && file.getSize() > 0) {
      FileUpload fileUpload = imageService.uploadImage(file, Path.home);
      slider.setImage(imageService.removeDoubleSlash(Path.home + fileUpload.getFile().getName()));
    }
    homeRepo.save(slider);
    return ResponseEntity.ok(carBuildService.jsonFilter(JSON_FILTER_NAME, slider));
  }


}
