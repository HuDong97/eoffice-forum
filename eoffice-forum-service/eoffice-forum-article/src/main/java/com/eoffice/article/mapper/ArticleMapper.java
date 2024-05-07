package com.eoffice.article.mapper;


import com.eoffice.model.article.pojos.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {

    //新增文章
    /*@Insert("insert into category(title,content,cover_img,state,category_id,create_user,create_time,update_time) " +
            "values(#{title},#{content},#{coverImg},#{state},#{categoryId},#{createUser},#{createTime},#{updateTime})")*/
    void add(Article article);

    //条件分页列表查询
    List<Article> list(Integer userId, Integer categoryId, String state);

    //通过id获取文章详情
    //@Select("select * from category where id = #{id}")
    Article findById(Integer id);


    //根据id删除文章
    //@Delete("delete from category where id=#{id}")
    void deleteById(Integer id);

    //更新文章
    //@Update("update category set title=#{title},content=#{content},cover_img=#{coverImg},state=#{state},category_id=#{categoryId},update_time=#{updateTime} where id=#{id}")
    void update(Article article);
}
