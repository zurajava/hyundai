package com.web.hyundai.bootstrap;

import com.web.hyundai.model.about.About;
import com.web.hyundai.model.about.AboutFeature;
import com.web.hyundai.model.car.*;
import com.web.hyundai.model.car.modif.CarComplect;
import com.web.hyundai.model.car.modif.ComplectParam;
import com.web.hyundai.model.car.modif.ComplectParamDetail;
import com.web.hyundai.model.home.Home;
import com.web.hyundai.model.legalinfo.LegalInfo;
import com.web.hyundai.model.translation.Translate;
import com.web.hyundai.model.usedcars.UsedCar;
import com.web.hyundai.model.usedcars.UsedCarModel;
import com.web.hyundai.model.user.User;
import com.web.hyundai.repo.about.AboutFeatureRepo;
import com.web.hyundai.repo.about.AboutRepo;
import com.web.hyundai.repo.car.*;
import com.web.hyundai.repo.car.modif.CarComplectRepo;
import com.web.hyundai.repo.car.modif.ComplectParamRepo;
import com.web.hyundai.repo.home.HomeRepo;
import com.web.hyundai.repo.legalinfo.LegalInfoRepo;
import com.web.hyundai.repo.news.NewsRepo;
import com.web.hyundai.repo.translate.TranslateRepo;
import com.web.hyundai.repo.usedcars.UsedCarModelRepo;
import com.web.hyundai.repo.usedcars.UsedCarRepo;
import com.web.hyundai.repo.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class DataSave {

    @Autowired
    private LegalInfoRepo legalInfoRepo;

    @Autowired
    private AboutRepo aboutRepo;

    @Autowired
    private TranslateRepo translateRepo;

    @Autowired
    private HomeRepo homeRepo;

    @Autowired
    private UserRepo userRepo;

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
    PhotoFeaturesRepo photoFeaturesRepo;
    @Autowired
    ModedPhotoDetailsRepo modedPhotoDetailsRepo;
    @Autowired
    AboutFeatureRepo aboutFeatureRepo;
    @Autowired
    NewsRepo newsRepo;
    @Autowired
    CarComplectRepo carComplectRepo;
    @Autowired
    ComplectParamRepo complectParamRepo;
    @Autowired
    UsedCarRepo usedCarRepo;
    @Autowired
    UsedCarModelRepo usedCarModelRepo;
    @Autowired
    CarColorRepo carColorRepo;



    @Bean
    public void createDirs(){
        String[] dirs = {"about", "car", "carservicelogos", "excel", "home", "legalinfo", "news", "usedcars"};
        File directory = new File("dodoimage");
        if (!directory.exists()) {
            directory.mkdir();
            for (String dir : dirs) {
                new File(directory.getName() + "/" + dir).mkdir();
            }
        }
    }



    @Bean
    public void translationData(){

        if(translateRepo.findAll().size() < 1) {

            List<Translate> translateList = new ArrayList<>();
            Translate translate = new Translate("BLACK", "შავი");
            translateList.add(translate);

            Translate translate2 = new Translate("RED", "წითელი");
            translateList.add(translate2);

            Translate translate3 = new Translate("WHITE", "თეთრი");
            translateList.add(translate3);

            Translate translate4 = new Translate("PETROL", "ბენზინი");
            translateList.add(translate4);

            Translate translate5 = new Translate("DIESEL", "დიზელი");
            translateList.add(translate5);

            Translate translate6 = new Translate("SEDAN", "სედანი");
            translateList.add(translate6);

            Translate translate7 = new Translate("COMMERCIAL", "კომერციული");
            translateList.add(translate7);

            Translate translate8 = new Translate("MANUAL", "მექანიკა");
            translateList.add(translate8);

            Translate translate9 = new Translate("AUTOMATIC", "ავტომატიკა");
            translateList.add(translate9);

            translateRepo.saveAll(translateList);
        }

    }




    @Bean
    public void saveAbout(){
        List<About> info = aboutRepo.findAll();

        if (info.size() < 1) {


            List<AboutFeature> features = new ArrayList<>();
            //test
            AboutFeature feature = new AboutFeature("itle","geo","desc","descgeo");
            feature.setType("top");
            feature.setImage("/about/sample.png");
            features.add(aboutFeatureRepo.save(feature));


            About about = new About("/about/sample.png", "Slider Title",
                    "სლაიდერის სათაური", features,"/about/sample.png",
                    "Slider Title", "სლაიდერის სათაური",
                    "history1", "ისტორია 1", "history2", "ისტორია 2",
                    "/about/sample.png", "vision", "ხედვა");
            aboutRepo.save(about);
        }

    }


    @Bean
    public void saveHome(){
        List<Home> home = homeRepo.findAll();

        if (home.size() < 1) {

            ArrayList<Home> homeSlider = new ArrayList<>();
            Home home1 = new Home("title1","სათაური1","/home/sample.png",1,null);
            Home home2 = new Home("title1","სათაური2","/home/sample.png",2,null);
            Home home3 = new Home("title1","სათაური3","/home/sample.png",3,null);
            Home home4 = new Home("title1","სათაური4","/home/sample.png",4,null);

            homeSlider.add(home1);
            homeSlider.add(home2);
            homeSlider.add(home3);
            homeSlider.add(home4);

            homeRepo.saveAll(homeSlider);
        }

    }

    @Bean
    public void saveUser(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User info = userRepo.findByUsername("admin");

        if (info == null) {

            userRepo.save(new User("admin",passwordEncoder.encode("Fm2fp4XugVVJby5X")));
        }

    }


    @Bean
    public void saveUsedCar(){
        if (usedCarRepo.findAll().size() < 1) {


            UsedCarModel model1 = usedCarModelRepo.save(new UsedCarModel("SONATA"));
            UsedCarModel model2 = usedCarModelRepo.save(new UsedCarModel("qONATA"));

            UsedCar usedCar1 = new UsedCar(1350, 1997, 12345, 5,
                    "SUV", "gasoline", "automatic", "red", "black",
                    "1.6  xss", 12.5, "fafa", new HashSet<>(), model1);
            UsedCar usedCar2 = new UsedCar(1350, 1997, 12345, 5,
                    "SUV", "gasoline", "automatic", "red", "black",
                    "1.6  xss", 12.5, "fafa", new HashSet<>(), model1);
            UsedCar usedCar3 = new UsedCar(1350, 1997, 12345, 5,
                    "SUV", "gasoline", "automatic", "red", "black",
                    "1.6  xss", 12.5, "fafa", new HashSet<>(), model2);
            usedCarRepo.save(usedCar1);
            usedCarRepo.save(usedCar2);
            usedCarRepo.save(usedCar3);
        }
    }


    @Bean
    public void saveTestCar(){

        if(carRepo.findAll().size() < 1) {
            Car car = new Car(
                    1L, "sonata", 1997, 5, "tqveni warmavlobistvis",
                    "თქვენი წარმავლობისთვის", "random", "random",
                    "random","random","random",78000, VehicleType.SEDAN, 1, "random",false,false,"soneta");

            Car car2 = new Car(
                    2L, "soneta", 1997, 5, "tqveni warmavlobistvis",
                    "თქვენი წარმავლობისთვის", "random", "random",
                    "random","random","random",78000,VehicleType.SEDAN, 1, "random",false,false,"soneta");

            carRepo.save(car);
            carRepo.save(car2);
            FuelUsage fuelUsage = new FuelUsage("15", "10", "12","13");
            Engine engine = new Engine(1L, "1.6 benzini", 350, 1600, fuelUsage,car);
            Engine engine2 = new Engine(2L, "1.6 benzini", 3503, 16030, fuelUsage,car);
            engineRepo.save(engine);
            engineRepo.save(engine2);

            EngineDesc engineDesc = new EngineDesc(1L, "chveni dzravi sauketesoa", "საუკეთესოა",EngineDescLogo.ENGINE, engine);
            engineDescRepo.save(engineDesc);
            Feature feature = new Feature(1L, "sauketeso manqanis aprobirebuli metodikebis analizi",
                    "საუკეთესო მანქანა", car);
            featureRepo.save(feature);
            //Photo360 photo360 = new Photo360(1L, "foto.luka", "red", 1, car);
            //photo360Repo.save(photo360);


            ModedPhoto gallery = new ModedPhoto(1L, "image.dodo", car);
            modedPhotoRepo.save(gallery);

            ModedPhotoDetails modedPhotoDetails1 = new ModedPhotoDetails(1L, 10, 15, "someimage.jpg",
                    "kargi manqana1", "კარგიმანქანა1", "seat", DesignType.INTERIER, gallery);
            ModedPhotoDetails modedPhotoDetails2 = new ModedPhotoDetails(2L, 10, 15, "someimage.jpg",
                    "kargi manqana2", "კარგიმანქან2ა", "radio", DesignType.TOP, gallery);
            modedPhotoDetailsRepo.save(modedPhotoDetails1);
            modedPhotoDetailsRepo.save(modedPhotoDetails2);


            PhotoFeatures photoFeatures1 = new PhotoFeatures(1L, "ruli", "რული", "saueklteos sensori",
                    "საუკეთესო სენსორი", "fofo.jpg", "ext", car);
            PhotoFeatures photoFeatures2 = new PhotoFeatures(2L, "fari", "ფარი", "saueklteos sensori"
                    , "საუკეთესო სენსორი", "fofo.jpg", "int", car);
            photoFeaturesRepo.save(photoFeatures1);
            photoFeaturesRepo.save(photoFeatures2);
            Comfort comfort = new Comfort(1L, "comforti", "კომფორტი", "wvimis sensori",
                    "წვიმის სენსორი", "dendro.jpg", car);
            comfortRepo.save(comfort);

        }



    }

    //@Bean
    public void saveModif(){

        //carComplectRepo.deleteAll();


        if (carComplectRepo.count() < 1) {
            Set<ComplectParamDetail> complectParamDetailList = new HashSet<>(Arrays.asList(
                    new ComplectParamDetail("field1", "ველი1", "value1", "მნიშვნელობა1"),
                    new ComplectParamDetail("field2", "ველი2", "value2", "მნიშვნელობა2"),
                    new ComplectParamDetail("field1", "ველი1", "value1", "მნიშვნელობა1"),
                    new ComplectParamDetail("field2", "ველი2", "value2", "მნიშვნელობა2")));

            Set<ComplectParamDetail> complectParamDetailList2 = new HashSet<>(Arrays.asList(
                    new ComplectParamDetail("field1", "ველი1", "value1", "მნიშვნელობა1"),
                    new ComplectParamDetail("field2", "ველი2", "value2", "მნიშვნელობა2"),
                    new ComplectParamDetail("field1", "ველი1", "value1", "მნიშვნელობა1"),
                    new ComplectParamDetail("field2", "ველი2", "value2", "მნიშვნელობა2")));





            Set<ComplectParam> complectParamList = Stream.of(
                    new ComplectParam("weight", "წონა",complectParamDetailList),
                    new ComplectParam("size", "ზომა" ,complectParamDetailList2)).collect(Collectors.toSet());


            complectParamRepo.saveAll(complectParamList);


            CarComplect carComplect1 = new CarComplect("comfort", "კომფორტი", null,complectParamList,null,null
                    ,engineRepo.findAll().get(0));
            CarComplect carComplect2 = new CarComplect("sport", "სპორტი", null,complectParamList,null,null
                    ,engineRepo.findAll().get(1));


            carComplectRepo.save(carComplect1);
            carComplectRepo.save(carComplect2);

            color360Create();

            //carComplectRepo.save(carComplect1);
            //CarComplect carComplect2 = new CarComplect("sport", "სპორტი");
            //CarComplect c2 = carComplectRepo.save(carComplect2);
            //c2.setComplectParamDetail(complectParamDetailList);
            //carComplectRepo.save(c2);
        }

    }

    private void color360Create(){
        CarColor carColor1 = new CarColor(1L,"RED","#1234");
        CarColor carColor2 = new CarColor(2L,"BLUE","#4321");
        carColorRepo.save(carColor1);
        carColorRepo.save(carColor2);
    }
}
