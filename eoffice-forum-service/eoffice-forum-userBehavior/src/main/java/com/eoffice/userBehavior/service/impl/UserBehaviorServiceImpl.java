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
import java.util.List;
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
    @Qualifier("commentListRedisTemplate")
    private RedisTemplate<String, List<Comments>> commentListRedisTemplate;


    @Override
    public void setLikeArticle(Integer articleId) {
        Likes likes = new Likes();
        likes.setCreatedTime(LocalDateTime.now());
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
        favorites.setCreatedTime(LocalDateTime.now());
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
        views.setCreatedTime(LocalDateTime.now());
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
        comments.setCreatedTime(LocalDateTime.now());
        // 从ThreadLocal获取当前登录用户id，设置为文章创建人id
        Integer userId = ThreadLocalUtil.getUser("id");
        if (userId != null) {
            comments.setUserId(userId);
        }
        userBehaviorMapper.insertComment(comments);
        // 更新 Redis 中的评论列表数据
        updateRedisComments(comments.getArticleId(), comments);
    }


    private void updateRedisComments(Integer articleId, Comments newComment) {
        String redisKey = "article:comments:" + articleId;

        // 尝试从Redis中获取评论列表
        List<Comments> comments = commentListRedisTemplate.opsForValue().get(redisKey);

        if (comments == null) {
            // 如果Redis中没有，从数据库中获取
            comments = userBehaviorMapper.findCommentByArticleId(articleId);
        }

        if (comments != null) {
            // 添加新评论到列表的开头
            comments.add(0, newComment);

            // 更新 Redis 中的评论列表数据
            commentListRedisTemplate.opsForValue().set(redisKey, comments, 1, TimeUnit.HOURS);
        }
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
        // 获取要删除的评论信息
        Comments comment = userBehaviorMapper.findCommentById(id);
        if (comment != null) {
            // 删除数据库中的数据
            userBehaviorMapper.deleteCommentById(id);

            // 从 Redis 中删除评论数据
            deleteRedisComment(comment.getArticleId(), comment);
        }
    }

    private void deleteRedisComment(Integer articleId, Comments deletedComment) {
        String redisKey = "article:comments:" + articleId;

        // 尝试从Redis中获取评论列表
        List<Comments> comments = commentListRedisTemplate.opsForValue().get(redisKey);

        if (comments != null) {
            // 从列表中移除被删除的评论
            comments.removeIf(comment -> comment.getId().equals(deletedComment.getId()));

            // 更新 Redis 中的评论列表数据
            commentListRedisTemplate.opsForValue().set(redisKey, comments, 1, TimeUnit.HOURS);
        }
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
    public List<Comments> findCommentByArticleId(Integer articleId) {
        String redisKey = "article:comments:" + articleId;

        // 尝试从Redis中获取评论列表
        List<Comments> comments = commentListRedisTemplate.opsForValue().get(redisKey);

        if (comments == null) {
            // 如果Redis中没有，从数据库中获取
            comments = userBehaviorMapper.findCommentByArticleId(articleId);
            if (comments != null && !comments.isEmpty()) {
                // 将评论列表存储到Redis中，设置过期时间，1小时
                commentListRedisTemplate.opsForValue().set(redisKey, comments, 1, TimeUnit.HOURS);
            }
        }

        return comments;
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
