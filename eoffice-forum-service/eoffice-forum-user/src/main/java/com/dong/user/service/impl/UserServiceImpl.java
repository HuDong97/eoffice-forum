package com.dong.user.service.impl;


import com.dong.model.user.pojos.User;
import com.dong.user.mapper.UserMapper;
import com.dong.user.service.UserService;
import com.dong.utils.common.Md5Util;

import com.dong.utils.thread.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    //实现接口方法，通过输入邮箱查询用户
    public User findByEmail(String email) {
        User userFindByEmail = userMapper.findByEmail(email);
        return userFindByEmail;
    }

    @Override
    //实现接口方法，通过用户名查询用户
    public User findByUserName(String username) {
        User userFindByUserName = userMapper.findByUserName(username);
        return userFindByUserName;
    }

    @Override
    @Transactional
    //给用户注册加入事务，要么全部成功要么全部失败
    //实现接口方法，用户注册，向数据库插入用户名 密码和邮箱
    public void register(String username, String password, String email) {


        //对用户输入进数据库的密码进行MD5加密
        String md5password = Md5Util.getMD5String(password);
        //添加数据进入数据库
        userMapper.add(username, md5password, email);

    }



    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    //实现接口方法，更新用户头像，id从ThreadLocalUtil获取
    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.updateAvatar(avatarUrl, id);

    }

    //实现接口方法，更新用户密码，id从ThreadLocalUtil获取
    @Override
    public void updatePwd(String newPwd) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        //对用户输入进数据库的密码进行MD5加密
        userMapper.updatePwd(Md5Util.getMD5String(newPwd), id);

    }


    //通过用户名查找邮箱
    @Override
    public String findEmailByUserName(String username) {
        String findEmailByUserName = userMapper.findEmailByUserName(username);
        return findEmailByUserName;
    }


    //通过用户名更新邮箱
    @Override
    public void updateEmail(String newEmail, String username) {
        userMapper.updateEmail( newEmail,  username);

    }


}
