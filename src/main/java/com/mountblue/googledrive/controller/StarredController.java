package com.mountblue.googledrive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StarredController {

    @GetMapping("/Starred")
    public String starred(){
        return "home";
    }
}
