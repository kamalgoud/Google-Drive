package com.mountblue.googledrive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrashController {

    @GetMapping("/Trash")
    public String trash(){
        return "home";
    }
}
