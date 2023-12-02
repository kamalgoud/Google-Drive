package com.mountblue.googledrive.controller;

import com.mountblue.googledrive.entity.File;
import com.mountblue.googledrive.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class FileController {

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService){
        this.fileService=fileService;
    }

    @GetMapping("/")
    public String homePage(Model model){
        List<File> files= fileService.allFiles();
        model.addAttribute("files",files);
        return "home2";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            fileService.uploadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

}
