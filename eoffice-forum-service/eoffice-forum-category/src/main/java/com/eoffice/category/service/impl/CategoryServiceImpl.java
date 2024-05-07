package com.eoffice.category.service.impl;


import com.eoffice.model.category.pojos.Category;
import com.eoffice.utils.thread.ThreadLocalUtil;
import com.eoffice.category.mapper.CategoryMapper;
import com.eoffice.category.service.CategoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        category.setCreateUser(userId);
        categoryMapper.add(category);



    }

    //通过创建人id查询展示文章分类列表
    @Override
    public List<Category> list() {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return categoryMapper.list(userId);
    }

    //通过id查询单个文章分类详细信息
    @Override
    public Category findById(Integer id) {
        Category c = categoryMapper.findById(id);
        return c;
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
