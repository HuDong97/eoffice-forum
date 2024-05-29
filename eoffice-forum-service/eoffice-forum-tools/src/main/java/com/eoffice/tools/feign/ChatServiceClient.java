package com.eoffice.tools.feign;



import com.eoffice.common.advice.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "chat-service", url = "http://localhost:8080")  // 直接使用服务的 URL
public interface ChatServiceClient {
    @GetMapping("/ai/chat3")
    ResponseEntity<String> chat3(@RequestParam("msg") String msg);
}
