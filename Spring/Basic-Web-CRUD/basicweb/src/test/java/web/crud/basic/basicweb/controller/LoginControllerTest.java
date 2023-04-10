package web.crud.basic.basicweb.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import web.crud.basic.basicweb.WebConfig;
import web.crud.basic.basicweb.domain.User;
import web.crud.basic.basicweb.domain.UserMapper;
import web.crud.basic.basicweb.form.LoginForm;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = WebConfig.class)
@WebMvcTest(LoginController.class)
class LoginControllerTest {
    @Mock
    private UserMapper userMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new LoginController(userMapper)).build();
    }

    @DisplayName("로그인 화면 출력")
    @Test
    void viewLoginTest() throws Exception {
        //given
        User loginUser = new User();
        MockHttpSession session = new MockHttpSession();

        loginUser.setId(10L);
        loginUser.setEmail("viewLoginTest@test.com");
        loginUser.setName("viewLoginTest");
        loginUser.setPassword("123test!");

        session.setAttribute("loginUser", loginUser);

        //when
        ResultActions loginPerform = mockMvc.perform(get("/")
            .session(session)
        );

        ResultActions notLoginPerform = mockMvc.perform(get("/"));

        //then
        loginPerform.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/board/1")); //로그인 상태일때
        notLoginPerform.andExpect(status().isOk()).andExpect(view().name("home")); //로그인 상태가 아닐때
    }

    @DisplayName("로그인")
    @Test
    void loginTest() throws Exception {
        //given
        User loginUser = new User();
        LoginForm rightForm = new LoginForm();
        LoginForm wrongForm = new LoginForm();

        loginUser.setEmail("loginTest@test.com");
        loginUser.setName("loginTest");
        loginUser.setPassword("123test!");
        rightForm.setEmail(loginUser.getEmail());
        rightForm.setPassword(loginUser.getPassword());
        wrongForm.setEmail("");
        wrongForm.setPassword("");

        given(userMapper.getUserByEmailAndPassword(rightForm.getEmail(), rightForm.getPassword())).willReturn(loginUser);

        //when
        ResultActions rightPerform = mockMvc.perform(post("/")
            .param("email", rightForm.getEmail())
            .param("password", rightForm.getPassword())
        );

        ResultActions wrongPerform = mockMvc.perform(post("/")
            .param("email", "")
            .param("password", "")
        );

        //then
        rightPerform.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/board/1"))
            .andExpect(request().sessionAttribute("loginUser", loginUser)).andDo(print());
        wrongPerform.andExpect(status().isOk()).andExpect(view().name("home"))
            .andExpect(request().sessionAttributeDoesNotExist("loginUser")).andDo(print());

    }

    @DisplayName("로그아웃")
    @Test
    void logoutTest() throws Exception {
        //given
        User loginUser = new User();
        MockHttpSession session = new MockHttpSession();

        loginUser.setEmail("logoutTest@test.com");
        loginUser.setName("logoutTest");
        loginUser.setPassword("123test!");
        session.setAttribute("loginUser", loginUser);

        //when
        ResultActions perform = mockMvc.perform(get("/logout")
            .session(session)
        );

        //then
        perform.andExpect(status().is3xxRedirection()).andExpect(request().sessionAttributeDoesNotExist("loginUser"));
    }
}