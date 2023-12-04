package com.mountblue.googledrive.repository;

import com.mountblue.googledrive.entity.ParentFolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentFolderRepository extends JpaRepository<ParentFolder,Long> {
    public ParentFolder findByName(String name);
}
