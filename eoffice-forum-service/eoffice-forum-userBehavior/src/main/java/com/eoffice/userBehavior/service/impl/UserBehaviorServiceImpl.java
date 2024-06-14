package com.eoffice.userBehavior.service.impl;

import com.eoffice.model.article.pojos.Article;
import com.eoffice.model.userBehavior.comments.vo.Comments;
import com.eoffice.model.userBehavior.favorites.vo.Favorites;
import com.eoffice.model.userBehavior.likes.vo.Likes;
import com.eoffice.model.userBehavior.views.vo.Views;
import com.eoffice.userBehavior.mapper.UserBehaviorMapper;
import com.eoffice.userBehavior.service.UserBehaviorService;
import com.eoffice.utils.thread.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


    @Autowired
    @Qualifier("commentRedisTemplate")
    private RedisTemplate<String, Comments> commentRedisTemplate;



    @Override
    public void setLikeArticle(Integer articleId) {
        Likes likes = new Likes();
        likes.setCreateTime(LocalDateTime.now());
        likes.setArticleId(articleId);
        // 从ThreadLocal获取当前登录用户id，设置为文章创建人id
        Integer userId = ThreadLocalUtil.getUser("id");
        if (userId != null) {
            likes.setUserId(userId);
        }
        userBehaviorMapper.insertLike(likes);
        // 更新 Redis 中的 map 表信息
        updateRedisMap(articleId, "likesCount");

    }

    @Override
    public void setFavoriteArticle(Integer articleId) {
        Favorites favorites = new Favorites();
        favorites.setCreateTime(LocalDateTime.now());
        favorites.setArticleId(articleId);
        // 从ThreadLocal获取当前登录用户id，设置为文章创建人id
        Integer userId = ThreadLocalUtil.getUser("id");
        if (userId != null) {
            favorites.setUserId(userId);
        }
        userBehaviorMapper.insertFavorite(favorites);
        // 更新 Redis 中的 map 表信息
        updateRedisMap(articleId, "favoritesCount");
    }


    @Override
    public void setViewArticle(Integer articleId) {
        Views views = new Views();
        views.setCreateTime(LocalDateTime.now());
        views.setArticleId(articleId);
        // 从ThreadLocal获取当前登录用户id，设置为文章创建人id
        Integer userId = ThreadLocalUtil.getUser("id");
        int viewMarked = selectViewsById(articleId);
        if (userId != null && viewMarked != 1) {
            views.setUserId(userId);
            userBehaviorMapper.insertView(views);
            // 更新 Redis 中的 map 表信息
            updateRedisMap(articleId, "viewsCount");
        }
    }


    @Override
    public void setCommentArticle(Comments comments) {
        comments.setCreateTime(LocalDateTime.now());
        // 从ThreadLocal获取当前登录用户id，设置为文章创建人id
        Integer userId = ThreadLocalUtil.getUser("id");
        if (userId != null) {
            comments.setUserId(userId);
        }
        userBehaviorMapper.insertComment(comments);
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


    @Override
    public void deleteLikeByUserIdAndArticleId(Integer userId, Integer articleId) {
        // 删除数据库中的数据
        userBehaviorMapper.deleteLikeByUserIdAndArticleId(userId, articleId);

    }


    @Override
    public void deleteFavoriteByUserIdAndArticleId(Integer userId, Integer articleId) {
        // 删除数据库中的数据
        userBehaviorMapper.deleteFavoriteByUserIdAndArticleId(userId, articleId);

    }


    @Override
    public void deleteCommentById(Integer id) {
        // 删除数据库中的数据
        userBehaviorMapper.deleteCommentById(id);

    }

    //获取用户是否点赞收藏
    @Override
    public Map<String, Integer> getArticleBehavior(Integer articleId) {

        Map<String, Integer> behaviorMap = new HashMap<>();

        int liked = selectLikeById(articleId);
        int bookmarked = selectFavoritesById(articleId);

        behaviorMap.put("liked", liked);
        behaviorMap.put("bookmarked", bookmarked);

        return behaviorMap;
    }

    @Override
    public Comments findByArticleId(Integer articleId) {
        String key = "comment:" + articleId;
        Comments commentAll = commentRedisTemplate.opsForValue().get(key);
        if (commentAll == null) {
            commentAll = userBehaviorMapper.findByArticleId(articleId);
            if (commentAll != null) {
                commentRedisTemplate.opsForValue().set(key, commentAll, CACHE_EXPIRATION_DAYS, TimeUnit.DAYS);
            }
        }

       return commentAll;
    }


    public int selectLikeById(Integer articleId) {
        // 从ThreadLocal获取当前登录用户id
        Integer userId = ThreadLocalUtil.getUser("id");
        //通过用户id和文章id查询用户是否点赞，返回1是点赞，0是没点赞
        int liked = userBehaviorMapper.selectLikeById(userId, articleId);

        if (liked != 1 && liked != 0) {
            throw new RuntimeException("点赞数异常");
        }

        return liked;
    }

    public int selectFavoritesById(Integer articleId) {
        // 从ThreadLocal获取当前登录用户id
        Integer userId = ThreadLocalUtil.getUser("id");
        //通过用户id和文章id查询用户是否收藏，返回1是点赞，0是没点赞

        int bookmarked = userBehaviorMapper.selectFavoritesById(userId, articleId);
        if (bookmarked != 1 && bookmarked != 0) {
            throw new RuntimeException("收藏数异常");
        }
        return bookmarked;
    }


    public int selectViewsById(Integer articleId) {
        // 从ThreadLocal获取当前登录用户id
        Integer userId = ThreadLocalUtil.getUser("id");
        //通过用户id和文章id查询用户是否收藏，返回1是点赞，0是没点赞

        int viewed = userBehaviorMapper.selectViewsById(userId, articleId);
        if (viewed != 1 && viewed != 0) {
            throw new RuntimeException("观看数异常");
        }
        return viewed;
    }

    private void updateRedisMap(Integer articleId, String behaviorType) {
        String key = CACHE_PREFIX + articleId;
        Map<String, Integer> counts = userBehaviorRedisTemplate.opsForValue().get(key);
        if (counts == null) {
            counts = new HashMap<>();
        }

        // 根据行为类型更新对应计数
        counts.put(behaviorType, counts.getOrDefault(behaviorType, 0) + 1);

        // 更新 Redis 中的 map 表信息
        userBehaviorRedisTemplate.opsForValue().set(key, counts, CACHE_EXPIRATION_DAYS, TimeUnit.DAYS);
    }


}
