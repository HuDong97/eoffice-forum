package com.eoffice.uploadFile.service.impl;

import com.eoffice.common.advice.Result;
import com.eoffice.file.service.MinIOFileStorageService;
import com.eoffice.model.common.enums.FileSizeEnum;
import com.eoffice.uploadFile.mapper.FileUploadMapper;
import com.eoffice.uploadFile.service.FileUploadService;
import com.eoffice.utils.common.MessageValidator;
import com.eoffice.utils.thread.ThreadLocalUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


@Service
@Transactional
public class FileUploadServiceImpl implements FileUploadService {

    private final FileUploadMapper fileUploadMapper;

    private final MinIOFileStorageService minIOFileStorageService;

    public FileUploadServiceImpl(FileUploadMapper fileUploadMapper, MinIOFileStorageService minIOFileStorageService) {
        this.fileUploadMapper = fileUploadMapper;
        this.minIOFileStorageService = minIOFileStorageService;
    }


    //图片上传
    @Override
    public Result<String> uploadPicture(MultipartFile multipartFile) {
        //1.检查参数
        if (multipartFile == null || multipartFile.getSize() == 0) {
            return Result.error("文件不能为空");
        }
        //1.1检查文件大小
        if (multipartFile.getSize() > FileSizeEnum.BIG.getSizeInBytes()) {
            return Result.error("文件大小超过限制，最大允许为500K");
        }

        // 2.上传图片到MinIO中，拼接用户名防止重名

        // 生成一个随机的文件名，去除了其中的横杠
        String fileName = UUID.randomUUID().toString().replace("-", "");

        // 获取上传文件的原始文件名
        String originalFilename = multipartFile.getOriginalFilename();

        // 调用校验工具类处理文件名中的非法字符
        if (originalFilename == null || MessageValidator.isValidOriginalFilename(originalFilename)) {
            return Result.error("文件名只支持数字、中文、英文大小写字母");
        }


        // 获取文件名的后缀部分，例如：".jpg"
        String postfix;
        postfix = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 初始化文件ID
        String fileId;
        try {
            // 调用MinIO文件存储服务的上传图片文件方法，拼接了用户名和后缀
            fileId = minIOFileStorageService.uploadImgFile("", fileName + postfix, multipartFile.getInputStream());
        } catch (IOException e) {
            // 捕获可能的IO异常
            e.printStackTrace();
            return Result.error("文件上传服务器失败");
        }


        //3.保存图片链接到数据库中
        Integer userId = ThreadLocalUtil.getUser("id");
        fileUploadMapper.updateAvatar(fileId, userId);

        //4.返回结果
        // 返回文件ID给前端
        return Result.success(fileId);
    }
}
