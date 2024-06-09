package com.eoffice.article.service.impl;

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

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ArticleServiceImpl implements ArticleService {
    private static final long CACHE_EXPIRATION_DAYS = 1;
    private final ArticleMapper articleMapper;
    private final RedisTemplate<String, Article> articleRedisTemplate;

    @Autowired
    public ArticleServiceImpl(ArticleMapper articleMapper, @Qualifier("articleRedisTemplate") RedisTemplate<String, Article> articleRedisTemplate) {
        this.articleMapper = articleMapper;
        this.articleRedisTemplate = articleRedisTemplate;
    }

    // 新增文章
    @Override
    public void add(Article article) {
        // 补充属性值
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());

        // 从ThreadLocal获取当前登录用户id，设置为文章创建人id
        Integer userId = ThreadLocalUtil.getUser("id");
        article.setCreateUser(userId);

        articleMapper.add(article);
    }

    // 通过id获取文章详情，先从Redis获取，如果不存在则从数据库获取，并将数据保存到Redis
    @Override
    public Article findById(Integer id) {
        String key = "article:" + id;
        Article article = articleRedisTemplate.opsForValue().get(key);
        if (article == null) {
            article = articleMapper.findById(id);
            if (article != null) {
                articleRedisTemplate.opsForValue().set(key, article, CACHE_EXPIRATION_DAYS, TimeUnit.DAYS);
            }
        }
        return article;
    }

    @Override
    public void update(Article article) {
        String key = "article:" + article.getId();
        // 检查 Redis 中是否有该文章的数据
        if (Boolean.TRUE.equals(articleRedisTemplate.hasKey(key))) {
            article.setUpdateTime(LocalDateTime.now());
            // 更新 Redis 中的数据
            articleRedisTemplate.opsForValue().set(key, article, CACHE_EXPIRATION_DAYS, TimeUnit.DAYS);
        }
        // 更新数据库中的数据
        articleMapper.update(article);
    }

    @Override
    public void deleteById(Integer id) {
        String key = "article:" + id;
        // 检查 Redis 中是否有该文章的数据
        if (Boolean.TRUE.equals(articleRedisTemplate.hasKey(key))) {
            // 删除 Redis 中的数据
            articleRedisTemplate.delete(key);
        }
        // 删除数据库中的数据
        articleMapper.deleteById(id);
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
