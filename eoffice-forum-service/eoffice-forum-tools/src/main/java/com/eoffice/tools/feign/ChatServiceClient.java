package com.eoffice.tools.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "spring-ai-chat", url = "http://localhost:8080")
public interface ChatServiceClient {

    @GetMapping("/ai/chat3")
    ResponseEntity<String> chat3(@RequestParam("msg") String msg);
}
