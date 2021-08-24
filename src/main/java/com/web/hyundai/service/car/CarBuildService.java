package com.web.hyundai.service.car;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.car.*;
import com.web.hyundai.model.car.modif.CarComplect;
import com.web.hyundai.model.car.modif.CarTire;
import com.web.hyundai.model.car.web.*;
import com.web.hyundai.model.home.CarSizeList;
import com.web.hyundai.repo.car.*;
import com.web.hyundai.repo.car.modif.CarComplectRepo;
import com.web.hyundai.repo.car.modif.CarTireRepo;
import com.web.hyundai.repo.translate.TranslateRepo;
import com.web.hyundai.service.ImageService;
import com.web.hyundai.service.news.NewsService;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class CarBuildService {



    @Autowired
    CarRepo carRepo;
    @Autowired
    ComfortRepo comfortRepo;
    @Autowired
    EngineDescRepo engineDescRepo;
    @Autowired
    EngineRepo engineRepo;
    @Autowired
    FeatureRepo featureRepo;
    @Autowired
    ModedPhotoRepo modedPhotoRepo;
    @Autowired
    Photo360Repo photo360Repo;
    @Autowired
    Photo360IntRepo photo360IntRepo;
    @Autowired
    PhotoFeaturesRepo photoFeaturesRepo;
    @Autowired
    ImageService imageService;
    @Autowired
    ModedPhotoDetailsRepo modedPhotoDetailsRepo;
    @Autowired
    private TranslateRepo translateRepo;
    @Autowired
    private CarComplectRepo carComplectRepo;
    @Autowired
    private CarColorRepo carColorRepo;
    @Autowired
    private CarTireRepo carTireRepo;
    @Autowired
    private NewsService newsService;




    public CarWeb buildCar(String model, String lang) {

        CarWeb carWeb = new CarWeb();

        Optional<Car> car = carRepo.findActiveCarByModel(model);
        if (car.isPresent()) {
            carWeb.setLogo(car.get().getLogo());
            carWeb.setSlugURL(car.get().getSlugURL());
            carWeb.setElectro(car.get().isElectro());
            carWeb.setNew(car.get().isNew());
            carWeb.setId(car.get().getId());
            carWeb.setYear(car.get().getYear());
            carWeb.setModel(WordUtils.capitalize(car.get().getModel()));
            carWeb.setPlace(car.get().getPlace());
            carWeb.setPrice(car.get().getPrice());
            carWeb.setFile(car.get().getFile());
            carWeb.setTitle(car.get().getTitle());
            if (lang.equals("ka")) carWeb.setTitle(car.get().getTitleGEO());
            carWeb.setSlider(car.get().getSlider());
            carWeb.setSlider2(car.get().getSlider2());
            carWeb.setVideoSlider(car.get().getVideoSlider());
            carWeb.setVideoSliderURL(car.get().getVideoSliderURL());
            carWeb.setVehicleType(car.get().getVehicleType().name());
            carWeb.setActive(car.get().getActive());
            Optional<CarTire> tire = carTireRepo.findOneTireByCarId(car.get().getId());
            tire.ifPresent(carWeb::setCarTire);

            // engine

            carWeb.setEngineList(engineBuild(lang, car.get(),"detail"));



            // car feature
            List<FeatureWeb> featureWebList = new ArrayList<>();
            featureRepo.findAllByCarId(car.get().getId()).forEach(feature -> {
                FeatureWeb feature1 = new FeatureWeb();
                feature1.setId(feature.getId());
                feature1.setName(feature.getName());
                if (lang.equals("ka")) feature1.setName(feature.getNameGEO());
                //feature1.setNameGEO(feature.getNameGEO());
                featureWebList.add(feature1);
            });
            carWeb.setFeatures(featureWebList);


            //photo360
            ArrayList<Photo360Web> photoList = new ArrayList<>();
            ArrayList<CarColor> colorList = new ArrayList<>();
            Optional<Photo360> photo360 = photo360Repo.findAllByCarIdOne(car.get().getId());
            if (photo360.isPresent()) {
                Photo360Web photo360Web = new Photo360Web();
                photo360Web.setCarColor(photo360.get().getCarColor());
                photo360Web.setPhoto360ListList(photo360.get().getPhoto360List());
                photo360Web.setId(photo360.get().getId());
                photoList.add(photo360Web);
            }
            carWeb.setPhoto360s(photoList);

            photo360Repo.findAllByCarId(car.get().getId()).forEach(photo3601 -> colorList.add(photo3601.getCarColor()));
            carWeb.setColors(colorList);

            //photo360Int
            Optional<Photo360Int> photo360Int = photo360IntRepo.findByCarId(car.get().getId());
            photo360Int.ifPresent(carWeb::setPhoto360Int);


            // modedPhoto
            List<ModedPhotoDetailsWeb> modedPhotoDetailsList = new ArrayList<>();


            List<ModedPhotoWeb> modedPhotoList = new ArrayList<>();
            modedPhotoRepo.findAllByCarId(car.get().getId()).forEach(gallery -> {
                ModedPhotoWeb galleryWeb = new ModedPhotoWeb();
                galleryWeb.setId(gallery.getId());
                galleryWeb.setImage(gallery.getImage());
                modedPhotoList.add(galleryWeb);

                modedPhotoDetailsRepo.findAllByPhotoId(gallery.getId()).forEach(detail -> {
                    ModedPhotoDetailsWeb modedPhotoDetailsWeb = new ModedPhotoDetailsWeb();
                    modedPhotoDetailsWeb.setId(detail.getId());
                    modedPhotoDetailsWeb.setHeight(detail.getHeight());
                    modedPhotoDetailsWeb.setWidth(detail.getWidth());
                    modedPhotoDetailsWeb.setImage(detail.getImage());
                    modedPhotoDetailsWeb.setTitle(detail.getTitle());
                    if (lang.equals("ka")) modedPhotoDetailsWeb.setTitle(detail.getTitleGEO());
                    //modedPhotoDetailsWeb.setTitleGEO(detail.getTitleGEO());
                    modedPhotoDetailsWeb.setType(detail.getDesignType().toString());
                    modedPhotoDetailsWeb.setPart(detail.getPart());
                    modedPhotoDetailsList.add(modedPhotoDetailsWeb);
                });
                galleryWeb.setModedPhotoDetailsWebs(modedPhotoDetailsList);


            });
            carWeb.setModedPhotosList(modedPhotoList);

            // PhotoFeatures
            List<PhotoFeaturesWeb> photoFeaturesWebList = new ArrayList<>();
            photoFeaturesRepo.findAllByCarId(car.get().getId()).forEach(photoFeatures -> {
                PhotoFeaturesWeb photoFeaturesWeb = new PhotoFeaturesWeb();
                photoFeaturesWeb.setId(photoFeatures.getId());
                photoFeaturesWeb.setDesc(photoFeatures.getDesc());
                if (lang.equals("ka")) photoFeaturesWeb.setDesc(photoFeatures.getDescGEO());
                //photoFeaturesWeb.setDescGEO(photoFeatures.getDescGEO());
                photoFeaturesWeb.setTitle(photoFeatures.getTitle());
                if (lang.equals("ka")) photoFeaturesWeb.setTitle(photoFeatures.getTitleGEO());
                //photoFeaturesWeb.setTitleGEO(photoFeatures.getTitleGEO());
                photoFeaturesWeb.setImage(photoFeatures.getImage());
                photoFeaturesWeb.setType(photoFeatures.getType());
                photoFeaturesWebList.add(photoFeaturesWeb);
            });
            carWeb.setPhotoFeatures(photoFeaturesWebList);

            //comfort
            List<ComfortWeb> comfortArrayList = new ArrayList<>();

            comfortRepo.findAllByCarId(car.get().getId()).forEach(comfort -> {
                ComfortWeb comfortWeb = new ComfortWeb();
                comfortWeb.setId(comfort.getId());
                comfortWeb.setDesc(comfort.getDesc());
                if (lang.equals("ka")) comfortWeb.setDesc(comfort.getDescGEO());
                //comfortWeb.setDescGEO(comfort.getDescGEO());
                comfortWeb.setImage(comfort.getImage());
                comfortWeb.setTitle(comfort.getTitle());
                if (lang.equals("ka")) comfortWeb.setTitle(comfort.getTitleGEO());
                //comfortWeb.setTitleGEO(comfort.getTitleGEO());
                comfortArrayList.add(comfortWeb);
            });
            carWeb.setComfort(comfortArrayList);


        }
        return carWeb;

    }

    public List<EngineWrep> engineBuild(String lang, Car car,String check) {
        HashMap<String,List<?>> map = new HashMap<>();

        List<EngineWrep> engineWreps = new ArrayList<>();
        List<Engine> engine = new ArrayList<>();
        if (check.equals("detail")) {
            engine = engineRepo.findAllByCarIdLimit3(car.getId());

        }else {
            engine = engineRepo.findAllByCarId(car.getId());
        }
        engine.forEach(engine1 -> {
            EngineWrep engineWrep = new EngineWrep();

            Optional<CarComplect> cmp = carComplectRepo.findByEngineId(engine1.getId());
            if (cmp.isPresent()) {
                engineWrep.setComplectId(cmp.get().getId());
                engineWrep.setComplectName(cmp.get().getName());
                if (lang.equals("ka")) engineWrep.setComplectName(cmp.get().getNameGEO());
            }
            engineWrep.setCarLogo(car.getLogo());
            engineWrep.setEngineId(engine1.getId());
            engineWrep.setEngineTitle(engine1.getTitle());
            //if (lang.equals("ka")) engineWrep.setTitle(engine1.getTitleGEO());
            //engineWrep.setTitleGEO(engine1.getTitleGEO());

            engineWrep.setCity(engine1.getFuelUsage().getCity());
            engineWrep.setOutCity(engine1.getFuelUsage().getOutCity());
            engineWrep.setHundred(engine1.getFuelUsage().getHundred());
            engineWrep.setCombined((engine1.getFuelUsage().getCombined()));
            //engineWrep.setFile(engine1.getFile());
            engineWrep.setHp(engine1.getHp());
            engineWrep.setPrice(engine1.getPrice());

            List<EngineDescWeb> engineDescWebs = new ArrayList<>();

            List<EngineDesc> descs = engineDescRepo.findAllByEngineId(engine1.getId());
            descs.forEach(engineDesc -> {
                EngineDescWeb engineDescWeb = new EngineDescWeb();

                engineDescWeb.setId(engineDesc.getId());
                engineDescWeb.setName(engineDesc.getName());
                engineDescWeb.setEngineDescLogo(engineDesc.getEngineDescLogo().toString());
                if (lang.equals("ka")) engineDescWeb.setName(engineDesc.getNameGEO());
                engineDescWebs.add(engineDescWeb);

            });
            engineWrep.setEngineDesc(engineDescWebs);


            engineWreps.add(engineWrep);
        });
        return engineWreps;
    }

    public Car createCar(String model, int year, int place, String title, String titleGEO, MultipartFile slider,
                         MultipartFile slider2, MultipartFile videoSlider, MultipartFile logo, MultipartFile file, int price, String type, int active, String videoSliderURL, boolean isElectro, boolean isNew) throws IOException {



        Car car = new Car();

        if (carRepo.findCarByModel(model).isPresent()) {
            car.setId(-1L);
            return car;
        }

        car.setElectro(isElectro);
        car.setNew(isNew);
        car.setModel(model);
        car.setYear(year);
        car.setActive(active);
        car.setPlace(place);
        car.setTitle(title);
        car.setPrice(price);
        car.setTitleGEO(titleGEO);
        String slug = newsService.makeSlug(model);
        if (slug.equals("0")) {
            car.setId(-2L);
            return car;
        }
        car.setSlugURL(slug);
        if (null!=videoSlider) car.setVideoSliderURL(videoSliderURL);
        VehicleType vehicleType;
        try {
            System.out.println("tryshi var");
            vehicleType = VehicleType.valueOf(type.toUpperCase());
            System.out.println(vehicleType);
        } catch (Exception e) {
            System.out.println("catcshi var");
            vehicleType = VehicleType.SEDAN;
            System.out.println(vehicleType);
        }
        System.out.println(vehicleType + " saboloo");
        car.setVehicleType(vehicleType);

        if (slider.getBytes().length > 0) {
            String sliderCar = imageService.uploadNewDir(slider,Path.getCarPath(car.getModel().toLowerCase().trim()));
            car.setSlider(sliderCar);
        }

        if (slider2.getBytes().length > 0) {
            String slider2Car = imageService.uploadNewDir(slider2, Path.getCarPath(car.getModel()).trim().toLowerCase());
            car.setSlider2(slider2Car);
        }

        if (videoSlider != null && videoSlider.getBytes().length > 0) {
            String videoSliderCar = imageService.uploadNewDir(videoSlider, Path.getCarPath(car.getModel()).trim().toLowerCase());
            car.setVideoSlider(videoSliderCar);
        }

        if (logo.getBytes().length > 0) {
            String carLogo = imageService.uploadNewDir(logo, Path.getCarPath(car.getModel()).trim().toLowerCase());
            car.setLogo(carLogo);
        }

        if (file.getBytes().length > 0) {
            String fileUpload = imageService.uploadNewDir(file, Path.CAR_PATH_FILES);
            car.setFile(fileUpload);
        }

        return carRepo.save(car);


    }

    public Car updateCar(String model, int year, int place, String title, String titleGEO,
                         MultipartFile slider, MultipartFile slider2, MultipartFile videoSlider, MultipartFile logo, MultipartFile file, int price, String type, int active, Long id, String videoSLiderURL, boolean isElectro, boolean isNew) throws IOException {

        Optional<Car> car = carRepo.findById(id);
        if (car.isPresent()) {
            Optional<Car> sameCarCheck = carRepo.findCarByModel(model);
            if (sameCarCheck.isPresent() && sameCarCheck.get().getId() != car.get().getId()) {
                Car newcar = new Car();
                newcar.setId(-1L);
                return newcar;
            }
            car.get().setElectro(isElectro);
            car.get().setNew(isNew);
            car.get().setModel(model);
            car.get().setYear(year);
            car.get().setActive(active);
            car.get().setPlace(place);
            car.get().setTitle(title);
            car.get().setPrice(price);
            car.get().setTitleGEO(titleGEO);
            String slug = newsService.makeSlug(model);
            if (slug.equals("0")) {
                car.get().setId(-2L);
                return car.get();
            }
            car.get().setSlugURL(slug);
            if (null!=videoSLiderURL) car.get().setVideoSliderURL(videoSLiderURL);

            try {
                car.get().setVehicleType(VehicleType.valueOf(type));
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (null != slider && slider.getBytes().length > 0) {
                File oldfile = new File(Path.folderPath() + car.get().getSlider());
                if (oldfile.exists()) oldfile.delete();

                String sliderCar = imageService.uploadNewDir(slider, Path.getCarPath(car.get().getModel()).trim().toLowerCase());
                car.get().setSlider(sliderCar);

            }

            if (null != slider2 && slider2.getBytes().length > 0) {
                File oldfile = new File(Path.folderPath() + car.get().getSlider2());
                if (oldfile.exists()) oldfile.delete();
                String slider2Car = imageService.uploadNewDir(slider2, Path.getCarPath(car.get().getModel()).trim().toLowerCase());
                car.get().setSlider2(slider2Car);
            }

            if (null != videoSlider && videoSlider.getBytes().length >0) {
                File oldfile = new File(Path.folderPath() + car.get().getVideoSlider());
                if (oldfile.exists()) oldfile.delete();
                String videoSliderCar = imageService.uploadNewDir(videoSlider, Path.getCarPath(car.get().getModel()).trim().toLowerCase());
                car.get().setVideoSlider(videoSliderCar);
            }

            if (null != logo && logo.getBytes().length > 0) {
                File oldfile = new File(Path.folderPath() + car.get().getLogo());
                if (oldfile.exists()) oldfile.delete();

                String carLogo = imageService.uploadNewDir(logo, Path.getCarPath(car.get().getModel()).trim().toLowerCase());
                car.get().setLogo(carLogo);
            }

            if (null != file && file.getBytes().length > 0) {

                File oldFile = new File(Path.folderPath() + car.get().getFile());
                if (oldFile.exists()) oldFile.delete();


                String fileUpload = imageService.uploadNewDir(file, Path.CAR_PATH_FILES);
                car.get().setFile(fileUpload);
            }

            return carRepo.save(car.get());
        }
        return new Car();
    }


    public List<HomeCarWeb> getCars(String lang,String key) {
        List<Car> cars;
        if (key.equals("config")) {
            cars = carRepo.findAllActiveCar();
        }
        else {
            cars = carRepo.findAllActiveCarLimit10();
        }
        List<HomeCarWeb> carWebs = new ArrayList<>();
        cars.forEach(car -> {
            String typeName = car.getVehicleType().name();

            car.setModel(WordUtils.capitalize(car.getModel()));
            HomeCarWeb homeCarWeb = new HomeCarWeb(
                    car.getId(), car.getModel(), car.getYear(), car.getPlace(),
                    car.getTitle(), car.getSlider(), car.getLogo(), car.getPrice(),
                    typeName, car.getActive(),car.getSlugURL(),car.isElectro(),car.isNew());

            if (null != lang && lang.toLowerCase().equals("ka")) {
                homeCarWeb.setTitle(car.getTitleGEO());
                translateRepo.findById(typeName)
                        .ifPresent(value -> homeCarWeb.setVehicleType(value.getValueGEO()));
            }


            carWebs.add(homeCarWeb);
        });
        return carWebs;
    }


    public List<HomeCarWeb> getCarsByModel(String lang,String type) {
        List<Car> cars = carRepo.findActiveCarByType(type);
        List<HomeCarWeb> carWebs = new ArrayList<>();
        cars.forEach(car -> {
            String typeName = car.getVehicleType().name();
            car.setModel(WordUtils.capitalize(car.getModel()));
            HomeCarWeb homeCarWeb = new HomeCarWeb(
                    car.getId(), car.getModel(), car.getYear(), car.getPlace(),
                    car.getTitle(), car.getSlider(), car.getLogo(), car.getPrice(),
                    typeName, car.getActive(),car.getSlugURL(),car.isElectro(),car.isNew());

            if (null != lang && lang.toLowerCase().equals("ka")) {
                homeCarWeb.setTitle(car.getTitleGEO());
                translateRepo.findById(typeName)
                        .ifPresent(value -> homeCarWeb.setVehicleType(value.getValueGEO()));
            }
            carWebs.add(homeCarWeb);
        });
        return carWebs;
    }

    public List<CarSizeList> getSizeList() {
        List<Car> cars =  carRepo.findAllActiveCar();
        List<CarSizeList> carSizeList = carRepo.countActiveCarByType();
        carSizeList.add(new CarSizeList() {
            @Override
            public String getType() {
                return "all";
            }

            @Override
            public int getSize() {
                return cars.size();
            }
        });
        return  carSizeList;
    }




    public String jsonExcludeFilter(String filterName, Set<String> fields, Object object) throws JsonProcessingException {
        SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
                .serializeAllExcept(fields);
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter(filterName, theFilter);
        return new ObjectMapper().writer(filters).writeValueAsString(object);
    }


    public String jsonFilter(String filterName,Object object) throws JsonProcessingException {
        SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter.serializeAll();
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter(filterName, theFilter);
        return new ObjectMapper().writer(filters).writeValueAsString(object);
    }

}