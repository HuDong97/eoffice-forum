package com.eoffice.category.mapper;


import com.eoffice.model.category.pojos.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryMapper {

    //新增分类
   /* @Insert("insert into category(category_name,category_alias,create_user,create_time,update_time) " +
            "values(#{categoryName},#{categoryCount},#{createUser},#{createTime},#{updateTime})")*/
    void add(Category category);

    //通过创建人id查询展示文章分类列表
    //@Select("select * from category ")
    List<Category> list();



    //更新文章分类信息
    //@Update("update category set category_name=#{categoryName},category_alias=#{categoryCount},update_time=#{updateTime} where id=#{id}")
    void update(Category category);

    //删除文章分类
    // @Delete("delete from category where id=#{id}")
    void deleteById(Integer id);

    //更新category表里面category_count数目
    @Update("UPDATE category SET category_count = category_count + 1 WHERE id = #{categoryId}")
    void increaseCategoryCount(Integer categoryId);

    @Update("UPDATE category SET category_count = category_count - 1 WHERE id = #{oleCategoryId}")
    void decreaseCategoryCount(Integer oleCategoryId);


}
