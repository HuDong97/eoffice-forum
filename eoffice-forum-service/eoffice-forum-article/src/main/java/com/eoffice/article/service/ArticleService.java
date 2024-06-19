package com.eoffice.article.service;


import com.eoffice.model.article.pojos.Article;
import com.eoffice.model.article.dtos.PageBean;

import java.util.Map;


public interface ArticleService {

    //新增文章
    void add(Article article);

    //条件分页列表查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    //根据id删除文章
    void deleteById(Integer id);

    //通过id获取文章详情
    Map<String, Object> findArticleById(Integer id);

    //更新文章
    void update(Article article);
}
