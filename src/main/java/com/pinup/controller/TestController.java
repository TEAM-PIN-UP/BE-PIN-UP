package com.pinup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String test() {
        return "success";
    }

    @GetMapping("/login-test")
    public String loginTest(){
        return "success";
    }
}
