package com.eoffice.category.service;


import com.dong.model.category.pojos.Category;

import java.util.List;

public interface CategoryService {
    //新增分类
    void add(Category category);

    //通过创建人id查询展示文章分类列表
    List<Category> list();

    //通过id查询单个文章分类详细信息
    Category findById(Integer id);

    //更新文章分类信息
    void update(Category category);

    //删除文章分类
    void deleteById(Integer id);
}
