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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TrashController {

    @Autowired
    private FileService fileService;
    @Autowired
    private FolderService folderService;
    @Autowired
    private ParentFolderService parentFolderService;

    @GetMapping("/trash")
    public String openTrash(Model model){
        List<ParentFolder> parentFolders = parentFolderService.getAllParentFolders();
        ParentFolder parentFolder = parentFolderService.getParentFolderByName("trash");

        List<Folder> folders = parentFolder.getFolders();
        List<File> files = parentFolder.getFiles();

        model.addAttribute("parentFolderName","My Drive");
        model.addAttribute("parentFolders",parentFolders);
        model.addAttribute("folders",folders);
        model.addAttribute("files",files);

        return "trash";
    }

    @PostMapping("/moveFileToTrash")
    public String trash(@RequestParam("fileId") Long fileId,
                        @RequestParam("parentFolder") String parentFolderName){
        File file = fileService.getFileById(fileId);
        ParentFolder parentFolder = parentFolderService.getParentFolderByName(parentFolderName);
        parentFolder.getFiles().remove((File) file);

        ParentFolder trashFolder = parentFolderService.getParentFolderByName("trash");
        trashFolder.getFiles().add((File) file);

        parentFolderService.save(parentFolder);
        parentFolderService.save(trashFolder);

        return "redirect:/"+parentFolderName;
    }

    @PostMapping("/moveFileOutOfTrash")
    public String unTrash(@RequestParam("fileId") Long fileId,
                          @RequestParam("parentFolder") String parentFolderName){
        File file = fileService.getFileById(fileId);
        ParentFolder parentFolder = parentFolderService.getParentFolderByName(parentFolderName);
        parentFolder.getFiles().remove((File) file);

        ParentFolder trashFolder = parentFolderService.getParentFolderByName("My Drive");
        trashFolder.getFiles().add((File) file);

        parentFolderService.save(parentFolder);
        parentFolderService.save(trashFolder);

        return "redirect:/"+parentFolderName;
    }

    @PostMapping("/moveFolderToTrash")
    public String moveFolderToTrash(@RequestParam("fileId") Long folderId,
                                    @RequestParam("parentFolder") String parentFolderName){
        Folder folder = folderService.getFolderById(folderId);
        ParentFolder parentFolder = parentFolderService.getParentFolderByName(parentFolderName);
        parentFolder.getFolders().remove(folder);

        ParentFolder trashFolder = parentFolderService.getParentFolderByName("trash");
        trashFolder.getFolders().add(folder);

        parentFolderService.save(parentFolder);
        parentFolderService.save(trashFolder);

        return "redirect:/"+parentFolderName;
    }

    @PostMapping("/moveFolderOutOfTrash")
    public String moveFolderOutOfTrash(@RequestParam("fileId") Long folderId,
                                       @RequestParam("parentFolder") String parentFolderName){
        Folder folder = folderService.getFolderById(folderId);
        ParentFolder parentFolder = parentFolderService.getParentFolderByName(parentFolderName);
        parentFolder.getFolders().remove(folder);

        ParentFolder trashFolder = parentFolderService.getParentFolderByName("My Drive");
        trashFolder.getFolders().add(folder);

        parentFolderService.save(parentFolder);
        parentFolderService.save(trashFolder);

        return "redirect:/"+parentFolderName;
    }


}