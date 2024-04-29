package com.dong.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-server")
public interface UserClient {
/*
    @PutMapping("/users")
    void deductMoney(@RequestParam("pw") String pw, @RequestParam("amount") Integer amount);
*/

}
