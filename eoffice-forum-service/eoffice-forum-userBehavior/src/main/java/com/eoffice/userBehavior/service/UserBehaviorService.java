package com.eoffice.userBehavior.service;
import com.eoffice.model.userBehavior.comments.vo.Comments;


import java.util.List;
import java.util.Map;

public interface UserBehaviorService {
    void setLikeArticle(Integer articleId);
    void setFavoriteArticle(Integer articleId);
    void setViewArticle(Integer articleId);
    void setCommentArticle(Comments comments);

    Map<String, Integer> getArticleCounts(Integer articleId);



    void deleteLikeByUserIdAndArticleId(Integer userId,Integer articleId);

    void deleteFavoriteByUserIdAndArticleId(Integer userId,Integer articleId);

    void deleteCommentById( Integer id);


    //获取用户是否点赞收藏
    Map<String, Integer> getArticleBehavior(Integer articleId);


    List<Comments> findCommentByArticleId(Integer articleId);

}