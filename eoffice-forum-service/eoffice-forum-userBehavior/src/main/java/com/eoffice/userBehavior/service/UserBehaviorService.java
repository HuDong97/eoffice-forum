package com.eoffice.userBehavior.service;

public interface UserBehaviorService {
    void setLikeArticle(Integer userId, Integer articleId);
    void setFavoriteArticle(Integer userId, Integer articleId);
    void setCommentArticle(Integer userId, Integer articleId, String content);
    void setViewArticle(Integer userId, Integer articleId);
    int getLikesCount(Integer articleId);
    int getFavoritesCount(Integer articleId);
    int getCommentsCount(Integer articleId);
    int getViewsCount(Integer articleId);
    void updateArticleCountsInRedis(Integer articleId);
}
