package com.eoffice.article.feign;


import com.eoffice.model.user.pojos.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "eoffice-forum-user", url = "http://localhost:18081")
public interface UserClient {

    @GetMapping("/user/nickName")

    String getNickNameByUserId(@RequestParam("userId") Integer userId);

}
