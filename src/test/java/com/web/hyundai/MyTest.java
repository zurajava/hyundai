package com.web.hyundai;

import com.web.hyundai.model.Path;
import com.web.hyundai.model.usedcars.UsedCarPhoto;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;


public class MyTest {




    @Test
    void test(){

        String path = Path.getDir() + Path.home;

        System.out.println(path);

        System.out.println(path.replaceAll("\\\\\\\\","\\\\"));
    }











}
