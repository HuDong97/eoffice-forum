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

@Service
public class ArticleServiceImpl implements ArticleService {

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
                articleRedisTemplate.opsForValue().set(key, article);
            }
        }
        return article;
    }

    // 更新文章，先更新Redis数据，再更新数据库数据
    @Override
    public void update(Article article) {
        String key = "article:" + article.getId();
        article.setUpdateTime(LocalDateTime.now());
        articleRedisTemplate.opsForValue().set(key, article);
        articleMapper.update(article);
    }

    // 根据id删除文章，先删除Redis数据，再删除数据库数据
    @Override
    public void deleteById(Integer id) {
        String key = "article:" + id;
        articleRedisTemplate.delete(key);
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
