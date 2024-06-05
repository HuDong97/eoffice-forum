package com.eoffice.userBehavior.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface UserBehaviorMapper {
    @Insert("INSERT INTO Likes (user_id, article_id) VALUES (#{userId}, #{articleId})")
    void insertLike(@Param("userId") Integer userId, @Param("articleId") Integer articleId);

    @Insert("INSERT INTO Favorites (user_id, article_id) VALUES (#{userId}, #{articleId})")
    void insertFavorite(@Param("userId") Integer userId, @Param("articleId") Integer articleId);

    @Insert("INSERT INTO Comments (user_id, article_id, content) VALUES (#{userId}, #{articleId}, #{content})")
    void insertComment(@Param("userId") Integer userId, @Param("articleId") Integer articleId, @Param("content") String content);

    @Insert("INSERT INTO Views (user_id, article_id) VALUES (#{userId}, #{articleId})")
    void insertView(@Param("userId") Integer userId, @Param("articleId") Integer articleId);

    @Select("SELECT COUNT(*) FROM Likes WHERE article_id = #{articleId}")
    int selectLikesCount(@Param("articleId") Integer articleId);

    @Select("SELECT COUNT(*) FROM Favorites WHERE article_id = #{articleId}")
    int selectFavoritesCount(@Param("articleId") Integer articleId);

    @Select("SELECT COUNT(*) FROM Comments WHERE article_id = #{articleId}")
    int selectCommentsCount(@Param("articleId") Integer articleId);

    @Select("SELECT COUNT(*) FROM Views WHERE article_id = #{articleId}")
    int selectViewsCount(@Param("articleId") Integer articleId);
}
