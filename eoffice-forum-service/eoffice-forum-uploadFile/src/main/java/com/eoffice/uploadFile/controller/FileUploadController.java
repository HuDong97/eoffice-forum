package com.eoffice.uploadFile.controller;


import com.eoffice.common.advice.Result;
import com.eoffice.uploadFile.service.FileUploadService;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/uploadFile")

//文件上传
public class FileUploadController {
    //图片上传
    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/uploadPicture")
    public Result<String> uploadFile(@NotNull @RequestParam("file") MultipartFile multipartFile) throws Exception {
        return fileUploadService.uploadPicture(multipartFile);
    }
}
