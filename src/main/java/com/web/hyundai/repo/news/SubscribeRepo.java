package com.web.hyundai.repo.news;

import com.web.hyundai.model.news.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface SubscribeRepo extends JpaRepository<Subscribe,Long> {

    @Query("select a from Subscribe a where a.date <= :creationDateTime")
    List<Subscribe> findAllWithCreationDateTimeBefore(@Param("creationDateTime") Date creationDateTime);
}
