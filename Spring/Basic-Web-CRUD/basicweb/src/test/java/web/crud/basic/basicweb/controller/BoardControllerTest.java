package web.crud.basic.basicweb.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import web.crud.basic.basicweb.Service.BoardService;
import web.crud.basic.basicweb.WebConfig;
import web.crud.basic.basicweb.domain.Article;
import web.crud.basic.basicweb.domain.ArticleMapper;
import web.crud.basic.basicweb.domain.User;
import web.crud.basic.basicweb.domain.UserMapper;
import web.crud.basic.basicweb.form.NewArticleForm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class) //Mock 객체를 위한 확장모델 등록
@AutoConfigureMybatis
@ContextConfiguration(classes = WebConfig.class) //Application Context(설정) 등록
@WebMvcTest(BoardController.class) //테스트할 컨트롤러 지정
class BoardControllerTest {
    @Mock
    private BoardService boardService;

    private MockMvc mockMvc; //@Autowired 가 없음에 주의!

    //MockMvc가 BoardController 를 사용할 수 있도록 설정
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BoardController(boardService)).build();
    }


    @DisplayName("게시판 보이기 테스트")
    @Test
    void showBoardTest(@Mock ArticleMapper articleMapper) throws Exception {
        //---------- given ----------
        List<Article> articleList = new ArrayList<>();
        Article article1 = new Article();
        Article article2 = new Article();
        MockHttpSession session = new MockHttpSession();
        User user = new User();

        article1.setTitle("Article1");
        article1.setContent("Article1 Content");
        article1.setWriter(1L);
        article1.setDateTime(LocalDateTime.now());

        article2.setTitle("Article2");
        article2.setContent("Article2 Content");
        article2.setWriter(1L);
        article2.setDateTime(LocalDateTime.now());

        articleList.add(article1);
        articleList.add(article2);

        user.setEmail("mockUser@test.com");
        user.setId(5L);
        user.setPassword("123test!");
        user.setName("mockUser");

        //로그인 사용자 세션 추가
        session.setAttribute("loginUser", user);

        given(boardService.getAllArticles()).willReturn(articleList);

        //---------- when ----------
        ResultActions actions = mockMvc.perform(get("/board/{pageNum}", 1)
            .session(session)
        );

        //---------- then ----------
        actions.andExpect(status().isOk()).andDo(print());

    }

    @DisplayName("새 게시글 작성 창 보이기")
    @Test
    void showArticleAddFormTest() throws Exception {
        //given
        NewArticleForm form = new NewArticleForm();
        form.setTitle("New Article Title");
        form.setContent("New Article Content");

        //when
        ResultActions actions = mockMvc.perform(
            get("/board/new-article")
        );

        //then
        actions.andExpect(status().isOk())
            .andExpect(view().name("add-article"));
    }

    @DisplayName("게시글 추가")
    @Test
    void addArticleTest() throws Exception {
        //given
        NewArticleForm rightForm = new NewArticleForm();
        NewArticleForm wrongForm = new NewArticleForm();
        User loginUser = new User();
        MockHttpSession session = new MockHttpSession();

        rightForm.setTitle("Test Article Title");
        rightForm.setContent("Test Article Content");
        wrongForm.setTitle("");
        wrongForm.setContent("");
        loginUser.setId(5L);
        loginUser.setName("BoardTestUser");
        loginUser.setEmail("BoardTestUser@test.com");
        loginUser.setPassword("123test!");
        session.setAttribute("loginUser", loginUser);

        given(boardService.addNewArticle(any())).willReturn(true);


        //when
        ResultActions rightPerform = mockMvc.perform(
            post("/board/new-article")
                .param("title", rightForm.getTitle())
                .param("content", rightForm.getContent())
                .session(session)
        );

        ResultActions wrongPerform = mockMvc.perform(
            post("/board/new-article")
                .param("title", wrongForm.getTitle())
                .param("content", wrongForm.getContent())
                .session(session)
        );

        //then
        rightPerform.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/board/1")); //성공했는지
        wrongPerform.andExpect(status().isOk()).andExpect(view().name("add-article")); //실패했는지
    }

    @DisplayName("게시글 보이기")
    @Test
    void showArticleTest() throws Exception {
        //given
        Article article = new Article();
        article.setTitle("showArticleTest Title");
        article.setContent("showArticleTest Content");
        article.setWriter(1L);
        article.setId(10L);
        article.setDateTime(LocalDateTime.now());
        given(boardService.getArticleById(1L)).willReturn(article);

        given(boardService.getWriterName(article)).willReturn("showArticleTest Writer");

        //when
        ResultActions perform = mockMvc.perform(get("/board/article/1"));

        //then
        perform.andExpect(status().isOk()).andExpect(view().name("article"));
    }

    @Transactional
    @DisplayName("게시글 수정")
    @Test
    void editArticleTest() throws Exception {
        //given
        Article updatedArticle = new Article();
        updatedArticle.setId(10L);
        updatedArticle.setTitle("Updated Article");
        updatedArticle.setContent("Updated Article");
        updatedArticle.setWriter(1L);

        given(boardService.updateArticle(any())).willReturn(updatedArticle);

        //when
        ResultActions perform = mockMvc.perform(post("/board/edit/" + updatedArticle.getId())
            .param("id", String.valueOf(updatedArticle.getId()))
            .param("title", updatedArticle.getTitle())
            .param("content", updatedArticle.getContent())
            .param("writerId", String.valueOf(updatedArticle.getWriter()))
        );

        //then
        perform.andExpect(status().is3xxRedirection());
    }

    @Transactional
    @DisplayName("게시글 삭제")
    @Test
    void deleteArticleTest() throws Exception {
        //given

        //when
        ResultActions perform = mockMvc.perform(get("/board/edit/1/delete"));

        //then
        perform.andExpect(status().is3xxRedirection());
    }
}