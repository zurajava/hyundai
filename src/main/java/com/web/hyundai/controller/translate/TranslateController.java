package com.web.hyundai.controller.translate;

import com.web.hyundai.model.translation.Translate;
import com.web.hyundai.repo.translate.TranslateRepo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api("Translates")
@CrossOrigin("*")
public class TranslateController {

    @Autowired
    private TranslateRepo translateRepo;

    @PostMapping("/admin/translate/create")
    public ResponseEntity<Translate> translateCreate(@RequestParam String key,
                                                     @RequestParam String valueGEO){
        return ResponseEntity.ok(translateRepo.save(new Translate(key.toUpperCase(),valueGEO)));
    }

    @GetMapping("/admin/translate/findall")
    public ResponseEntity<List<Translate>> translateFindAll(){
        return ResponseEntity.ok(translateRepo.findAll());
    }
}
