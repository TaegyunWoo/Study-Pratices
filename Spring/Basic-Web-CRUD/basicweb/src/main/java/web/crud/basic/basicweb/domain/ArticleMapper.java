package web.crud.basic.basicweb.domain;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    @Insert("INSERT INTO article (title, content, writer, datetime) VALUES (#{article.title}, #{article.content}, #{article.writer}, #{article.dateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertArticle(@Param("article") Article article);

    @Select("SELECT * FROM article")
    @Results(id = "ArticleMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "content", column = "content"),
            @Result(property = "writer", column = "writer"),
            @Result(property = "dateTime", column = "datetime")
    })
    List<Article> getAll();

    @Select("SELECT * FROM article WHERE id=#{id}")
    @ResultMap("ArticleMap")
    Article getById(@Param("id") Long id);

    @Select("SELECT * FROM article WHERE writer=#{userId}")
    @ResultMap("ArticleMap")
    List<Article> getByWriter(@Param("userId") Long userId);

    @Update("UPDATE article SET title=#{article.title}, datetime=#{article.dateTime}, content=#{article.content} WHERE id=#{article.id}")
    @ResultMap("ArticleMap")
    int update(@Param("article") Article article);

    @Delete("DELETE FROM article WHERE id=#{id}")
    int delete(@Param("id") Long id);
}
