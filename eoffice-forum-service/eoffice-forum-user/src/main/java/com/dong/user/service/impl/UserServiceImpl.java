package com.dong.user.service.impl;

import com.dong.model.user.pojos.User;
import com.dong.user.mapper.UserMapper;
import com.dong.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }
}
