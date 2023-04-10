package web.crud.basic.basicweb.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import web.crud.basic.basicweb.domain.Article;
import web.crud.basic.basicweb.domain.ArticleMapper;
import web.crud.basic.basicweb.domain.UserMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    private BoardService boardService;
    @Mock
    private ArticleMapper articleMapper;
    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setBoardService() {
        this.boardService = new BoardService(articleMapper, userMapper);
    }

    @DisplayName("글 추가")
    @Test
    void addNewArticle() {
        //given
        Article article = new Article();
        article.setTitle("NewArticle");
        article.setContent("NewArticle");
        article.setWriter(1L);
        article.setDateTime(LocalDateTime.now());

        given(articleMapper.insertArticle(article)).willReturn(1);

        //when
        boolean result = boardService.addNewArticle(article);

        //then
        assertEquals(true, result);
    }

    @DisplayName("글 수정")
    @Test
    void updateArticle() {
        //given
        Article article = new Article();
        article.setId(10L);
        article.setTitle("NewArticle");
        article.setContent("NewArticle");
        article.setWriter(1L);
        article.setDateTime(LocalDateTime.now());

        given(articleMapper.getById(10L)).willReturn(article);
        given(articleMapper.update(article)).willReturn(1);

        //when
        Article result = boardService.updateArticle(article);

        //then
        assertEquals(article, result);
    }

}