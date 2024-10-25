package com.eoffice.user.service.impl;

import com.eoffice.model.user.pojos.User;
import com.eoffice.user.mapper.UserMapper;
import com.eoffice.user.service.UserService;
import com.eoffice.utils.common.Md5Util;
import com.eoffice.utils.thread.ThreadLocalUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    //实现接口方法，通过输入邮箱查询用户
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    //实现接口方法，通过用户名查询用户
    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    @Transactional
    //实现接口方法，用户注册，向数据库插入用户名 密码和邮箱
    public void register(String username, String password, String email) {


        //对用户输入进数据库的密码进行MD5加密
        String md5password = Md5Util.getMD5String(password);
        //添加数据进入数据库
        userMapper.add(username, md5password, email);

    }


    //更新用户昵称
    @Override
    public void updateNickname(String nickname) {

        Integer userId = ThreadLocalUtil.getUser("id");
        userMapper.updateNickname(nickname, userId);
    }

    //实现接口方法，更新用户头像，id从ThreadLocalUtil获取
    @Override
    public void updateAvatar(String avatarUrl) {
        Integer userId = ThreadLocalUtil.getUser("id");
        userMapper.updateAvatar(avatarUrl, userId);

    }

    //实现接口方法，更新用户密码，id从ThreadLocalUtil获取
    @Override
    public void updatePwd(String newPwd) {
        Integer userId = ThreadLocalUtil.getUser("id");
        //对用户输入进数据库的密码进行MD5加密
        userMapper.updatePwd(Md5Util.getMD5String(newPwd), userId);

    }


    //通过用户名查找邮箱
    @Override
    public String findEmailByUserName(String username) {
        return userMapper.findEmailByUserName(username);
    }


    //通过用户名更新邮箱
    @Override
    public void updateEmail(String newEmail, String username) {
        userMapper.updateEmail(newEmail, username);

    }

    @Override
    public String getNickNameByUserId(Integer userId) {


        return userMapper.getNickNameByUserId(userId);
    }


    //通过邮箱重置密码
    @Override
    public void resetPassword( String email,String newPassword) {

        userMapper.resetPassword(Md5Util.getMD5String(newPassword), email);

    }


}
