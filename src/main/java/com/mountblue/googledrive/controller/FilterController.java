package com.mountblue.googledrive.controller;


import com.mountblue.googledrive.entity.File;
import com.mountblue.googledrive.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
public class FilterController {

    @Autowired
    private FileService fileService;

    @GetMapping("/filter")
    public String filter( @RequestParam(name = "minSize", required = false) Long minSize,
                          @RequestParam(name = "maxSize", required = false) Long maxSize,
                          @RequestParam(name = "fileName", required = false) String fileName,
                          @RequestParam(name = "fileType", required = false) String fileType,
                         Model model){
        if(fileType!=null && fileType.trim().equals("")){
            fileType = null;
        }
        List<File> filteredFiles = fileService.filterFiles(minSize, maxSize, fileName, fileType);
        model.addAttribute("files",filteredFiles);
        System.out.println(filteredFiles);
        return "filtered-files";
    }
}
