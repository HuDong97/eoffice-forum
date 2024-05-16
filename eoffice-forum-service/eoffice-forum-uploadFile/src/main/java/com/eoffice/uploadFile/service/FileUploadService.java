package com.eoffice.uploadFile.service;

import com.eoffice.common.advice.Result;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    //图片上传
    Result<String> uploadPicture(MultipartFile multipartFile);
}
