package com.eoffice.userBehavior.controller;

import com.eoffice.userBehavior.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/likesCount/{articleId}")
    public int getLikesCount(@PathVariable Integer articleId) {
        return userBehaviorService.getLikesCount(articleId);
    }

    @GetMapping("/favoritesCount/{articleId}")
    public int getFavoritesCount(@PathVariable Integer articleId) {
        return userBehaviorService.getFavoritesCount(articleId);
    }

    @GetMapping("/commentsCount/{articleId}")
    public int getCommentsCount(@PathVariable Integer articleId) {
        return userBehaviorService.getCommentsCount(articleId);
    }

    @GetMapping("/viewsCount/{articleId}")
    public int getViewsCount(@PathVariable Integer articleId) {
        return userBehaviorService.getViewsCount(articleId);
    }
}
