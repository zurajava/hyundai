package com.web.hyundai.controller.user;


import com.web.hyundai.model.user.User;
import com.web.hyundai.repo.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("/admin/user")
public class UserController {

//    @Autowired
//    UserRepo userRepo;
//
//    @GetMapping("/api/getuser")
//    public boolean getUser(@RequestParam("password") String password){
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//
//        User user = userRepo.findByUsername("admin");
//        System.out.println(user);
//        return passwordEncoder.matches(password,user.getPassword());
//    }


}
