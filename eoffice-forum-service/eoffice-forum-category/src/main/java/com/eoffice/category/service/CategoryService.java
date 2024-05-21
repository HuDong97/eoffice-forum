package com.eoffice.category.service;


import com.eoffice.model.category.pojos.Category;

import java.util.List;

public interface CategoryService {
    //新增分类
    void add(Category category);

    //查询展示文章分类列表
    List<Category> list();



    //更新文章分类信息
    void update(Category category);

    //删除文章分类
    void deleteById(Integer id);
}

