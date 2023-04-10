package web.crud.basic.basicweb.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import web.crud.basic.basicweb.domain.User;
import web.crud.basic.basicweb.domain.UserMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    private SignUpService signUpService;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setSignUpService() {
        this.signUpService = new SignUpService(userMapper);
    }

    @DisplayName("비밀번호, 비밀번호 확인")
    @Test
    void isDifferentPasswordTest() {
        //given
        final String password = "123test!";
        final String samePassword = password;
        final String diffPassword = "123diff!";

        //when
        boolean samePassResult = signUpService.isDifferentPassword(password, samePassword);
        boolean diffPassResult = signUpService.isDifferentPassword(password, diffPassword);

        //then
        assertEquals(false, samePassResult);
        assertEquals(true, diffPassResult);
    }

    @DisplayName("중복 이메일")
    @Test
    void isExistEmailTest() {
        //given
        final String newEmail = "newEmail@test.com";
        final String existEmail = "existEmail@test.com";
        User user = new User();
        user.setEmail(existEmail);
        user.setName("existEmail");
        user.setPassword("123existEmail!");

        given(userMapper.getUserByEmail(existEmail)).willReturn(user);
        given(userMapper.getUserByEmail(newEmail)).willReturn(null);

        //when
        boolean newResult = signUpService.isExistEmail(newEmail);
        boolean existResult = signUpService.isExistEmail(existEmail);

        //then
        assertEquals(true, existResult);
        assertEquals(false, newResult);
    }

}