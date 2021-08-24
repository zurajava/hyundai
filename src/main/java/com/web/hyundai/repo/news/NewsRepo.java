package com.web.hyundai.repo.news;

import com.web.hyundai.model.news.News;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface NewsRepo extends JpaRepository<News,Long> {

    @Query(value = "select * from news where share=0 and prom=true order by sort asc,date desc",nativeQuery = true)
    List<News> findAllPromo();

    @Query(value = "select * from news where share=0 order by views desc,date desc",nativeQuery = true)
    List<News> findAllByViews(Pageable pageable);

    @Query(value = "select * from (select * from news where  upper(title) like upper(concat('%', ?1,'%')) or upper(text) like upper(concat('%', ?2,'%')) or upper(titlegeo) like upper(concat('%', ?3,'%')) or upper(textgeo) like upper(concat('%', ?4,'%'))) where share = 0",nativeQuery = true)
    List<News> findAllBySearch
            (String title,String text,String titleGEO,String textGEO);

    @Query(value = "select * from news where share=0 and prom=false order by sort asc,date desc",nativeQuery = true)
    List<News> findAllNews(Pageable pageable);

    @Query(value = "select * from news order by sort asc,date desc",nativeQuery = true)
    List<News> findAllNewsAdmin(Pageable pageable);

    @Query(value = "select * from news where share=0 and car_id=:id order by sort asc,date desc",nativeQuery = true)
    List<News> findAllNewsByCarId(Pageable pageable,@Param("id")Long id);

    @Query(value = "select * from news where share=1 order by date desc",nativeQuery = true)
    List<News> findAllSharedNews(Pageable pageable);

    @Query(value = "select * from news where slug_url=:slug",nativeQuery = true)
    Optional<News> findBySlug(String slug);


}


