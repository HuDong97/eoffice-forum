package com.eoffice.uploadFile.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FileUploadMapper {

    @Update("update user set user_pic=#{fileId},update_time=now() where id=#{userId}")
    void updateAvatar(String fileId, Integer userId);

    @Select("select user_pic from user where id=#{userId}")
    String selectAvatarById(Integer userId);
}
