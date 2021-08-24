package com.web.hyundai.repo.translate;

import com.web.hyundai.model.translation.Translate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslateRepo extends JpaRepository<Translate,String> {
}
