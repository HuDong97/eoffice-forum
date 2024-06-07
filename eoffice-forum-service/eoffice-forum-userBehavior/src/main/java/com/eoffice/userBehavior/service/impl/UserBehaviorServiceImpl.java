package com.eoffice.userBehavior.service.impl;

import com.eoffice.userBehavior.mapper.UserBehaviorMapper;
import com.eoffice.userBehavior.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserBehaviorServiceImpl implements UserBehaviorService {

    private static final long CACHE_EXPIRATION_DAYS = 1;
    private static final String CACHE_PREFIX = "articleCounts:";

    @Autowired
    private UserBehaviorMapper userBehaviorMapper;

    @Autowired
    @Qualifier("userBehaviorRedisTemplate")
    private RedisTemplate<String, Map<String, Integer>> userBehaviorRedisTemplate;

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
    public Map<String, Integer> getArticleCounts(Integer articleId) {
        String key = CACHE_PREFIX + articleId;
        Map<String, Integer> counts = userBehaviorRedisTemplate.opsForValue().get(key);
        if (counts == null) {
            counts = fetchArticleCountsFromDB(articleId);
            cacheArticleCounts(articleId, counts);
        }
        return counts;
    }

    private Map<String, Integer> fetchArticleCountsFromDB(Integer articleId) {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("likesCount", userBehaviorMapper.selectLikesCount(articleId));
        counts.put("favoritesCount", userBehaviorMapper.selectFavoritesCount(articleId));
        counts.put("commentsCount", userBehaviorMapper.selectCommentsCount(articleId));
        counts.put("viewsCount", userBehaviorMapper.selectViewsCount(articleId));
        return counts;
    }

    private void cacheArticleCounts(Integer articleId, Map<String, Integer> counts) {
        String key = CACHE_PREFIX + articleId;
        userBehaviorRedisTemplate.opsForValue().set(key, counts, CACHE_EXPIRATION_DAYS, TimeUnit.DAYS);
    }

    public void updateArticleCountsInRedis(Integer articleId) {
        Map<String, Integer> counts = fetchArticleCountsFromDB(articleId);
        cacheArticleCounts(articleId, counts);
    }
}
