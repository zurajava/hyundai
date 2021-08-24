package com.web.hyundai.repo.home;

import com.web.hyundai.model.home.Home;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeRepo extends JpaRepository<Home,Long> {

    List<Home> findAllByOrderBySortAsc();
}
