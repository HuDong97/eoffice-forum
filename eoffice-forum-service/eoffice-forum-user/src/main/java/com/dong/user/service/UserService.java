package com.dong.user.service;


import com.dong.model.user.pojos.User;

public interface UserService {

    //通过输入邮箱查询用户
    User findByEmail(String email);

    //通过用户名查询用户
    User findByUserName(String username);

    //用户注册，向数据库插入用户名 密码和邮箱
    void register(String username, String password, String email);



    //更新用户昵称
    void updateNickname(String nickname);
    //更新用户头像
    void updateAvatar(String avatarUrl);

    //更新用户密码
    void updatePwd(String newPwd);

    //通过用户名查找邮箱
    String findEmailByUserName(String username);

    //通过用户名更新邮箱
    void updateEmail(String newEmail, String username);
}
