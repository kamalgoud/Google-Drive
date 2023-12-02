package com.mountblue.googledrive.service;

import com.mountblue.googledrive.entity.File;
import com.mountblue.googledrive.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository){
        this.fileRepository=fileRepository;
    }

    public void saveFile(File file){
        fileRepository.save(file);
    }
}
