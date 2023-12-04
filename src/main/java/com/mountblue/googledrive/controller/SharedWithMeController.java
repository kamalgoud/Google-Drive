package com.mountblue.googledrive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SharedWithMeController {

    @GetMapping("/Shared with Me")
    public String sharedWithMe(){

        return "home";
    }
}
