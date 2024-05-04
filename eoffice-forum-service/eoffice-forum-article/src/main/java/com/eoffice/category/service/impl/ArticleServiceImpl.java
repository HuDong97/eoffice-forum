package com.eoffice.category.service.impl;


import com.dong.model.article.dtos.PageBean;
import com.dong.model.article.pojos.Article;
import com.dong.utils.thread.ThreadLocalUtil;
import com.eoffice.category.mapper.ArticleMapper;
import com.eoffice.category.service.ArticleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    //新增文章
    @Override
    public void add(Article article) {

        //补充属性值
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());

        //从ThreadLocal获取当前登录用户id，设置为文章创建人id
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        article.setCreateUser(userId);

        articleMapper.add(article);

    }



    //条件分页列表查询
    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        //1.创建PageBean对象
        PageBean<Article> pb = new PageBean<>();

        //2.开启分页查询 PageHelper
        PageHelper.startPage(pageNum,pageSize);

        //3.调用mapper
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        List<Article> as = articleMapper.list(userId,categoryId,state);

        //Page中提供了方法,可以获取PageHelper分页查询后 得到的总记录条数和当前页数据
        Page<Article> p = (Page<Article>) as;

        //把数据填充到PageBean对象中
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }


    //通过id获取文章详情
    @Override
    public Article findById(Integer id) {
        return articleMapper.findById(id);

    }
    //更新文章
    @Override
    public void update(Article article) {
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.update(article);

    }

    //根据id删除文章
    @Override
    public void deleteById(Integer id) {
        articleMapper.deleteById(id);

    }


}

