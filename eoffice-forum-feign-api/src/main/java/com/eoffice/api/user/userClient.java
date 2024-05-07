package com.eoffice.api.user;


import com.eoffice.model.user.pojos.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("eoffice-forum-user")
public interface userClient {
    @GetMapping("/user/{id}")
    User getUserById(@PathVariable("id") int id);
}