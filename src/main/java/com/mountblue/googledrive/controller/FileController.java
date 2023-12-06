package com.mountblue.googledrive.controller;

import com.mountblue.googledrive.entity.File;
import com.mountblue.googledrive.entity.Folder;
import com.mountblue.googledrive.entity.ParentFolder;
import com.mountblue.googledrive.entity.Users;
import com.mountblue.googledrive.service.FileService;
import com.mountblue.googledrive.service.ParentFolderService;
import com.mountblue.googledrive.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.*;

@Controller
public class FileController {

    private FileService fileService;
    @Autowired
    private ParentFolderService parentFolderService;
    @Autowired
    private UserService userService;

    @Autowired
    public FileController(FileService fileService){
        this.fileService=fileService;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("parentFolder") String parentFolderName,
                                   Model model, Principal principal) {
        try {

            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
            Map<String, Object> userAttributes = oauthToken.getPrincipal().getAttributes();
            String userEmail = (String) userAttributes.get("email");
            Users user = userService.getUserByEmail(userEmail);

            File newFile = fileService.uploadFile(file);
            newFile.setUser(user);

            ParentFolder parentFolder = parentFolderService.getParentFolderByName(parentFolderName,user);
            System.out.println(parentFolderName+" "+user+" "+userEmail);

            parentFolder.getFiles().add(newFile);
            parentFolderService.save(parentFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/"+parentFolderName;
    }

    @GetMapping("/downloadFile")
    public void downloadFile(@RequestParam("fileId") Long fileId, HttpServletResponse response){
        File file = fileService.getFileById(fileId);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getFileName());

        try (InputStream is = fileService.getFileInputStream(file)) {
            // Stream the file content to the response output stream
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/deleteFile")
    public String deleteFile(@RequestParam("fileId") Long fileId,
                             @RequestParam("parentFolder") String parentFolderName,
                             Principal principal){

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
        // Retrieve user attributes from the OAuth2 token
        Map<String, Object> userAttributes = oauthToken.getPrincipal().getAttributes();
        String userEmail = (String) userAttributes.get("email");
        Users user = userService.getUserByEmail(userEmail);

        File file = fileService.getFileById(fileId);
        ParentFolder parentFolder = parentFolderService.getParentFolderByName(parentFolderName,user);
        parentFolder.getFiles().remove((File) file);
        fileService.deleteFileById(fileId);
        parentFolderService.save(parentFolder);

        return "redirect:/"+parentFolderName;
    }

    @GetMapping("/search")
    public String getSearchResult(@ModelAttribute("search")String search,
                                  Principal principal,
                                  Model model){
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
        Map<String, Object> userAttributes = oauthToken.getPrincipal().getAttributes();
        String userEmail = (String) userAttributes.get("email");
        Users user = userService.getUserByEmail(userEmail);

        List<File> searchedFiles=fileService.searchFile(search);

        Iterator<File> iterator = searchedFiles.iterator();
        while (iterator.hasNext()) {
            File file = iterator.next();
            if (file.getUser() != user) {
                iterator.remove();  // Safe removal using Iterator
            }
        }

        model.addAttribute("files",searchedFiles);

        return "home";
    }

    @GetMapping("/view-file/{fileId}")
    public String viewFileContent(@PathVariable Long fileId, Model model) {
        File file = fileService.getFileById(fileId);
        model.addAttribute("fileContent", fileService.getFileInputStream(file));

        return "file-content";
    }

    @PostMapping("/starFile")
    public String starFile(@RequestParam Long fileId,
                             @RequestParam("parentFolder") String parentFolderName,
                             Principal principal,
                             Model model) {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
        Map<String, Object> userAttributes = oauthToken.getPrincipal().getAttributes();
        String userEmail = (String) userAttributes.get("email");
        Users user = userService.getUserByEmail(userEmail);

        File file = fileService.getFileById(fileId);
        ParentFolder parentFolder = parentFolderService.getParentFolderByName("Starred",user);
        parentFolder.getFiles().add(file);
        parentFolderService.save(parentFolder);
        if(!file.isStarred()){
            file.setStarred(true);
        }
        else{
            file.setStarred(false);
        }
        fileService.save(file);

        return "redirect:/"+parentFolderName;
    }
}
