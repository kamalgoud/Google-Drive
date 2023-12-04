package com.mountblue.googledrive.controller;

import com.mountblue.googledrive.entity.File;
import com.mountblue.googledrive.entity.Folder;
import com.mountblue.googledrive.entity.ParentFolder;
import com.mountblue.googledrive.service.ParentFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Iterator;
import java.util.List;

@Controller
public class StarredController {
    @Autowired
    private ParentFolderService parentFolderService;

    @GetMapping("/Starred")
    public String starred(Model model){
        List<ParentFolder> parentFolders = parentFolderService.getAllParentFolders();
        ParentFolder parentFolder = parentFolderService.getParentFolderByName("Starred");
        List<Folder> folders = parentFolder.getFolders();
        List<File> files= parentFolder.getFiles();

        Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()) {
            File file = iterator.next();
            if (!file.isStarred()) {
                iterator.remove();  // Safe removal using Iterator
            }
        }

        Iterator<Folder> iteratorFolder = folders.iterator();
        while (iteratorFolder.hasNext()) {
            Folder folder = iteratorFolder.next();
            if (!folder.isStarred()) {
                iteratorFolder.remove();  // Safe removal using Iterator
            }
        }

        model.addAttribute("parentFolderName","My Drive");
        model.addAttribute("parentFolders",parentFolders);
        model.addAttribute("folders",folders);
        model.addAttribute("files",files);
        return "starred";
    }
}
