package com.eoffice.category.service.impl;


import com.eoffice.category.mapper.CategoryMapper;
import com.eoffice.category.service.CategoryService;
import com.eoffice.model.category.pojos.Category;
import com.eoffice.utils.thread.ThreadLocalUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {


    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    //新增分类
    @Override
    public void add(Category category) {

        //补充属性值
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        //id从ThreadLocalUtil获取
        Integer userId = ThreadLocalUtil.getUser("id");
        category.setCreateUser(userId);
        categoryMapper.add(category);



    }

    //查询展示文章分类列表
    @Override
    public List<Category> list() {
        return categoryMapper.list();
    }



    //更新文章分类信息
    @Override
    public void update(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.update(category);

    }

    //删除文章分类
    @Override
    public void deleteById(Integer id) {
        categoryMapper.deleteById(id);

    }

}
