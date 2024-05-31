package com.eoffice.userBehavior.service;

import java.util.Map;

public interface UserBehaviorService {
    void setLikeArticle(Integer userId, Integer articleId);
    void setFavoriteArticle(Integer userId, Integer articleId);
    void setCommentArticle(Integer userId, Integer articleId, String content);
    void setViewArticle(Integer userId, Integer articleId);
    Map<String, Integer> getArticleCounts(Integer articleId);
    void updateArticleCountsInRedis(Integer articleId);
}
