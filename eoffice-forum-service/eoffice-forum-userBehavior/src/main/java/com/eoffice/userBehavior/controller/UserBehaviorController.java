package com.eoffice.userBehavior.controller;
import com.eoffice.common.advice.Result;
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
    public Result<String> setLikeArticle(@RequestParam Integer userId, @RequestParam Integer articleId) {
        userBehaviorService.setLikeArticle(userId, articleId);
        return Result.success();
    }

    @PostMapping("/favorite")
    public Result<String> setFavoriteArticle(@RequestParam Integer userId, @RequestParam Integer articleId) {
        userBehaviorService.setFavoriteArticle(userId, articleId);
        return Result.success();
    }

    @PostMapping("/comment")
    public Result<String> setCommentArticle(@RequestParam Integer userId, @RequestParam Integer articleId, @RequestParam String content) {
        userBehaviorService.setCommentArticle(userId, articleId, content);
        return Result.success();
    }

    @PostMapping("/view")
    public Result<String> setViewArticle(@RequestParam Integer userId, @RequestParam Integer articleId) {
        userBehaviorService.setViewArticle(userId, articleId);
        return Result.success();
    }

    @GetMapping("/counts")
    public Result<Map<String, Integer>> getArticleCounts(@RequestParam Integer articleId) {
        Map<String, Integer> counts = userBehaviorService.getArticleCounts(articleId);
        return Result.success(counts);
    }
}
