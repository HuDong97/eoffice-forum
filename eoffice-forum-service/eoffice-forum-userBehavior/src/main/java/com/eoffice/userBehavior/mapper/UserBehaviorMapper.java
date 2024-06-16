package com.eoffice.userBehavior.mapper;

import com.eoffice.model.userBehavior.comments.vo.Comments;
import com.eoffice.model.userBehavior.favorites.vo.Favorites;
import com.eoffice.model.userBehavior.likes.vo.Likes;
import com.eoffice.model.userBehavior.views.vo.Views;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserBehaviorMapper {

    @Insert("INSERT INTO Likes (user_id, article_id, created_time ) VALUES (#{userId}, #{articleId}, #{createdTime})")
    void insertLike(Likes likes);

    @Insert("INSERT INTO Favorites (user_id, article_id, created_time ) VALUES (#{userId}, #{articleId}, #{createdTime})")
    void insertFavorite(Favorites favorites);

    @Insert("INSERT INTO Comments (user_id, article_id, content, created_time) VALUES (#{userId}, #{articleId}, #{content}, #{createdTime})")
    void insertComment(Comments comments);

    @Insert("INSERT INTO Views (user_id, article_id, created_time) VALUES (#{userId}, #{articleId}, #{createdTime})")
    void insertView(Views views);


    @Select("SELECT COUNT(*) FROM Likes WHERE article_id = #{articleId}")
    int selectLikesCount(@Param("articleId") Integer articleId);

    @Select("SELECT COUNT(*) FROM Favorites WHERE article_id = #{articleId}")
    int selectFavoritesCount(@Param("articleId") Integer articleId);

    @Select("SELECT COUNT(*) FROM Comments WHERE article_id = #{articleId}")
    int selectCommentsCount(@Param("articleId") Integer articleId);

    @Select("SELECT COUNT(*) FROM Views WHERE article_id = #{articleId}")
    int selectViewsCount(@Param("articleId") Integer articleId);

    @Delete("DELETE FROM Likes WHERE user_id = #{userId} AND article_id = #{articleId}")
    void deleteLikeByUserIdAndArticleId(Integer userId,Integer articleId);

    @Delete("delete from Favorites where user_id = #{userId} AND article_id = #{articleId}")
    void deleteFavoriteByUserIdAndArticleId(Integer userId,Integer articleId);

    @Delete("delete from Comments where id =#{id} ")
    void deleteCommentById( Integer id);

    @Select("SELECT COUNT(*) FROM Likes WHERE user_id = #{userId} AND article_id = #{articleId}")
    int selectLikeById(Integer userId, Integer articleId);

    @Select("SELECT COUNT(*) FROM Favorites WHERE user_id = #{userId} AND article_id = #{articleId}")
    int selectFavoritesById(Integer userId, Integer articleId);
    @Select("SELECT COUNT(*) FROM Views WHERE user_id = #{userId} AND article_id = #{articleId}")
    int selectViewsById(Integer userId, Integer articleId);

    @Select("select * from Comments where article_id=#{articleId}")
    List<Comments> findCommentByArticleId(Integer articleId);
    @Select("select * from Comments where id=#{id}")
    Comments findCommentById(Integer id);
}

