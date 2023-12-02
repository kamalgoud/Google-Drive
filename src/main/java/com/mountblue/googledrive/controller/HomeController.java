package com.mountblue.googledrive.controller;

import com.mountblue.googledrive.entity.File;
import com.mountblue.googledrive.entity.Folder;
import com.mountblue.googledrive.service.FileService;
import com.mountblue.googledrive.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Iterator;
import java.util.List;

@Controller
public class HomeController {

    private FileService fileService;
    private FolderService folderService;

    @Autowired
    public HomeController(FileService fileService,FolderService folderService){
        this.fileService=fileService;
        this.folderService = folderService;
    }

    @GetMapping("/")
    public String home(Model model){
        List<Folder> folders = folderService.getAllFolders();
        List<File> files= fileService.allFiles();

        Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()) {
            File file = iterator.next();
            if (file.getFolder()!=null) {
                iterator.remove();  // Safe removal using Iterator
            }
        }
        model.addAttribute("folders",folders);
        model.addAttribute("files",files);
        return "home";
    }
}
