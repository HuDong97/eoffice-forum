package com.eoffice.user.mapper;


import com.eoffice.model.user.pojos.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper {

    //通过输入邮箱查询用户
    //@Select("select * from user where email=#{email}")
    User findByEmail(String email);

    //通过用户名查询用户
    //@Select("select * from user where username=#{username}")
    User findByUserName(String name);

    //向数据库插入用户名 密码和邮箱等创建时间和更新时间为当前时间
    //@Insert("insert into user(username,password,email,create_time,update_time)" + " values(#{username},#{password},#{email},now(),now())")
    void add(String username, String password, String email);

    //更新用户昵称
    //@Update("update user set nickname=#{nickname},update_time=now() where id=#{userId}")
    void updateNickname(String nickname, Integer userId);


    //更新用户头像
    //@Update("update user set user_pic=#{avatarUrl},update_time=now() where id=#{userId}")
    void updateAvatar(String avatarUrl, Integer userId);

    //更新用户密码
    //@Update("update user set password=#{md5String},update_time=now() where id=#{userId}")
    void updatePwd(String md5String, Integer userId);

    //通过用户名查找邮箱
    //@Select("select email from user where username=#{username}")
    String findEmailByUserName(String username);

    //更新用户邮箱
    //@Update("update user set email=#{newEmail} where username=#{username}")
    void updateEmail(String newEmail, String username);

    @Select("select nickname from user where id=#{userId}")
    String getNickNameByUserId(Integer userId);

}
