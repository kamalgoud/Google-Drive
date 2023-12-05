package com.mountblue.googledrive.repository;

import com.mountblue.googledrive.entity.ParentFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParentFolderRepository extends JpaRepository<ParentFolder,Long> {
    public ParentFolder findByName(String name);

    List<ParentFolder> findByUserEmail(String userEmail);

}
