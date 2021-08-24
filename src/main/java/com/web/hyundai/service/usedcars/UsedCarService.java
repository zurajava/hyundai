package com.web.hyundai.service.usedcars;


import com.web.hyundai.model.FileUpload;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.translation.Translate;
import com.web.hyundai.model.usedcars.UsedCar;
import com.web.hyundai.model.usedcars.UsedCarModel;
import com.web.hyundai.model.usedcars.UsedCarPhoto;
import com.web.hyundai.model.usedcars.UsedCarWeb;
import com.web.hyundai.repo.translate.TranslateRepo;
import com.web.hyundai.repo.usedcars.UsedCarModelRepo;
import com.web.hyundai.repo.usedcars.UsedCarPhotoRepo;
import com.web.hyundai.repo.usedcars.UsedCarRepo;
import com.web.hyundai.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UsedCarService {
    @Autowired
    private UsedCarRepo usedCarRepo;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UsedCarPhotoRepo usedCarPhotoRepo;

    @Autowired
    private UsedCarModelRepo usedCarModelRepo;

//    @Autowired
//    private UsedCarFeatureRepo featureRepo;

    @Autowired
    private TranslateRepo translateRepo;

    private final String folder = Path.usedCarPath();


    public UsedCar create(String extColor,
                          String intColor,
                          double hp,
                          String engine,
                          Long modelid,
                          Integer price,
                          String year,
                          Integer mileage,
                          Integer door,
                          String type,
                          String fuel,
                          String tran,
                          MultipartFile displayFile,
                          ArrayList<MultipartFile> files

//                          ArrayList<String> features,
 //                         ArrayList<String> featuresGEO


    ) throws IOException {

//        Set<UsedCarFeature> featuresList = new HashSet<>();
//        if (features.size() == featuresGEO.size()) {
//
//            for (int i = 0; i <features.size() ; i++) {
//                featuresList.add(new UsedCarFeature(features.get(i),featuresGEO.get(i)));
//            }
//        }


        Optional<UsedCarModel> model = usedCarModelRepo.findById(modelid);


        UsedCar usedCar = new UsedCar();
        usedCar.setExtColor(extColor);
        usedCar.setIntColor(intColor);
        usedCar.setHp(hp);
        usedCar.setEngine(engine);
        usedCar.setDoor(door);
        usedCar.setFuel(fuel);
        usedCar.setMileage(mileage);
        model.ifPresent(usedCar::setUsedCarModel);
        usedCar.setPrice(price);
        usedCar.setTransmission(tran);
        usedCar.setPrice(price);
        usedCar.setType(type);
        usedCar.setYear(Integer.parseInt(year));
        usedCar.setDisplayPhoto(imageService.thumbnailFromImage(displayFile, folder));
        //featureRepo.saveAll(featuresList);
        //usedCar.setUsedCarFeatures(featuresList);
        Set<UsedCarPhoto> usedCarPhotos = new HashSet<>();
        files.forEach(file -> {

            try {
                FileUpload img1 = imageService.uploadImage(file, folder);

                UsedCarPhoto usedCarPhoto = new UsedCarPhoto();

                usedCarPhoto.setName(folder + img1.getFile().getName());
                usedCarPhotos.add(usedCarPhoto);

            } catch (IOException e) {
                e.printStackTrace();
            }

        });


        usedCarPhotoRepo.saveAll(usedCarPhotos);
        usedCar.setUsedCarPhotoList(usedCarPhotos);




        return usedCarRepo.save(usedCar);
    }

    public String delete(Long id) {

        Optional<UsedCar> car = usedCarRepo.findById(id);
        if (car.isPresent()) {
            File image = new File(Path.folderPath() + car.get().getDisplayPhoto());
            if (image.exists()) image.delete();

            Set<UsedCarPhoto> files = car.get().getUsedCarPhotoList();
            System.out.println(files + " ++++++++");
            files.forEach(file -> {
                File img = new File(Path.folderPath() + file.getName());
                System.out.println(img.getAbsolutePath());
                if (img.exists()) img.delete();
            });

            usedCarRepo.deleteById(id);
            usedCarPhotoRepo.deleteAll(files);
            return "1";
        }
        return "0";
    }


    public ResponseEntity<?> carUpdate(Long id, UsedCar updatedCars, MultipartFile file) {


        AtomicInteger counter = new AtomicInteger(0);

        Optional<UsedCar> dbCars = usedCarRepo.findById(id);


        dbCars.ifPresent(oldCar -> {
            oldCar.setYear(updatedCars.getYear());
            oldCar.setType(updatedCars.getType());
            oldCar.setPrice(updatedCars.getPrice());
            oldCar.setTransmission(updatedCars.getTransmission());
            oldCar.setFuel(updatedCars.getFuel());
            oldCar.setDoor(updatedCars.getDoor());
            oldCar.setExtColor(updatedCars.getExtColor());
            oldCar.setMileage(updatedCars.getMileage());
            oldCar.setHp(updatedCars.getHp());
            oldCar.setIntColor(updatedCars.getIntColor());
            oldCar.setEngine(updatedCars.getEngine());



            if (null != file && !file.isEmpty()) {

                File oldFile = new File(Path.folderPath() + oldCar.getDisplayPhoto());
                try {
                    FileUpload fileUpload = imageService.uploadImage(file, Path.usedCarPath());
                    oldCar.setDisplayPhoto(Path.usedCarPath() + fileUpload.getFile().getName());
                    if (oldFile.exists()) oldFile.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            counter.incrementAndGet();
        });

        if (counter.get() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ასეთი მანქანა არ მოიძებნა");
        }


        return ResponseEntity.ok(usedCarRepo.save(dbCars.get()));

    }


    public List<UsedCarWeb> translateAllCars(String lang, List<UsedCarWeb> webObj){

        if (lang.equals("ka")) {

            webObj.forEach(usedCar -> {
                Optional<Translate> extColor = translateRepo.findById(usedCar.getExtColor().toUpperCase());
                Optional<Translate> intColor = translateRepo.findById(usedCar.getIntColor().toUpperCase());
                Optional<Translate> fuel = translateRepo.findById(usedCar.getFuel().toUpperCase());
                Optional<Translate> trans = translateRepo.findById(usedCar.getTransmission().toUpperCase());

                trans.ifPresent(translate -> usedCar.setTransmission(translate.getValueGEO()));
                extColor.ifPresent(translate -> usedCar.setExtColor(translate.getValueGEO()));
                intColor.ifPresent(translate -> usedCar.setIntColor(translate.getValueGEO()));
                fuel.ifPresent(translate -> usedCar.setFuel(translate.getValueGEO()));
            });
        }

        return  webObj;
    }


}
