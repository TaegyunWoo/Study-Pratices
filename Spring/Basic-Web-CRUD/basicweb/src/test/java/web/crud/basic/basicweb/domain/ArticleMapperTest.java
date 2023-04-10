package web.crud.basic.basicweb.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@MybatisTest
class ArticleMapperTest {

    @Autowired
    private ArticleMapper articleMapper;

    private Article article = new Article();

    @BeforeEach
    void initArticle() {
        article.setTitle("Test1");
        article.setContent("Test1 Content");
        article.setWriter(1L);
        article.setDateTime(LocalDateTime.now());
    }

    @Transactional
    @DisplayName("Insert Article & Select All")
    @Test
    void test() {
        int i = articleMapper.insertArticle(article);

        Article selectArticle = articleMapper.getById(article.getId());

        assertEquals(article.getId(), selectArticle.getId());
    }

    @DisplayName("Select By Id")
    @Test
    void getByIdTest() {
        //given, when
        Article selectedArticle = articleMapper.getById(1L);

        //then
        assertEquals(1L, selectedArticle.getId());
    }

    @DisplayName("Select by writer")
    @Test
    void getByWriterTest() {
        //given, when
        List<Article> articles = articleMapper.getByWriter(1L);

        assertNotEquals(null, articles);
    }

    @DisplayName("Update")
    @Test
    void updateTest() {
        //given
        articleMapper.insertArticle(article);
        Article insertedArticle = articleMapper.getById(article.getId());

        //when
        article.setTitle("Updated Test Title");
        articleMapper.update(article);
        Article updatedArticle = articleMapper.getById(article.getId());

        //then
        assertEquals(insertedArticle.getId(), updatedArticle.getId());
        assertEquals("Updated Test Title", updatedArticle.getTitle());
    }

    @DisplayName("Delete by id")
    @Test
    void deleteTest() {
        //given
        int isDeleted;
        int isInserted = articleMapper.insertArticle(article);
        Article insertedArticle = articleMapper.getById(article.getId());

        //when
        isDeleted = articleMapper.delete(insertedArticle.getId());

        //then
        assertEquals(1, isInserted);
        assertNotEquals(null, insertedArticle);
        assertEquals(1, isDeleted);
    }
}