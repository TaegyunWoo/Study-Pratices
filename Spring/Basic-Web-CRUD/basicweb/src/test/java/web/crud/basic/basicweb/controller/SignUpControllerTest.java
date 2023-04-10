package web.crud.basic.basicweb.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import web.crud.basic.basicweb.Service.SignUpService;
import web.crud.basic.basicweb.WebConfig;
import web.crud.basic.basicweb.domain.User;
import web.crud.basic.basicweb.form.SignUpForm;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = WebConfig.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(SignUpController.class)
class SignUpControllerTest {

    @Mock
    private SignUpService signUpService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SignUpController(signUpService)).build();
    }

    @DisplayName("회원가입창 출력")
    @Test
    void showSignUpTest() throws Exception {
        //given


        //when
        ResultActions perform = mockMvc.perform(get("/sign-up"));

        //then
        perform.andExpect(status().isOk()).andExpect(view().name("add-user"));
    }

    @DisplayName("회원가입")
    @Test
    void signUpTest() throws Exception {
        //given
        final String originEmail = "alreadyExistEmail@test.com";
        final String newEmail = "signUpTest@test.com";
        final String passwordA = "123test!";
        final String passwordB = "differentPassword123!";

        SignUpForm signUpForm = new SignUpForm();

        signUpForm.setEmail(newEmail);
        signUpForm.setName("signUpTest");
        signUpForm.setPassword(passwordA);
        signUpForm.setConfirmPassword(passwordA);

        given(signUpService.isDifferentPassword(passwordA, passwordB)).willReturn(true);
        given(signUpService.isDifferentPassword(passwordA, passwordA)).willReturn(false);
        given(signUpService.isExistEmail(originEmail)).willReturn(true);
        given(signUpService.isExistEmail(newEmail)).willReturn(false);

        //when
        ResultActions successPerform = mockMvc.perform(post("/sign-up")
            .param("email", signUpForm.getEmail())
            .param("name", signUpForm.getName())
            .param("password", signUpForm.getPassword())
            .param("confirmPassword", signUpForm.getConfirmPassword())
        );

        ResultActions differentPasswordPerform = mockMvc.perform(post("/sign-up")
            .param("email", signUpForm.getEmail())
            .param("name", signUpForm.getName())
            .param("password", signUpForm.getPassword())
            .param("confirmPassword", passwordB)
        );

        ResultActions alreadyExistEmailPerform = mockMvc.perform(post("/sign-up")
            .param("email", originEmail)
            .param("name", signUpForm.getName())
            .param("password", signUpForm.getPassword())
            .param("confirmPassword", signUpForm.getConfirmPassword())
        );

        //then
        successPerform.andExpect(status().is3xxRedirection());
        differentPasswordPerform.andExpect(status().isOk()).andExpect(view().name("add-user"));
        alreadyExistEmailPerform.andExpect(status().isOk()).andExpect(view().name("add-user"));
    }
}