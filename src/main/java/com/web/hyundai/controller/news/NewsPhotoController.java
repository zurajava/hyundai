package com.web.hyundai.controller.news;

import com.web.hyundai.model.Path;
import com.web.hyundai.model.news.News;
import com.web.hyundai.model.news.NewsGallery;
import com.web.hyundai.repo.news.NewsGalleryRepo;
import com.web.hyundai.repo.news.NewsRepo;
import com.web.hyundai.service.ImageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin("*")
@Api(tags = "News Gallery")
public class NewsPhotoController {

    @Autowired
    private NewsGalleryRepo newsGalleryRepo;

    @Autowired
    private NewsRepo newsRepo;

    @Autowired
    private ImageService imageService;

    @PostMapping("/admin/news/gallery/create/{newsId}")
    public ResponseEntity<List<NewsGallery>> createImages(@PathVariable Long newsId, @RequestParam() ArrayList<MultipartFile> images){
        Optional<News> news = newsRepo.findById(newsId);
        if (news.isPresent() && images.size() > 0) {
            ArrayList<NewsGallery> newsGalleryArrayList = new ArrayList<>();
            images.forEach(file -> {
                try {
                    NewsGallery newsGallery = new NewsGallery();
                    newsGallery.setNews(news.get());
                    newsGallery.setImage(Path.newsPath() + imageService.uploadImage(file, Path.newsPath()).getFile().getName());
                    newsGalleryArrayList.add(newsGallery);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return ResponseEntity.ok(newsGalleryRepo.saveAll(newsGalleryArrayList));

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonList(new NewsGallery()));
    }

    @PostMapping("/admin/news/gallery/delete/{galleryId}")
    public ResponseEntity<String> deleteImages(@PathVariable Long galleryId){
        Optional<NewsGallery> gallery = newsGalleryRepo.findById(galleryId);
        if (gallery.isPresent()) {


            imageService.deleteFile(gallery.get().getImage());

            newsGalleryRepo.deleteById(galleryId);
            return ResponseEntity.ok("1");

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("0");
    }

    @GetMapping("/admin/news/gallery/findall/{newsId}")
    public ResponseEntity<List<NewsGallery>> findImages(@PathVariable Long newsId){
        Optional<News> news = newsRepo.findById(newsId);
        List<NewsGallery> gallery = newsGalleryRepo.findAllByBlogId(newsId);
        if (news.isPresent()) {
            return ResponseEntity.ok(gallery);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonList(new NewsGallery()));
    }
}
