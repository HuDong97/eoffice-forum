package com.eoffice.category.controller;

import com.eoffice.category.service.CategoryService;
import com.eoffice.common.advice.Result;
import com.eoffice.model.category.pojos.Category;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    //article微服务使用openfeign调用
    @PutMapping("/article/{id}/increase")
    public void increaseCategoryCount(@PathVariable("id") Integer categoryId) {
        categoryService.increaseCategoryCount(categoryId);
    }

    //article微服务使用openfeign调用
    @PutMapping("/article/{id}/decrease")
    public void decreaseCategoryCount(@PathVariable("id") Integer oleCategoryId) {
        categoryService.decreaseCategoryCount(oleCategoryId);
    }

    //添加文章分类
    @PostMapping
    public Result<String> add(@RequestBody @Validated(Category.Add.class) Category category) {
        categoryService.add(category);
        return Result.success("添加成功");
    }

    //查询展示文章分类列表
    @GetMapping
    public Result<List<Category>> list() {
        List<Category> c = categoryService.list();
        return Result.success(c);
    }

    //更新文章分类信息
    @PutMapping
    public Result<String> update(@RequestBody @Validated(Category.Update.class) Category category) {
        categoryService.update(category);
        return Result.success("更新成功");
    }

    //删除文章分类
    @DeleteMapping
    public Result<String> delete(Integer id) {
        categoryService.deleteById(id);
        return Result.success("删除成功");
    }


}
