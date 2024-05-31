package com.eoffice.userBehavior.controller;

import com.eoffice.userBehavior.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/userBehavior")
public class UserBehaviorController {

    private final UserBehaviorService userBehaviorService;

    @Autowired
    public UserBehaviorController(UserBehaviorService userBehaviorService) {
        this.userBehaviorService = userBehaviorService;
    }

    @PostMapping("/like")
    public void setLikeArticle(@RequestParam Integer userId, @RequestParam Integer articleId) {
        userBehaviorService.setLikeArticle(userId, articleId);
    }

    @PostMapping("/favorite")
    public void setFavoriteArticle(@RequestParam Integer userId, @RequestParam Integer articleId) {
        userBehaviorService.setFavoriteArticle(userId, articleId);
    }

    @PostMapping("/comment")
    public void setCommentArticle(@RequestParam Integer userId, @RequestParam Integer articleId, @RequestParam String content) {
        userBehaviorService.setCommentArticle(userId, articleId, content);
    }

    @PostMapping("/view")
    public void setViewArticle(@RequestParam Integer userId, @RequestParam Integer articleId) {
        userBehaviorService.setViewArticle(userId, articleId);
    }

    @GetMapping("/counts/{articleId}")
    public Map<String, Integer> getArticleCounts(@PathVariable Integer articleId) {
        return userBehaviorService.getArticleCounts(articleId);
    }
}