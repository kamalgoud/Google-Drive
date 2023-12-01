package com.mountblue.googledrive.entity;

import jakarta.persistence.Entity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Folder {
    private Long id;
    private String folderName;
    @CreationTimestamp
    private Date createdAt;

    public Folder() {
    }

    public Folder(Long id, String folderName, Date createdAt) {
        this.id = id;
        this.folderName = folderName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
