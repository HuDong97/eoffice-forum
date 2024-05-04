package com.eoffice.category.service;


import com.dong.model.article.pojos.Article;
import com.dong.model.article.dtos.PageBean;


public interface ArticleService {

    //新增文章
    void add(Article article);

    //条件分页列表查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    //根据id删除文章
    void deleteById(Integer id);

    //通过id获取文章详情
    Article findById(Integer id);
    //更新文章
    void update(Article article);
}
