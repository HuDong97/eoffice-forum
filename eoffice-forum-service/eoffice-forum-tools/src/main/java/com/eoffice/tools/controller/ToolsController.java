package com.eoffice.tools.controller;

import com.eoffice.tools.feign.ChatServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ToolsController {

    @Autowired
    private ChatServiceClient chatServiceClient;

    @GetMapping("/invokeChat3")
    public String invokeChat3(@RequestParam("msg") String msg) {
        ResponseEntity<String> response = chatServiceClient.chat3(msg);
        return response.getBody();
    }
}