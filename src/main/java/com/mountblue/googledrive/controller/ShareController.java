package com.mountblue.googledrive.controller;

import com.mountblue.googledrive.entity.File;
import com.mountblue.googledrive.entity.ParentFolder;
import com.mountblue.googledrive.entity.Users;
import com.mountblue.googledrive.service.FileService;
import com.mountblue.googledrive.service.ParentFolderService;
import com.mountblue.googledrive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ShareController {

    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;
    @Autowired
    private ParentFolderService parentFolderService;


    @PostMapping("/shareFile")
    public String shareFile(@RequestParam("fileId")String id,@RequestParam("email")String email,Principal principal){
        System.out.println(id);
        Long fileId = Long.parseLong(id);
        File file = fileService.getFileById(fileId);
        File newFile = new File();
        newFile.setFileName(file.getFileName());
        newFile.setFileType(file.getFileType());
        newFile.setLink(file.getLink());
        newFile.setSize(file.getSize());
        newFile.setUploadDate(file.getUploadDate());
        Users user = file.getUser();
        if(user!=null && user.getEmail().equals(email)){
            return "same-email";
        }
        if(userService.getUserByEmail(email)==null){
            return "invalid-user";
        }
        if(user==null){
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
            Map<String, Object> userAttributes = oauthToken.getPrincipal().getAttributes();
            String userEmail = (String) userAttributes.get("email");
            Users newUser = userService.getUserByEmail(userEmail);
            file.setUser(newUser);
            fileService.save(file);
        }
        Users fileSharedUser = userService.getUserByEmail(email);
        ParentFolder sharedParentFolder = parentFolderService.getParentFolderByName("Shared With Me",fileSharedUser);
        if(sharedParentFolder.getFiles()==null){
            List<File> sharedFiles = new ArrayList<>();
            sharedFiles.add(newFile);
            sharedParentFolder.setFiles(sharedFiles);
            parentFolderService.save(sharedParentFolder);
            userService.saveUser(fileSharedUser);
        }
        else{
            sharedParentFolder.getFiles().add(newFile);
            parentFolderService.save(sharedParentFolder);
            userService.saveUser(fileSharedUser);
        }
        return "redirect:/";
    }

    @GetMapping("Shared With Me")
    public String sharedWithMe(Principal principal, Model model){
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;

        // Retrieve user attributes from the OAuth2 token
        Map<String, Object> userAttributes = oauthToken.getPrincipal().getAttributes();

        String userEmail = (String) userAttributes.get("email");

        Users user = userService.getUserByEmail(userEmail);

        ParentFolder sharedParentFolder = parentFolderService.getParentFolderByName("Shared With Me",user);
        List<File> files = sharedParentFolder.getFiles();
        model.addAttribute("files",files);
        return "files";
    }
}