package com.eoffice.user.controller;


import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileUploadController {

/*    // 设置文件最大允许的大小为100KB
    private static final long MAX_FILE_SIZE_BYTES = 100 * 1024;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        // 检查文件大小
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {

            return Result.error("文件大小超过限制，最大允许为100KB");
        }

        //把文件的内容存储到本地磁盘/阿里云
        String originalFilename = file.getOriginalFilename();
        //保证文件的名字是唯一的,从而防止文件覆盖
        String filename = UUID.randomUUID() +originalFilename.substring(originalFilename.lastIndexOf("."));
        //file.transferTo(new File("C:\\Users\\胡行东\\Desktop\\files\\"+filename));
        String url = AliOssUtil.uploadFile(filename,file.getInputStream());
        return Result.success(url);
    }*/
}
