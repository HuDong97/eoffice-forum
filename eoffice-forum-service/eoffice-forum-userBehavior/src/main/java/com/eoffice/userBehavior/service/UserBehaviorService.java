package com.eoffice.userBehavior.service;

import com.eoffice.model.userBehavior.comments.vo.Comments;
import com.eoffice.model.userBehavior.favorites.vo.Favorites;
import com.eoffice.model.userBehavior.likes.vo.Likes;
import com.eoffice.model.userBehavior.views.vo.Views;

import java.util.Map;

public interface UserBehaviorService {
    void setLikeArticle(Integer articleId);
    void setFavoriteArticle(Integer articleId);
    void setViewArticle(Integer articleId);
    void setCommentArticle(Comments comments);

    Map<String, Integer> getArticleCounts(Integer articleId);



    void deleteLikeByUserIdAndArticleId(Integer userId,Integer articleId);

    void deleteFavoriteByUserIdAndArticleId(Integer userId,Integer articleId);

    void deleteCommentByUserIdAndArticleId(Integer userId,Integer articleId);


    //获取用户是否点赞收藏
    Map<String, Integer> getArticleBehavior(Integer articleId);

}