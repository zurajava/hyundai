package com.web.hyundai.controller.news;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.news.Hashtag;
import com.web.hyundai.model.news.News;
import com.web.hyundai.model.news.NewsGallery;
import com.web.hyundai.model.news.Subscribe;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.news.HashtagRepo;
import com.web.hyundai.repo.news.NewsGalleryRepo;
import com.web.hyundai.repo.news.NewsRepo;
import com.web.hyundai.repo.news.SubscribeRepo;
import com.web.hyundai.service.ImageService;
import com.web.hyundai.service.car.CarBuildService;
import com.web.hyundai.service.news.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import javax.validation.Valid;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;





@RestController
@Api(tags = "News")
@CrossOrigin("*")

public class NewsController {





    private final int pageSize = 50;
    private final int carNewsSize = 6;
    private final String JSON_FILTER_NAME = "newsFilter";
    private final Set<String> EXCLUDED_FIELDS = Stream.of("titleGEO", "textGEO", "displayTextGEO","car").collect(Collectors.toSet());


    @Autowired
    private NewsRepo newsRepo;

    @Autowired
    private HashtagRepo hashtagRepo;

    @Autowired
    private NewsService newsService;

    @Autowired
    private SubscribeRepo subscribeRepo;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CarBuildService carBuildService;
    @Autowired
    private CarRepo carRepo;
    @Autowired
    private NewsGalleryRepo newsGalleryRepo;

    @GetMapping("/api/news/findallbycar/{carid}")
    public String findAllNewsByCar(
            @PathVariable Long carid,
            @RequestParam(value = "page")
            @ApiParam(value = "პაგინაცია ფეიჯის ნომერი საწყისი 0", required = true, defaultValue = "0") int page,
            @RequestHeader(value = "accept-language", defaultValue = "en") String lang
    ) throws JsonProcessingException {
        Pageable firstPageWithTwoElements = PageRequest.of(page, carNewsSize);

        List<News> news = newsRepo.findAllNewsByCarId(firstPageWithTwoElements,carid);
        if (lang.equals("ka")) {
            newsService.translateNews(news);
        }

        return carBuildService.jsonExcludeFilter(JSON_FILTER_NAME,EXCLUDED_FIELDS,news);
    }


