package com.mountblue.googledrive.repository;

import com.mountblue.googledrive.entity.File;
import jakarta.persistence.TemporalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface FileRepository extends JpaRepository<File,Long> {

    List<File> findByFolderId(Long folderId);
    List<File> findAllByFileNameContaining(String search);


    List<File> findByUserEmail(String userEmail);

    @Query("SELECT f FROM File f " +
            "WHERE (:minSize IS NULL OR f.size >= :minSize) " +
            "AND (:maxSize IS NULL OR f.size <= :maxSize) " +
            "AND (:fileName IS NULL OR LOWER(f.fileName) LIKE LOWER(CONCAT('%', :fileName, '%'))) " +
            "AND (:fileType IS NULL OR f.fileType = :fileType)")
    List<File> findFilteredFiles(
            @Param("minSize") Long minSize,
            @Param("maxSize") Long maxSize,
            @Param("fileName") String fileName,
            @Param("fileType") String fileType
    );

    @Query("SELECT f.fileType from File f")
    public Set<String> findAllFileTypes();

}
