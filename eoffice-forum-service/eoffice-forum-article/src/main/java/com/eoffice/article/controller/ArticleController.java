package com.eoffice.article.controller;

import com.eoffice.common.advice.Result;
import com.eoffice.model.article.dtos.PageBean;
import com.eoffice.model.article.pojos.Article;
import com.eoffice.article.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/article")
public class ArticleController {


    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    //新增文章
    @PostMapping
    public Result<String> add(@RequestBody Article article) {
        articleService.add(article);
        return Result.success();
    }

    //更新文章
    @PutMapping
    public Result<String> update(@RequestBody  Article article){
        articleService.update(article);
        return Result.success();
    }


    //条件分页列表查询
    @GetMapping
    public Result<PageBean<Article>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String state
    ) {
        PageBean<Article> pb =  articleService.list(pageNum,pageSize,categoryId,state);
        return Result.success(pb);
    }

    //通过id获取文章详情
    @GetMapping("/detail")
    public Result<Map<String, Object>> detail(@RequestParam Integer id){
        Map<String, Object> articleDetail = articleService.findArticleById(id);

        return Result.success(articleDetail);
    }


    //根据id删除文章
    @DeleteMapping
    public Result<String> delete(Integer id){
        articleService.deleteById(id);
        return Result.success();
    }


}
