package com.dong.user.service;


import com.dong.model.user.pojos.User;

import java.util.List;

public interface UserService {
    List<User> getUserByUsername(String username);
}