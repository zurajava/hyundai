package com.web.hyundai.repo.news;

import com.web.hyundai.model.news.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepo extends JpaRepository<Hashtag,Long> {
}
