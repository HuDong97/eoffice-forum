package com.eoffice.tools.controller;

import com.eoffice.tools.feign.ChatServiceClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/chat")
public class ToolsController {

    private final ChatServiceClient chatServiceClient;

    public ToolsController(ChatServiceClient chatServiceClient) {
        this.chatServiceClient = chatServiceClient;
    }

    @GetMapping("/invokeChat3")
    public String invokeChat3(@RequestParam("msg") String msg) {
        ResponseEntity<String> response = chatServiceClient.chat3(msg);
        return response.getBody();
    }
}

