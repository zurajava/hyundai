package com.web.hyundai.controller.car;


import com.web.hyundai.model.car.*;
import com.web.hyundai.model.car.modif.CarComplect;
import com.web.hyundai.model.car.web.EngineDescWeb;
import com.web.hyundai.model.car.web.EngineWrep;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.car.EngineDescRepo;
import com.web.hyundai.repo.car.EngineRepo;
import com.web.hyundai.repo.car.modif.CarComplectRepo;
import com.web.hyundai.service.car.CarBuildService;
import com.web.hyundai.service.car.EngineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = "Car-Engine")
@CrossOrigin("*")

public class EngineController {



    @Autowired
    CarRepo carRepo;

    @Autowired
    EngineService engineService;

    @Autowired
    EngineRepo engineRepo;

    @Autowired
    EngineDescRepo engineDescRepo;

    @Autowired
    CarBuildService carBuildService;

    @Autowired
    CarComplectRepo carComplectRepo;





    @GetMapping(path = "/admin/car-engine/findall/{carid}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> findallEngine(@RequestHeader("accept-language") String lang, @PathVariable Long carid){

        Optional<Car> car = carRepo.findById(carid);
        if (car.isPresent()) {
            List<EngineWrep> eng = carBuildService.engineBuild(lang, car.get(),"engine");
            if (eng.size() > 0) return ResponseEntity.ok(eng);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი მანქანა ვერ მოიძებნა ან ამ მანქანაზე მიმაგრებული ძრავი");
    }


    @PostMapping(path = "/admin/car-engine/create/{carid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<Engine> createEngine
            (
                    @PathVariable Long carid,
                    @RequestParam("title") String title,
                    @RequestParam(value = "hp",required = false,defaultValue = "0") @ApiParam(value = "ცხენის ძალა ინტიჯერში", required = true) int hp,
                    @RequestParam("price") int price,
                    @RequestParam("city") @ApiParam(value = "წვა ქალაქში", required = true) String city,
                    @RequestParam("outCity") @ApiParam(value = "წვა ქალაქ გარეთ", required = true) String outCity,
                    @RequestParam("hundred") @ApiParam(value = "წვა 100კმ ზე", required = true) String hundred,
                    @RequestParam("combined") @ApiParam(value = "შერეული წვა", required = true) String combined
            ) throws IOException {


        Engine engine = engineService.createEngine(carid,title,price,hp,city,outCity,hundred,combined);

        if (engine.getId() != null) return ResponseEntity.ok(engine);


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Engine());
    }


    @PostMapping(path = "/admin/car-engine/update/{engineid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<EngineWrep> updateEngine(
            @PathVariable Long engineid,
            @RequestParam("title") String title,
            @RequestParam("hp") @ApiParam(value = "ცხენის ძალა ინტიჯერში", required = true) int hp,
            @RequestParam("price") int price,
            @RequestParam("city") @ApiParam(value = "წვა ქალაქში", required = true) String city,
            @RequestParam("outCity") @ApiParam(value = "წვა ქალაქ გარეთ", required = true) String outCity,
            @RequestParam("hundred") @ApiParam(value = "წვა 100კმ ზე", required = true) String hundred,
            @RequestParam("combined") @ApiParam(value = "შერეული წვა", required = true) String combined,
            @RequestHeader("accept-language") String lang
    ) throws IOException {


        Engine engine = engineService.updateEngine(engineid, title, hp, price,  city, outCity, hundred,combined);

        List<EngineWrep> engineWreps = new ArrayList<>();
        EngineWrep engineWrep = new EngineWrep();
        engineWrep.setCarLogo(engine.getCar().getLogo());
        engineWrep.setEngineId(engine.getId());
        engineWrep.setEngineTitle(engine.getTitle());
        engineWrep.setCity(engine.getFuelUsage().getCity());
        engineWrep.setOutCity(engine.getFuelUsage().getOutCity());
        engineWrep.setHundred(engine.getFuelUsage().getHundred());
        engineWrep.setCombined((engine.getFuelUsage().getCombined()));
        engineWrep.setHp(engine.getHp());
        engineWrep.setPrice(engine.getPrice());
        Optional<CarComplect> cmp = carComplectRepo.findByEngineId(engine.getId());
        if (cmp.isPresent()) {
            engineWrep.setComplectId(cmp.get().getId());
            engineWrep.setComplectName(cmp.get().getName());
            if (lang.equals("ka")) engineWrep.setComplectName(cmp.get().getNameGEO());
        }

        List<EngineDescWeb> engineDescWebs = new ArrayList<>();

        List<EngineDesc> descs = engineDescRepo.findAllByEngineId(engine.getId());
        descs.forEach(engineDesc -> {
            EngineDescWeb engineDescWeb = new EngineDescWeb();

            engineDescWeb.setId(engineDesc.getId());
            engineDescWeb.setName(engineDesc.getName());
            engineDescWeb.setEngineDescLogo(engineDesc.getEngineDescLogo().toString());
            if (lang.equals("ka")) engineDescWeb.setName(engineDesc.getNameGEO());
            engineDescWebs.add(engineDescWeb);

        });
        engineWrep.setEngineDesc(engineDescWebs);


        if (engine.getId() != null) return ResponseEntity.ok(engineWrep);


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new EngineWrep());
    }


