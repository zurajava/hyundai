//package com.web.hyundai.webcontroller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//
//@Controller
//public class TestCont {
//
//    @GetMapping("/api/testo123")
//    public String home(HttpServletRequest request, HttpSession httpSession){
//        Object lang = httpSession.getAttribute("language");
//        if (null != lang && lang.equals("en")) return "test";
//        return "testGEO";
//    }
//
//
//
//    @GetMapping("/api/testo/{id}")
//    public String homeLang(HttpServletRequest request, HttpSession httpSession){
//        httpSession.setAttribute("language","en");
//        return "test";
//    }
//
//
//}
