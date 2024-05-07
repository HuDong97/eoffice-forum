package com.eoffice.user.feign;

import com.eoffice.api.user.userClient;
import com.eoffice.model.user.pojos.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class UserClientImpl implements userClient {
    @Override
    @GetMapping("/user/{id}")
    public User getUserById(int id) {
        return null;
    }
}
