package com.mountblue.googledrive.controller;

import com.mountblue.googledrive.entity.File;
import com.mountblue.googledrive.service.FolderService;
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
public class FolderController {

    private FolderService folderService;
    @Autowired
    public FolderController(FolderService folderService){
        this.folderService=folderService;
    }

    @PostMapping("/uploadFolder")
    public String uploadFolder(@RequestParam("files") List<MultipartFile> files) {
        try {
            String folderName = folderService.getFolderNameFromFilename(files.get(0).getOriginalFilename());

            folderService.createFolder(folderName, files);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/folder/{id}")
    public String showFilesInFolder(@PathVariable Long id, Model model) {
        List<File> files = folderService.getFilesInFolder(id);
        model.addAttribute("files", files);
        return "files";
    }
}
