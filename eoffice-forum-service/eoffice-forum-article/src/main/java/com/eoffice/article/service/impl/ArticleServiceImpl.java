package com.eoffice.article.service.impl;

import com.eoffice.article.feign.CategoryClient;
import com.eoffice.article.feign.UserClient;
import com.eoffice.article.mapper.ArticleMapper;
import com.eoffice.article.service.ArticleService;
import com.eoffice.model.article.dtos.PageBean;
import com.eoffice.model.article.pojos.Article;
import com.eoffice.utils.thread.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class ArticleServiceImpl implements ArticleService {

    private static final long CACHE_EXPIRATION_DAYS = 1;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    @Qualifier("articleRedisTemplate")
    private RedisTemplate<String, Map<String, Object>> articleRedisTemplate;
    @Override
    public Map<String, Object> findArticleById(Integer id) {
        // 尝试从 Redis 中获取缓存
        String cacheKey = "article:" + id;
        Map<String, Object> resultMap = articleRedisTemplate.opsForValue().get(cacheKey);
        System.out.println("--------redis缓存数据-------"+resultMap);

        if (resultMap == null) {
            // Redis 中不存在缓存，从数据库获取数据
            Article article = articleMapper.findArticleById(id);
            Integer userId = null;
            String nickName = null;
            if (article != null) {

                userId = article.getCreateUser();
                nickName = userClient.getNickNameByUserId(userId);
            }

            // 构建返回的Map
            resultMap = new HashMap<>();
            resultMap.put("article", article);
            resultMap.put("nickName", nickName);

            // 将结果放入 Redis 缓存，设置过期时间1天
            articleRedisTemplate.opsForValue().set(cacheKey, resultMap, Duration.ofDays(CACHE_EXPIRATION_DAYS));
        }

        System.out.println("--------service层返回值-------"+resultMap);

        return resultMap;
    }
    // 新增文章
    @Transactional
    @Override
    public void add(Article article) {
        // 补充属性值
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());

        // 从ThreadLocal获取当前登录用户id，设置为文章创建人id
        Integer userId = ThreadLocalUtil.getUser("id");
        article.setCreateUser(userId);


        articleMapper.add(article);

        Integer categoryId = article.getCategoryId();
        categoryClient.increaseCategoryCount(categoryId);

    }

    @Transactional
    @Override
    public void update(Article article) {

        Integer id = article.getId();
        Integer oleCategoryId = articleMapper.findCategoryIdById(id);
        Integer categoryId = article.getCategoryId();

        // 如果新分类与旧分类相同
        if (Objects.equals(categoryId, oleCategoryId)) {
            String cacheKey = "article:" + article.getId();

            // 检查 Redis 中是否有该文章的数据
            if (Boolean.TRUE.equals(articleRedisTemplate.hasKey(cacheKey))) {
                article.setUpdateTime(LocalDateTime.now());

                Map<String, Object> resultMap;
                resultMap = new HashMap<>();
                resultMap.put("article", article);

                // 更新 Redis 中的数据
                articleRedisTemplate.opsForValue().set(cacheKey, resultMap, CACHE_EXPIRATION_DAYS, TimeUnit.DAYS);
            }

            // 更新数据库中的数据
            articleMapper.update(article);
        } else {

            String cacheKey = "article:" + article.getId();

            // 检查 Redis 中是否有该文章的数据
            if (Boolean.TRUE.equals(articleRedisTemplate.hasKey(cacheKey))) {
                article.setUpdateTime(LocalDateTime.now());
                // 更新 Redis 中的数据

                Map<String, Object> resultMap;
                resultMap = new HashMap<>();
                resultMap.put("article", article);
                articleRedisTemplate.opsForValue().set(cacheKey, resultMap, CACHE_EXPIRATION_DAYS, TimeUnit.DAYS);
            }

            // 更新数据库中的数据
            articleMapper.update(article);

            // 如果分类不同，更新分类计数
            categoryClient.decreaseCategoryCount(oleCategoryId);
            categoryClient.increaseCategoryCount(categoryId);
        }
    }


    @Override
    @Transactional
    public void deleteById(Integer id) {
        // 先获取文章的categoryId
        Integer oleCategoryId = articleMapper.findCategoryIdById(id);

        // 检查 categoryId 是否存在
        if (oleCategoryId == null) {
            throw new IllegalArgumentException("文章 " + id + " 不存在");
        }

        String key = "article:" + id;
        // 检查 Redis 中是否有该文章的数据
        if (Boolean.TRUE.equals(articleRedisTemplate.hasKey(key))) {
            // 删除 Redis 中的数据
            articleRedisTemplate.delete(key);
        }

        // 删除数据库中的数据
        articleMapper.deleteById(id);

        // 减少类别计数
        categoryClient.decreaseCategoryCount(oleCategoryId);
    }


    // 条件分页列表查询
    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        // 1.创建PageBean对象
        PageBean<Article> pb = new PageBean<>();

        // 2.开启分页查询 PageHelper
        PageHelper.startPage(pageNum, pageSize);

        // 3.调用mapper
        Integer userId = ThreadLocalUtil.getUser("id");
        List<Article> as = articleMapper.list(userId, categoryId, state);

        // Page中提供了方法,可以获取PageHelper分页查询后 得到的总记录条数和当前页数据
        Page<Article> p = (Page<Article>) as;

        // 把数据填充到PageBean对象中
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }
}
