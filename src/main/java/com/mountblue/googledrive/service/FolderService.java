package com.mountblue.googledrive.service;

import com.mountblue.googledrive.entity.File;
import com.mountblue.googledrive.entity.Folder;
import com.mountblue.googledrive.repository.FileRepository;
import com.mountblue.googledrive.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class FolderService {

    private FolderRepository folderRepository;

    private FileRepository fileRepository;

    @Autowired
    public FolderService(FolderRepository folderRepository,FileRepository fileRepository){
        this.folderRepository=folderRepository;
        this.fileRepository=fileRepository;
    }

    public List<Folder> getAllFolders() {
        return folderRepository.findAll();
    }

    public Folder createFolder(String folderName, List<MultipartFile> files) throws IOException {
        Folder folder = new Folder();
        folder.setFolderName(folderName);

        for (MultipartFile file : files) {
            File fileEntity = new File();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setFileType(file.getContentType());
            fileEntity.setContent(file.getBytes());
            fileEntity.setFolder(folder);

            folder.getFiles().add(fileEntity);
        }

        return folderRepository.save(folder);
    }

    public List<File> getFilesInFolder(Long folderId) {
        return fileRepository.findByFolderId(folderId);
    }

    public String getFolderNameFromFilename(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return filename.substring(0, lastDotIndex);
        } else {
            return filename;
        }
    }

}
