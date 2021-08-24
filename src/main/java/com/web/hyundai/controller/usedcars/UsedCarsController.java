package com.web.hyundai.controller.usedcars;


import com.web.hyundai.model.usedcars.UsedCarModel;
import com.web.hyundai.repo.usedcars.UsedCarModelRepo;
import com.web.hyundai.repo.usedcars.spec.SpecBuilder;
import com.web.hyundai.model.translation.Translate;
import com.web.hyundai.model.usedcars.UsedCar;
import com.web.hyundai.model.usedcars.UsedCarWeb;
import com.web.hyundai.repo.translate.TranslateRepo;
import com.web.hyundai.repo.usedcars.UsedCarRepo;
import com.web.hyundai.service.usedcars.UsedCarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@RestController
@Api(tags = "UsedCars")
@CrossOrigin("*")

public class UsedCarsController {



    @Autowired
    private UsedCarRepo usedCarRepo;

    @Autowired
    private TranslateRepo translateRepo;

    @Autowired
    private UsedCarService usedCarService;

    @Autowired
    private UsedCarModelRepo usedCarModelRepo;

    @Autowired
    private SpecBuilder specBuilder;


    private final int PAGE_SIZE = 4;


    // unda movides ?page=0 parametrit  defaultad sorti aris desci asc miuweros.
    @GetMapping("/api/usedcars/findall")
    public ResponseEntity<List<UsedCarWeb>> getCarsList(
            @RequestParam(value = "page", defaultValue = "0")
            @ApiParam(value = "პაგინაცია ფეიჯის ნომერი საწყისი 0", required = true, defaultValue = "0") int page,

            @RequestHeader(value = "accept-language", defaultValue = "en")
            @ApiParam(value = "ka მოვიდეს თუ ქართულია დეფაულტად არის en", required = true) String lang,

            @RequestParam(required = false, value = "sort", defaultValue = "date-desc")
            @ApiParam(value = "დეფაულტად არის date-desc მოვიდეს [field-asc] " + "მაგალითად: date-asc, price-desc")
                    String sort) {

        Sort.Direction sortDir = Sort.Direction.DESC;

        if (sort.toLowerCase().split("-")[1].equals("asc")) {
            sortDir = Sort.Direction.ASC;
        }
        sort = sort.split("-")[0];

        Pageable firstPageWithTwoElements = PageRequest.of(page, PAGE_SIZE, Sort.by(sortDir, sort));

        List<Object[]> ff = usedCarRepo.findallCarWeb(firstPageWithTwoElements);

        List<UsedCarWeb> webObj = ff.stream().map(objects -> {

            Optional<UsedCarModel> model = usedCarModelRepo.findById(Long.valueOf(objects[1].toString()));
            BigInteger id = (BigInteger) objects[0];
            UsedCarWeb car = UsedCarWeb.builder().id(id.longValue()).price((Integer) objects[2])
                    .year((int) objects[3]).mileage((Integer) objects[4]).fuel((String) objects[5])
                    .extColor((String) objects[6]).displayPhoto((String) objects[7]).transmission((String) objects[8])
                    .intColor((String) objects[9])
                    .hp((double) objects[10])
                    .engine((String) objects[11])
                    .build();
            model.ifPresent(usedCarModel -> car.setModel(usedCarModel.getModelName()));
            return car;

        }).collect(Collectors.toList());


        return ResponseEntity.ok(usedCarService.translateAllCars(lang, webObj));
    }

    @GetMapping("/admin/usedcars/findall")
    public List<UsedCar> getCarsListAdmin(
            @RequestParam(value = "page", defaultValue = "0")
            @ApiParam(value = "პაგინაცია ფეიჯის ნომერი საწყისი 0", required = true, defaultValue = "0") int page
            ) {
        Pageable firstPageWithTwoElements = PageRequest.of(page, PAGE_SIZE);

        return usedCarRepo.findAll(firstPageWithTwoElements).getContent();
    }


