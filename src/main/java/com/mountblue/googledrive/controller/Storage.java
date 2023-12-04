package com.mountblue.googledrive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Storage {

    @GetMapping("/Storage")
    public String storage(){
        return "home";
    }
}
