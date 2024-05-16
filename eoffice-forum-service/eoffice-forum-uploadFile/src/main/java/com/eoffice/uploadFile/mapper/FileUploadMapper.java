package com.eoffice.uploadFile.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FileUploadMapper {

    @Update("update user set user_pic=#{fileId},update_time=now() where id=#{id}")
    void updateAvatar(String fileId, Integer id);

}