    @GetMapping("/api/usedcars/filter")
    public List<UsedCarWeb> filterForm(
            @RequestParam(value = "modelid", required = false) Long modelid,
            @RequestParam(value = "price1", required = false) Integer price1,
            @RequestParam(value = "price2", required = false) Integer price2,
            //@RequestParam(value = "colors",required = false)ArrayList<String> colors,
            @RequestParam(value = "year1", required = false) Integer year1,
            @RequestParam(value = "year2", required = false) Integer year2,
            @RequestParam(value = "mileage1", required = false) Integer mileage1,
            @RequestParam(value = "mileage2", required = false) Integer mileage2,
            //@RequestParam(value = "door",required = false)String door,
            //@RequestParam(value = "type",required = false)String type,
            //@RequestParam(value = "fuel",required = false)String fuel,
            //@RequestParam(value = "tran",required = false)String tran,
            //sorting etc..
            @RequestParam(value = "page", defaultValue = "0")
            @ApiParam(value = "პაგინაცია ფეიჯის ნომერი საწყისი 0", required = true, defaultValue = "0")
                    int page,
            @RequestHeader(value = "accept-language", defaultValue = "en") String lang,

            @RequestParam(required = false, value = "sort", defaultValue = "date-desc")
            @ApiParam(value = "დეფაულტად არის date-desc მოვიდეს [field-asc] " +
                    "მაგალითად: date-asc, price-desc")
                    String sort) {


        Specification<UsedCar> speco = Specification.where(
                specBuilder.equalFilter("usedCarModel", modelid))
                .and(specBuilder.betweenFilter("price", price1, price2))
                .and(specBuilder.betweenFilter("year", year1, year2))
                .and(specBuilder.betweenFilter("mileage", mileage1, mileage2));
        //.and(SpecBuilder.equalFilter("door",door))
        //.and(SpecBuilder.equalFilter("type",type))
        //.and(SpecBuilder.equalFilter("fuel",fuel))
        //.and(SpecBuilder.equalFilter("transmission",tran))
        //.and(SpecBuilder.colorFilter(colors))


        Sort.Direction sortDir = Sort.Direction.DESC;

        if (sort.toLowerCase().split("-")[1].equals("asc")) {
            sortDir = Sort.Direction.ASC;
        }
        sort = sort.split("-")[0];

        Pageable firstPageWithTwoElements = PageRequest.of(page, PAGE_SIZE, Sort.by(sortDir, sort));


        List<UsedCarWeb> usedCarWebList = usedCarRepo.findAll(speco, firstPageWithTwoElements).stream()
                .map(usedCar -> UsedCarWeb.builder()
                        .extColor(usedCar.getExtColor())
                        .intColor(usedCar.getIntColor())
                        .engine(usedCar.getEngine())
                        .hp(usedCar.getHp())
                        .displayPhoto(usedCar.getDisplayPhoto())
                        .fuel(usedCar.getFuel())
                        .mileage(usedCar.getMileage())
                        .price(usedCar.getPrice())
                        .model(usedCar.getUsedCarModel().getModelName())
                        .id(usedCar.getId())
                        .year(usedCar.getYear())
                        .transmission(usedCar.getTransmission())
                        .build()).collect(Collectors.toList());


        return usedCarService.translateAllCars(lang, usedCarWebList);


    }


    @GetMapping("/api/usedcars/get/{id}")
    public ResponseEntity<UsedCar> getCar(@PathVariable Long id,
                                          @RequestHeader(value = "accept-language", defaultValue = "en") String lang) {

        AtomicInteger atomicInteger = new AtomicInteger(0);

        Optional<UsedCar> usedcar = usedCarRepo.findById(id);

        usedcar.ifPresent(car -> {
            if (lang.equals("ka")) {
                Optional<Translate> color = translateRepo.findById(car.getExtColor().toUpperCase());
                Optional<Translate> fuel = translateRepo.findById(car.getFuel().toUpperCase());
                Optional<Translate> trans = translateRepo.findById(car.getTransmission().toUpperCase());
//                Optional<Translate> type = translateRepo.findById(car.getDesignType().toUpperCase());


                color.ifPresent(translate -> car.setExtColor(translate.getValueGEO()));
                fuel.ifPresent(translate -> car.setFuel(translate.getValueGEO()));
                trans.ifPresent(translate -> car.setTransmission(translate.getValueGEO()));
//                type.ifPresent(translate -> car.setDesignType(translate.getValueGEO()));
            }

            atomicInteger.incrementAndGet();
        });

        if (atomicInteger.get() == 0) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UsedCar());


