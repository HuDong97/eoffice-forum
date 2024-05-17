package com.eoffice.uploadFile.exception;

import com.eoffice.common.advice.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UploadFileExceptionHandler {
    //文件上传微服务的异常处理器
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e){
        e.printStackTrace();
        return Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "操作失败");
    }
}
