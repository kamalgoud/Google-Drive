package com.mountblue.googledrive.repository;

import com.mountblue.googledrive.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder,Long> {

}