        return ResponseEntity.ok(usedcar.get());

    }

    // 1 is success
    // 0 is not successful
    @PostMapping("/admin/usedcars/remove/{id}")
    public ResponseEntity<String> removeCars(@PathVariable Long id) {

        return ResponseEntity.ok(usedCarService.delete(id));

    }


    @PostMapping(path = "/admin/usedcars/update/{id}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> updateCars(@PathVariable Long id,
                                              @RequestParam()
                                              @ApiParam(value = "ექსტერიერის ფერი", required = true) String extColor,
                                              @RequestParam() @ApiParam(value = "ინტერიერის ფერი", required = true) String intColor,
                                              @RequestParam() String engine,
                                              @ApiParam(value = "ცხენის ძალა", required = true)
                                                  @RequestParam() double hp,
                                              @RequestParam() Integer price,
                                              @RequestParam() String year,
                                              @RequestParam() Integer mileage,
                                              @RequestParam()
                                                  @ApiParam(value = "კარების რაოდენობა", required = true)
                                                          Integer door,
                                              @RequestParam()
                                                  @ApiParam(value = "მანქანის ტიპი: sedan,suv,hatchback,commercial", required = true)
                                                          String type,
                                              @RequestParam()
                                                  @ApiParam(value = "საწვავის ტიპი: petrol,diesel", required = true) String fuel,
                                              @RequestParam()
                                                  @ApiParam(value = "გადაცემათა კოლოფი: manual,automatic", required = true)
                                                          String tran,
                                              @RequestParam(value = "file",required = false)
                                                  @ApiParam(value = "გამოსაჩენი ფოტო") MultipartFile displayPhoto)
     {


        UsedCar car = new UsedCar();
        car.setYear(Integer.parseInt(year));
        car.setType(type);
        car.setPrice(price);
        car.setTransmission(tran);
        car.setFuel(fuel);
        car.setDoor(door);
        car.setExtColor(extColor);
        car.setMileage(mileage);
        car.setEngine(engine);
        car.setIntColor(intColor);
        car.setHp(hp);



        return usedCarService.carUpdate(id, car, displayPhoto);

    }



    //validation yvela veli required
    @PostMapping("/admin/usedcars/savecars")
    public ResponseEntity<UsedCar> createCar(
            @RequestParam()
            @ApiParam(value = "ექსტერიერის ფერი", required = true) String extColor,
            @RequestParam() @ApiParam(value = "ინტერიერის ფერი", required = true) String intColor,
            @RequestParam() String engine,
            @ApiParam(value = "ცხენის ძალა", required = true)
            @RequestParam() double hp,
            @RequestParam() Long modelid,
            @RequestParam() Integer price,
            @RequestParam() String year,
            @RequestParam() Integer mileage,
            @RequestParam()
            @ApiParam(value = "კარების რაოდენობა", required = true)
                    Integer door,
            @RequestParam()
            @ApiParam(value = "მანქანის ტიპი: sedan,suv,hatchback,commercial", required = true)
                    String type,
            @RequestParam()
            @ApiParam(value = "საწვავის ტიპი: petrol,diesel", required = true) String fuel,
            @RequestParam()
            @ApiParam(value = "გადაცემათა კოლოფი: manual,automatic", required = true)
                    String tran,
            @RequestParam("file")
            @ApiParam(value = "გამოსაჩენი ფოტო", required = true) MultipartFile displayFile,
            @RequestParam("files")
            @ApiParam(value = "სვაგერ 2-ს არაქვს მხარდაჭერა ფაილების ლისტის გასაგზავნად, მივა ცარიელი ან ერორი", required = true)
                    ArrayList<MultipartFile> files
            //@RequestParam("features") ArrayList<String> features,
            //@RequestParam("featuresGEO") ArrayList<String> featuresGEO


    ) throws IOException {


        return ResponseEntity.ok(usedCarService.create(extColor, intColor, hp, engine, modelid, price, year, mileage, door, type, fuel,
                tran, displayFile, files));
    }
}
