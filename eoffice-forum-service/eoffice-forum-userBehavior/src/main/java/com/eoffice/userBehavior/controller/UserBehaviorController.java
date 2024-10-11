package com.eoffice.userBehavior.controller;
import com.eoffice.common.advice.Result;
import com.eoffice.model.article.pojos.Article;
import com.eoffice.model.category.pojos.Category;
import com.eoffice.model.userBehavior.comments.vo.Comments;
import com.eoffice.userBehavior.service.UserBehaviorService;
import com.eoffice.utils.thread.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return Result.success("点赞成功");
    }

    @PostMapping("/setFavorite")
    public Result<String> setFavoriteArticle(@RequestParam("articleId") Integer articleId) {
        userBehaviorService.setFavoriteArticle(articleId);
        return Result.success("收藏成功");
    }
    @PostMapping("/setView")
    public Result<String> setViewArticle(@RequestParam("articleId") Integer articleId) {
        userBehaviorService.setViewArticle(articleId);
        return Result.success("");
    }

    @PostMapping("/setComment")
    public Result<String> setCommentArticle(@RequestBody Comments comments) {
        userBehaviorService.setCommentArticle(comments);
        return Result.success("评论成功");
    }

    @DeleteMapping("/deleteLike")
    public Result<String> deleteLikeArticle(@RequestParam Integer articleId){
        Integer userId = ThreadLocalUtil.getUser("id");
        userBehaviorService.deleteLikeByUserIdAndArticleId(userId,articleId);
        return Result.success("取消点赞");
    }

    @DeleteMapping("/deleteFavorite")
    public Result<String> deleteFavoriteArticle(@RequestParam Integer articleId){
        Integer userId = ThreadLocalUtil.getUser("id");
        userBehaviorService.deleteFavoriteByUserIdAndArticleId(userId,articleId);
        return Result.success("取消收藏");
    }

    @DeleteMapping("/deleteComment")
    public Result<String> deleteCommentArticle(@RequestParam String id, @RequestParam String userId){
        Integer nowUserId = ThreadLocalUtil.getUser("id");

        if (!userId.equals(String.valueOf(nowUserId))) {
            // 如果前端传来的userId和当前登录用户的id不相同，则返回删除失败的结果
            return Result.error("删除失败，用户身份验证失败");
        }
        // 如果userId和当前登录用户id相同，继续执行删除操作
        userBehaviorService.deleteCommentById(id);
        return Result.success("删除成功");
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

    //通过文章id获取当前文章评论
    @GetMapping("/commentDetail")
    public Result<List<Comments>> allCommentDetail(Integer articleId){
        List<Comments> commentAll = userBehaviorService.findCommentByArticleId(articleId);
        return Result.success(commentAll);
    }


}
