package com.mountblue.googledrive.controller;

import com.mountblue.googledrive.entity.File;
import com.mountblue.googledrive.entity.Folder;
import com.mountblue.googledrive.entity.ParentFolder;
import com.mountblue.googledrive.service.FileService;
import com.mountblue.googledrive.service.ParentFolderService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class FileController {

    private FileService fileService;
    @Autowired
    private ParentFolderService parentFolderService;

    @Autowired
    public FileController(FileService fileService){
        this.fileService=fileService;
    }



    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("parentFolder") String parentFolderName,
                                   Model model) {
        try {
            File newFile = fileService.uploadFile(file);

            ParentFolder parentFolder = parentFolderService.getParentFolderByName(parentFolderName);
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
                             @RequestParam("parentFolder") String parentFolderName){

        File file = fileService.getFileById(fileId);
        ParentFolder parentFolder = parentFolderService.getParentFolderByName(parentFolderName);
        parentFolder.getFiles().remove((File) file);
        fileService.deleteFileById(fileId);
        parentFolderService.save(parentFolder);

        return "redirect:/"+parentFolderName;
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
                             Model model) {

        File file = fileService.getFileById(fileId);
        ParentFolder parentFolder = parentFolderService.getParentFolderByName("Starred");
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
