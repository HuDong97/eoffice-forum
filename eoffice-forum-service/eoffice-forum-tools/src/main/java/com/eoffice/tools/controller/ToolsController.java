package com.eoffice.tools.controller;

import com.eoffice.tools.feign.ChatServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/chat")
public class ToolsController {


    private final ChatServiceClient chatServiceClient;

    public ToolsController(ChatServiceClient chatServiceClient) {
        this.chatServiceClient = chatServiceClient;
    }

    @GetMapping("/invokeChat3")
    public ResponseEntity<String> invokeChat3(@RequestParam("msg") String msg) {
        return chatServiceClient.chat3(msg);
    }
}

