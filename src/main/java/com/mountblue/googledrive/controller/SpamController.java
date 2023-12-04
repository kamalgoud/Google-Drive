package com.mountblue.googledrive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpamController {

    @GetMapping("/Spam")
    public String spam(){
        return "home";
    }
}
