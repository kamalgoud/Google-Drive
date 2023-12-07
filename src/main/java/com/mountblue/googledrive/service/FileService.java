package com.mountblue.googledrive.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.mountblue.googledrive.entity.File;
import com.mountblue.googledrive.entity.Users;
import com.mountblue.googledrive.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FileService {
    private FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository){
        this.fileRepository=fileRepository;
    }

    public java.io.File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        java.io.File tempFile = new java.io.File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    public String getExtension(String fileName) {
//        String extension = fileName.split(".")[1];
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }

    public File uploadFile(MultipartFile file, String fileName) throws IOException {

        java.io.File tempFile = java.io.File.createTempFile("temp", null);
        file.transferTo(tempFile.toPath());

        try (FileInputStream serviceAccount = new FileInputStream("./serviceAccountKey.json")) {
            BlobId blobId = BlobId.of("drive-db-415a1.appspot.com", fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

            Credentials credentials = GoogleCredentials.fromStream(serviceAccount);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            storage.create(blobInfo, Files.readAllBytes(tempFile.toPath()));

            String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/drive-db-415a1/o/%s?alt=media";

            File savefile = new File();
            savefile.setFileName(file.getOriginalFilename());
            savefile.setLink(String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8)));
            savefile.setSize(file.getSize());  // Set the file size
            savefile.setFileType(file.getContentType());

            fileRepository.save(savefile);
            return savefile;
        } catch (IOException e) {
            // Handle the exception (log, throw, or return an error response)
            throw new RuntimeException("Error uploading file to Firebase Storage", e);
        } finally {
            // Delete the temporary file
            tempFile.delete();
        }
    }


    

    public List<File> allFiles(){
        return fileRepository.findAll();
    }

    public  File getFileById(Long id){
        return fileRepository.findById(id).get();
    }

//    public InputStream getFileInputStream(File file){
//        byte[] fileContent = file.getContent();
//        return new ByteArrayInputStream(fileContent);
//    }

    public void deleteFileById(Long fileId){
        fileRepository.deleteById(fileId);
    }

    public void save(File file){
        fileRepository.save(file);
    }

    public List<File> getAllFilesInOrder(Users user){
        return fileRepository.findFilesByUserInSort(user);
    }

    public List<File> searchFile(String search) {
        return fileRepository.findAllByFileNameContainingIgnoreCase(search);
    }

    public List<File> getFilesByUserEmail(String userEmail) {
        return fileRepository.findByUserEmail(userEmail);
    }
    public List<File> filterFiles(Long minSize, Long maxSize, String fileName, String fileType) {
        return fileRepository.findFilteredFiles(minSize, maxSize, fileName, fileType);
    }

    public Set<String> getAllFileTypes(){
        return  fileRepository.findAllFileTypes();
    }
}
