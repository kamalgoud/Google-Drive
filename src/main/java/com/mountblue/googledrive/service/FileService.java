package com.mountblue.googledrive.service;

import com.mountblue.googledrive.entity.File;
import com.mountblue.googledrive.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.attribute.FileAttribute;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    private FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository){
        this.fileRepository=fileRepository;
    }

    public File uploadFile(MultipartFile file) throws IOException {
        File savefile = new File();

        if (!file.isEmpty()) {
            savefile.setFileName(file.getOriginalFilename());
            savefile.setFileType(file.getContentType());
            savefile.setContent(file.getBytes());
            savefile.setSize(file.getSize());
            savefile.setUploadDate(new Date());
        } else {
            throw new IllegalArgumentException("Uploaded file is empty");
        }
        return fileRepository.save(savefile);
    }

    public List<File> allFiles(){
        return fileRepository.findAll();
    }

    public  File getFileById(Long id){
        return fileRepository.findById(id).get();
    }

    public InputStream getFileInputStream(File file){
        byte[] fileContent = file.getContent();
        return new ByteArrayInputStream(fileContent);
    }

    public void deleteFileById(Long fileId){
        fileRepository.deleteById(fileId);
    }

    public void save(File file){
        fileRepository.save(file);
    }

    public List<File> getAllFilesInOrder(Sort sort){
        return fileRepository.findAll(sort);
    }

    public List<File> searchFile(String search) {
        return fileRepository.findAllByFileNameContaining(search);
    }

    public List<File> getFilesByUserEmail(String userEmail) {
        return fileRepository.findByUserEmail(userEmail);
    }
}
