package com.mountblue.googledrive.controller;

import com.mountblue.googledrive.entity.File;
import com.mountblue.googledrive.entity.Folder;
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
            System.out.println(folderName);
            folderService.createFolder(folderName, files);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/openFolder")
    public String showFilesInFolder(@RequestParam Long folderId, Model model) {
        Folder folder = folderService.getFolderById(folderId);
        List<File> files = folder.getFiles();
        model.addAttribute("files", files);
        return "files";
    }

    @PostMapping("/deleteFolder")
    public String deleteFolder(@RequestParam Long folderId, Model model) {
        folderService.deleteFolderById(folderId);
        return "redirect:/";
    }
}
