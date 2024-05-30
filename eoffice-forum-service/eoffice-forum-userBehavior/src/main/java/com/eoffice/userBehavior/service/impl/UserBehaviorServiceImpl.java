package com.eoffice.userBehavior.service.impl;

import com.eoffice.userBehavior.mapper.UserBehaviorMapper;
import com.eoffice.userBehavior.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
public class UserBehaviorServiceImpl implements UserBehaviorService {


    private static final long CACHE_EXPIRATION_DAYS = 1;

    @Autowired
    private UserBehaviorMapper userBehaviorMapper;

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;





    @Override
    @Transactional
    public void setLikeArticle(Integer userId, Integer articleId) {
        userBehaviorMapper.insertLike(userId, articleId);
        updateArticleCountsInRedis(articleId);
    }

    @Override
    @Transactional
    public void setFavoriteArticle(Integer userId, Integer articleId) {
        userBehaviorMapper.insertFavorite(userId, articleId);
        updateArticleCountsInRedis(articleId);
    }

    @Override
    @Transactional
    public void setCommentArticle(Integer userId, Integer articleId, String content) {
        userBehaviorMapper.insertComment(userId, articleId, content);
        updateArticleCountsInRedis(articleId);
    }

    @Override
    @Transactional
    public void setViewArticle(Integer userId, Integer articleId) {
        userBehaviorMapper.insertView(userId, articleId);
        updateArticleCountsInRedis(articleId);
    }

    @Override
    public int getLikesCount(Integer articleId) {
        return getArticleCountFromRedis("likesCount", articleId, userBehaviorMapper::selectLikesCount);
    }

    @Override
    public int getFavoritesCount(Integer articleId) {
        return getArticleCountFromRedis("favoritesCount", articleId, userBehaviorMapper::selectFavoritesCount);
    }

    @Override
    public int getCommentsCount(Integer articleId) {
        return getArticleCountFromRedis("commentsCount", articleId, userBehaviorMapper::selectCommentsCount);
    }

    @Override
    public int getViewsCount(Integer articleId) {
        return getArticleCountFromRedis("viewsCount", articleId, userBehaviorMapper::selectViewsCount);
    }

    private int getArticleCountFromRedis(String type, Integer articleId, java.util.function.Function<Integer, Integer> dbQuery) {
        String key = type + ":" + articleId;
        Integer count = redisTemplate.opsForValue().get(key);
        if (count == null) {
            count = dbQuery.apply(articleId);
            redisTemplate.opsForValue().set(key, count, CACHE_EXPIRATION_DAYS, TimeUnit.DAYS);
        }
        return count;
    }

    @Override
    public void updateArticleCountsInRedis(Integer articleId) {
        redisTemplate.opsForValue().set("likesCount:" + articleId, userBehaviorMapper.selectLikesCount(articleId), CACHE_EXPIRATION_DAYS, TimeUnit.DAYS);
        redisTemplate.opsForValue().set("favoritesCount:" + articleId, userBehaviorMapper.selectFavoritesCount(articleId), CACHE_EXPIRATION_DAYS, TimeUnit.DAYS);
        redisTemplate.opsForValue().set("commentsCount:" + articleId, userBehaviorMapper.selectCommentsCount(articleId), CACHE_EXPIRATION_DAYS, TimeUnit.DAYS);
        redisTemplate.opsForValue().set("viewsCount:" + articleId, userBehaviorMapper.selectViewsCount(articleId), CACHE_EXPIRATION_DAYS, TimeUnit.DAYS);
    }
}
