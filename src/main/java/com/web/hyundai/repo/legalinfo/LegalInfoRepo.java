package com.web.hyundai.repo.legalinfo;

import com.web.hyundai.model.legalinfo.LegalInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LegalInfoRepo extends JpaRepository<LegalInfo,Long> {
}
