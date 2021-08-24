package com.web.hyundai.repo.usedcars.spec;

import com.web.hyundai.model.usedcars.UsedCar;
import com.web.hyundai.model.usedcars.UsedCarModel;
import com.web.hyundai.repo.usedcars.UsedCarModelRepo;
import com.web.hyundai.repo.usedcars.UsedCarRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class SpecBuilder {

    @Autowired
    UsedCarModelRepo usedCarModelRepo;


    public  Specification<UsedCar> equalFilter(String field, Long modelid) {



        return (Specification<UsedCar>) (root, query, criteriaBuilder) -> {
            //if (var == null || var.isEmpty()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            if (modelid == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); //no filtering
            Optional<UsedCarModel> model = usedCarModelRepo.findById(modelid);
            if (model.isEmpty()) return criteriaBuilder.disjunction(); //no model = emptiness


            return criteriaBuilder.equal(root.get(field),modelid);


        };

    }

    public  Specification<UsedCar> betweenFilter(String field, Integer first, Integer second) {

        return (Specification<UsedCar>) (root, query, criteriaBuilder) -> {
            if (first == null && second == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); //no filtering


            if (first != null && second == null) return criteriaBuilder.equal(root.get(field),first);
            if (first == null) return criteriaBuilder.equal(root.get(field),second);




            return criteriaBuilder.between(root.get(field), first, second);
        };
        }



    public  Specification<UsedCar> colorFilter(List<String> colors) {

        System.out.println(colors);

        return (Specification<UsedCar>) (root, query, criteriaBuilder) -> {
            if (colors == null || colors.size() < 1) return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); //no filtering



            final Path<Object> group = root.get("extColor");



            return group.in(colors);
        };
    }




    }




