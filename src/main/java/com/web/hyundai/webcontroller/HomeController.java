//package com.web.hyundai.webcontroller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//@Controller
//public class HomeController {
//
//
//    @GetMapping("/home")
//    public String home(HttpServletRequest request, HttpSession httpSession){
//        Object lang = httpSession.getAttribute("language");
//        if (null != lang && lang.equals("en")) return "home";
//        if (null == lang) httpSession.setAttribute("language","ge");
//        return "homeGEO";
//    }
//
//
//    @GetMapping("/about")
//    public String about(HttpServletRequest request, HttpSession httpSession){
//        Object lang = httpSession.getAttribute("language");
//        if (null != lang && lang.equals("en")) return "about";
//        if (null == lang) httpSession.setAttribute("language","ge");
//        return "aboutGEO";
//    }
//
//
//
//
//
//}
