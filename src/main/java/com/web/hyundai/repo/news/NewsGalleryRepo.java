package com.web.hyundai.repo.news;

import com.web.hyundai.model.news.NewsGallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsGalleryRepo extends JpaRepository<NewsGallery,Long> {
    @Query(value = "select * from news_gallery where news_id=:id",nativeQuery = true)
    List<NewsGallery> findAllByBlogId(Long id);
}
