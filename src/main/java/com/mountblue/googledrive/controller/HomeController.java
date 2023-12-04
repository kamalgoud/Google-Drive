package com.mountblue.googledrive.controller;

import com.mountblue.googledrive.entity.File;
import com.mountblue.googledrive.entity.Folder;
import com.mountblue.googledrive.entity.ParentFolder;
import com.mountblue.googledrive.service.FileService;
import com.mountblue.googledrive.service.FolderService;
import com.mountblue.googledrive.service.ParentFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class HomeController {

    private FileService fileService;
    private FolderService folderService;
    private ParentFolderService parentFolderService;

    @Autowired
    public HomeController(FileService fileService,FolderService folderService,ParentFolderService parentFolderService){
        this.fileService=fileService;
        this.folderService = folderService;
        this.parentFolderService = parentFolderService;
    }

    @GetMapping("/start")
    public String getStarted(){
        return "start";
    }

    @GetMapping({"/", "/My Drive"})
    public String home(Model model){
        List<ParentFolder> parentFolders = parentFolderService.getAllParentFolders();
        ParentFolder parentFolder = parentFolderService.getParentFolderByName("My Drive");
        ParentFolder starredFolder = parentFolderService.getParentFolderByName("Starred");
        List<Folder> folders = parentFolder.getFolders();
        List<File> files= parentFolder.getFiles();

        folders.addAll(starredFolder.getFolders());
        files.addAll(starredFolder.getFiles());

        Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()) {
            File file = iterator.next();
            if (file.getFolder()!=null) {
                iterator.remove();  // Safe removal using Iterator
            }
        }
        model.addAttribute("parentFolderName","My Drive");
        model.addAttribute("parentFolders",parentFolders);
        model.addAttribute("folders",folders);
        model.addAttribute("files",files);
        return "home";
    }
}