    @PostMapping("/api/news/subscribe")
    public ResponseEntity<String> subscribe(@Valid @RequestBody Subscribe subscribe, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Optional.ofNullable(result.getFieldError("email").getDefaultMessage()).orElse("0"));

        }
        subscribeRepo.save(subscribe);
        return ResponseEntity.ok("1");
    }


    // date fieldit damazaprosos
    @GetMapping(path = "/admin/news/subscribe/get",produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> subscribeXLS(@RequestParam("date")
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                        @ApiParam(value = "date field yyy-MM-dd", required = true) Date date)
            throws IOException {
        List<Subscribe> users = subscribeRepo.findAllWithCreationDateTimeBefore(date);

        System.out.println(users + " ==============");
        //newsService.createXLS(users, date);
        if (newsService.createSpreadSheet(users,date)) {
            return ResponseEntity.ok("1");
        }


        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("ჩანაწერი არ ჩაიწერა");
    }


    @GetMapping(path = "/api/news/prom/findall",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<String> findProm(
            @RequestHeader(value = "accept-language", defaultValue = "en") String lang) throws JsonProcessingException {

        List<News> proms = newsRepo.findAllPromo();
        if (lang.equals("ka")) newsService.translateNews(proms);

        return ResponseEntity.ok(carBuildService.jsonExcludeFilter(JSON_FILTER_NAME,EXCLUDED_FIELDS,proms));
    }


    @GetMapping(path = "/api/news/search")
    public ResponseEntity<String> searchNews(
            @RequestParam("search") String search,
            @RequestHeader(value = "accept-language", defaultValue = "en") String lang) throws JsonProcessingException {

        List<News> news = newsRepo.findAllBySearch(search, search,search,search);
        if (lang.equals("ka")) newsService.translateNews(news);

        return ResponseEntity.ok(carBuildService.jsonExcludeFilter(JSON_FILTER_NAME,EXCLUDED_FIELDS,news));
    }


    //@PostMapping("/admin/news/hashtag/update/{id}")
    public ResponseEntity<?> updateTags(@PathVariable Long id, @RequestBody Hashtag hashtag) {

        Optional<Hashtag> oldtag = hashtagRepo.findById(id);
        if (oldtag.isPresent()) {
            oldtag.get().setName(hashtag.getName());
            return ResponseEntity.ok(hashtagRepo.save(oldtag.get()));

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი თაგი ვერ მოიძებნა");
    }
    //@PostMapping("/admin/news/hashtag/create")
    public ResponseEntity<Hashtag> createTags(@RequestBody Hashtag hashtag) {
        return ResponseEntity.ok(hashtagRepo.save(hashtag));
    }
    //@GetMapping("/api/hashtag/findall")
    public ResponseEntity<List<Hashtag>> listTags() {
        return ResponseEntity.ok(hashtagRepo.findAll());
    }


    // prosta html post unda wamovides hashtagshi idebi
    @PostMapping(path = "/admin/news/create",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<String> createNews(@ApiParam(value = "უნდა შეიცავდეს მხოლოდ ლათინურ ასოებს",required = true)
                                            @RequestParam() String title,
                                             @RequestParam(required = false) Long carid,
                                             @RequestParam() String titleGEO,
                                             @RequestParam() String displayText,
                                             @RequestParam() String displayTextGEO,
                                             @RequestParam() String text,
                                             @RequestParam() String textGEO,
                                             @RequestParam() String slugUrl,
                                             //@RequestParam("hashtagid")
                                           //@ApiParam(value = "ჰაშთაგების აიდების ლისტი (ასეთი ჰაშთაგი უნდა არსებობდეს ბაზაში)", required = true)
                                                   //ArrayList<Long> hashtagid,
                                             @RequestParam(defaultValue = "false",required = false)
                                           @ApiParam(value = "არის თუარა პრომო გვერდი", required = true)
                                                   boolean prom,
                                             @RequestParam(defaultValue = "0",required = false)
                                           @ApiParam(value = "გასაზიარებელი ფეიჯი დეფაულტად არის 0", defaultValue = "0")
                                                   int share,
                                             @RequestParam(defaultValue = "0",required = false)
                                           @ApiParam(value = "სორტირება დეფაულტად 0 ანუ მასზე იმოქმედებს თარიღით სორტი", required = true,defaultValue = "0")
                                                       int sort,
                                             @RequestParam MultipartFile file) throws IOException {

        if (sort <= 0) sort = Integer.MAX_VALUE;
        //Set<Hashtag> hashtags = new HashSet<>();
        //for (Long tag : hashtagid) hashtagRepo.findById(tag).ifPresent(hashtags::add);

        HashMap<String, String> paths = newsService.uploadNewsImage(file);

        final Car[] car = {null};
        if (null != carid)
            carRepo.findById(carid).ifPresent(car1 -> car[0] =car1);


        News news = new News(title,titleGEO,displayText,displayTextGEO,text,textGEO,paths.get("image"),paths.get("thumbnail"),prom, share,sort,car[0]);
        String slug = newsService.makeSlug(slugUrl);
        if (slug.equals("0"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("slugURL შეიცავს არალათინურ ასოებს");
        if (newsRepo.findBySlug(slug).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ასეთი slugURL უკვე არსებობს");
        }


        news.setSlugUrl(slug);

        newsRepo.save(news);

        return ResponseEntity.ok(carBuildService.jsonFilter(JSON_FILTER_NAME,news));


    }

    // 1 is success
    // 0 is not successful
    @PostMapping("/admin/news/remove/{id}")
    public ResponseEntity<String> removeNews(@PathVariable Long id) {

        return ResponseEntity.ok(newsService.deleteNews(id));

    }

    // unda movides ?page=0 parametrit
    @GetMapping(path = "/api/news/findall",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<String> getNewsList(
            @RequestParam(value = "page")
            @ApiParam(value = "პაგინაცია ფეიჯის ნომერი საწყისი 0", required = true, defaultValue = "0") int page,
            @RequestHeader(value = "accept-language", defaultValue = "en") String lang,
            @RequestParam(value = "popular",required = false,defaultValue = "0")
            @ApiParam(value = "სორტი პოპულარული ნიუსების მიხედვით, მოვიდეს 1", defaultValue = "0") int popular) throws JsonProcessingException {

        Pageable firstPageWithTwoElements = PageRequest.of(page, pageSize);

        List<News> news = popular == 0 ?
                newsRepo.findAllNews(firstPageWithTwoElements) :
                newsRepo.findAllByViews(firstPageWithTwoElements);

        if (lang.equals("ka")) newsService.translateNews(news);
        return ResponseEntity.ok(carBuildService.jsonExcludeFilter(JSON_FILTER_NAME,EXCLUDED_FIELDS,news));
    }

    @GetMapping(path = "/admin/news/findall",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<String> getNewsListAdmin(
            @RequestParam(value = "page",defaultValue = "0")
            @ApiParam(value = "პაგინაცია ფეიჯის ნომერი საწყისი 0", required = true, defaultValue = "0") int page) throws JsonProcessingException {

        Pageable firstPageWithTwoElements = PageRequest.of(page, pageSize);
        List<News> news = newsRepo.findAllNewsAdmin(firstPageWithTwoElements);

        return ResponseEntity.ok(carBuildService.jsonFilter(JSON_FILTER_NAME,news));
    }

    @GetMapping(path = "/api/news/findallshared",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<String> getSharedNewsList(
            @RequestParam(value = "page")
            @ApiParam(value = "პაგინაცია ფეიჯის ნომერი საწყისი 0", required = true, defaultValue = "0") int page,
            @RequestHeader(value = "accept-language", defaultValue = "en") String lang) throws JsonProcessingException {

        Pageable firstPageWithTwoElements = PageRequest.of(page, pageSize);

        List<News> news = newsRepo.findAllSharedNews(firstPageWithTwoElements);

        if (lang.equals("ka")) newsService.translateNews(news);
        return ResponseEntity.ok(carBuildService.jsonExcludeFilter(JSON_FILTER_NAME,EXCLUDED_FIELDS,news));
    }


    @GetMapping(path = "/api/news/get/{slugUrl}",produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<String> getSingleNews(@PathVariable() String slugUrl,
                                                @RequestHeader(value = "accept-language", defaultValue = "en") String lang) throws JsonProcessingException {
        Optional<News> news = newsRepo.findBySlug(slugUrl);
        ObjectMapper objectMapper = new ObjectMapper();
        if (news.isPresent()) {
            news.get().setViews(news.get().getViews() + 1);
            newsRepo.save(news.get());

            if (lang.equals("ka")) newsService.translateNews(news.get());
            String str = carBuildService.jsonExcludeFilter(JSON_FILTER_NAME,EXCLUDED_FIELDS,news.get());
            ObjectNode objNode = (ObjectNode) objectMapper.readTree(str);
            if(news.get().getCar() != null) {
                objNode.put("carId", news.get().getCar().getId());
            }
            List<NewsGallery> galleryList = newsGalleryRepo.findAllByBlogId(news.get().getId());
            System.out.println(galleryList + " +_+_");
            if (galleryList.size() > 0) {
                ArrayNode arrayNode = objectMapper.valueToTree(galleryList);
                objNode.putArray("gallery").addAll(arrayNode);
            }




            return ResponseEntity.ok(objectMapper.writeValueAsString(objNode));

        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ნიუსი არ მოიძებნა");

    }


    @PostMapping("/admin/news/update/{id}")
    public ResponseEntity<String> updateNews(@PathVariable Long id,
                                             @RequestParam(required = false) Long carid,
                                             @ApiParam(value = "უნდა შეიცავდეს მხოლოდ ლათინურ ასოებს",required = true)
                                             @RequestParam() String title,
                                             @RequestParam() String titleGEO,
                                             @RequestParam() String displayText,
                                             @RequestParam() String displayTextGEO,
                                             @RequestParam() String text,
                                             @RequestParam() String textGEO,
                                             @RequestParam() String slugUrl,
                                             //@RequestParam(value = "hashtagid")
                                           //@ApiParam(value = "ჰაშთაგების აიდების ლისტი", required = true)
                                                   //ArrayList<Long> hashtagid,
                                             @RequestParam(defaultValue = "false")
                                           @ApiParam(value = "არის თუარა პრომო გვერდი", required = true)
                                                   boolean prom,
                                             @RequestParam()
                                           @ApiParam(value = "გასაზიარებელი ფეიჯი", required = true) int share,
                                             @RequestParam()
                                           @ApiParam(value = "სორტირება", required = true)
                                                       int sort,
                                             @RequestParam(required = false, value = "file") MultipartFile file) throws JsonProcessingException {


        //Set<Hashtag> hashtags = new HashSet<>();


        //for (Long tagId : hashtagid) hashtagRepo.findById(tagId).ifPresent(hashtags::add);


        News updatedNews = new News(title, titleGEO, displayText, displayTextGEO, text, textGEO, prom, share,sort,slugUrl);
        return newsService.newsUpdate(id, updatedNews, file, carid);
    }
}
