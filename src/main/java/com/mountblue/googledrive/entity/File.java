package com.mountblue.googledrive.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class File {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    @Lob
    private byte[] content;
    private Long size;
    private Date uploadDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public File() {
    }

    public File(Long id, String fileName, String fileType, byte[] content, Long size, Date uploadDate) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.content = content;
        this.size = size;
        this.uploadDate = uploadDate;
    }
}
