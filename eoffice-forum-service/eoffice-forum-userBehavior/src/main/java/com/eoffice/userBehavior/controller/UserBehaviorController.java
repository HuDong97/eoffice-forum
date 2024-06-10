package com.eoffice.userBehavior.controller;
import com.eoffice.common.advice.Result;
import com.eoffice.model.userBehavior.comments.vo.Comments;
import com.eoffice.model.userBehavior.favorites.vo.Favorites;
import com.eoffice.model.userBehavior.likes.vo.Likes;
import com.eoffice.model.userBehavior.views.vo.Views;
import com.eoffice.userBehavior.service.UserBehaviorService;
import com.eoffice.utils.thread.ThreadLocalUtil;
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

    @PostMapping("/setLike")
    public Result<String> setLikeArticle(@RequestParam("articleId") Integer articleId) {
        userBehaviorService.setLikeArticle(articleId);
        return Result.success();
    }

    @PostMapping("/setFavorite")
    public Result<String> setFavoriteArticle(@RequestParam("articleId") Integer articleId) {
        userBehaviorService.setFavoriteArticle(articleId);
        return Result.success();
    }
    @PostMapping("/setView")
    public Result<String> setViewArticle(@RequestParam("articleId") Integer articleId) {
        userBehaviorService.setViewArticle(articleId);
        return Result.success();
    }

    @PostMapping("/setComment")
    public Result<String> setCommentArticle(@RequestBody Comments comments) {
        userBehaviorService.setCommentArticle(comments);
        return Result.success();
    }



    @DeleteMapping("/deleteLike")
    public Result<String> deleteLikeArticle(@RequestParam Integer articleId){
        Integer userId = ThreadLocalUtil.getUser("id");
        userBehaviorService.deleteLikeByUserIdAndArticleId(userId,articleId);
        return Result.success();
    }

    @DeleteMapping("/deleteFavorite")
    public Result<String> deleteFavoriteArticle(@RequestParam Integer articleId){
        Integer userId = ThreadLocalUtil.getUser("id");
        userBehaviorService.deleteFavoriteByUserIdAndArticleId(userId,articleId);
        return Result.success();
    }

    @DeleteMapping("/deleteComment")
    public Result<String> deleteCommentArticle(@RequestParam Integer articleId){
        Integer userId = ThreadLocalUtil.getUser("id");
        userBehaviorService.deleteCommentByUserIdAndArticleId(userId,articleId);
        return Result.success();
    }

    @GetMapping("/counts")
    public Result<Map<String, Integer>> getArticleCounts(@RequestParam Integer articleId) {
        Map<String, Integer> counts = userBehaviorService.getArticleCounts(articleId);
        return Result.success(counts);
    }

    //获取用户是否点赞收藏
    @GetMapping("/ArticleBehavior")
    public Result<Map<String, Integer>> getArticleBehavior(@RequestParam Integer articleId) {
        Map<String, Integer> ArticleBehavior = userBehaviorService.getArticleBehavior(articleId);
        return Result.success(ArticleBehavior);
    }

}