    @PostMapping(path = "/admin/car-engine/delete/{engineid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> deleteEngine(@PathVariable Long engineid) {

        if (engineService.deleteEngine(engineid).equals("1")) {
            return ResponseEntity.ok("წარმატებით წაიშალა");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი ძრავი ვერ მოიძებნა");


    }

    @GetMapping(path = "/admin/car-engine/desc/findall/{engineid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<List<EngineDesc>> findAllEngineDesc(@PathVariable Long engineid){
        List<EngineDesc> engineDesc = engineDescRepo.findAllByEngineId(engineid);
        return ResponseEntity.ok(engineDesc);
    }


    @PostMapping(path = "/admin/car-engine/desc/create/{engineid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> createEngineDesc(@PathVariable Long engineid,
                                                   @RequestParam String name,
                                                   @RequestParam String nameGEO,
                                                   @ApiParam(value = "FUEL,SPEED,ENGINE,SHIELD",required = true)
                                                   @RequestParam String logo){
        boolean enumCheck = false;
        EngineDescLogo[] values = EngineDescLogo.values();
        for (EngineDescLogo value : values) {
            if (value.name().equals(logo)) {enumCheck=true; break;}
        }
        if (!enumCheck) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ლოგო სახელი არ ემთხვევა ობიექტის სახელს.");
        }


        Optional<Engine> engine = engineRepo.findById(engineid);
        if (engine.isPresent()) {
            EngineDesc engineDesc = new EngineDesc(name,nameGEO,EngineDescLogo.valueOf(logo),engine.get());
            return ResponseEntity.ok(engineDescRepo.save(engineDesc));

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი ძრავი ვერ მოიძებნა");

    }

    @PostMapping(path = "/admin/car-engine/desc/update/{descid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> updateEngineDesc(@PathVariable Long descid,
                                                   @RequestParam String name,
                                                   @RequestParam String nameGEO,
                                                   @ApiParam(value = "FUEL,SPEED,ENGINE,SHIELD",required = true)
                                                   @RequestParam String logo){

        boolean enumCheck = false;
        EngineDescLogo[] values = EngineDescLogo.values();
        for (EngineDescLogo value : values) {
            if (value.name().equals(logo)) {enumCheck=true; break;}
        }
        if (!enumCheck) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ლოგო სახელი არ ემთხვევა ობიექტის სახელს.");
        }

        Optional<EngineDesc> desc = engineDescRepo.findById(descid);
        if (desc.isPresent()) {
            desc.get().setName(name);
            desc.get().setNameGEO(nameGEO);
            desc.get().setEngineDescLogo(EngineDescLogo.valueOf(logo));

            return ResponseEntity.ok(engineDescRepo.save(desc.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი ძრავის დახასიათება ვერ მოიძებნა");

    }

    @PostMapping(path = "/admin/car-engine/desc/delete/{descid}",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> deleteEngineDesc(@PathVariable Long descid){

        Optional<EngineDesc> desc = engineDescRepo.findById(descid);
        if (desc.isPresent()) {

            engineDescRepo.deleteById(descid);
            return ResponseEntity.ok("1");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი ძრავის დახასიათება ვერ მოიძებნა");

    }


}



