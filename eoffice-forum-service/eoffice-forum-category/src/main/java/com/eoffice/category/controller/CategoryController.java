package com.eoffice.category.controller;

import com.eoffice.common.advice.Result;
import com.eoffice.model.category.pojos.Category;
import com.eoffice.category.service.CategoryService;
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

    //添加文章分类
    @PostMapping
    public Result<String> add (@RequestBody @Validated (Category.Add.class)Category category){
        categoryService.add(category);
        return Result.success();
    }

    //通过创建人id查询展示文章分类列表
    @GetMapping
    public Result<List<Category>> list(){
        List<Category> cs = categoryService.list();
        return Result.success(cs);
    }

    //通过id查询单个文章分类详细信息
    @GetMapping("/detail")
    public Result<Category> detail(Integer id){
        Category c = categoryService.findById(id);
        return Result.success(c);
    }

    //更新文章分类信息
    @PutMapping
    public Result<String> update(@RequestBody @Validated(Category.Update.class) Category category){
        categoryService.update(category);
        return Result.success();
    }

    //删除文章分类
    @DeleteMapping
    public Result<String> delete(Integer id){
        categoryService.deleteById(id);
        return Result.success();
    }



}
