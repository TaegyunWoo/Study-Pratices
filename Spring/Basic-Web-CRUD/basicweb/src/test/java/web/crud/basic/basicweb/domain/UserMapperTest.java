package web.crud.basic.basicweb.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private User user = new User();

    @BeforeEach
    void initUser() {
        user.setName("MybatisTest");
        user.setEmail("MybatisTest@Test.com");
        user.setPassword("123test!");
    }

    @DisplayName("Select user by email and password")
    @Test
    void getUserByEmailAndPasswordTest() {
        //given
        String email = "MybatisTest@Test.com";
        String password = "123test!";
        User getUser;

        int isInserted = userMapper.insertUser(user);

        //when
        getUser = userMapper.getUserByEmailAndPassword(email, password);

        //then
        assertEquals(1, isInserted);
        assertNotEquals(null, getUser);
        assertEquals(email, getUser.getEmail());
    }

    @DisplayName("Select by email")
    @Test
    void getUserByEmail() {
        //given
        String email = "MybatisTest@Test.com";
        User getUser;
        int isInserted = userMapper.insertUser(user);

        //when
        getUser = userMapper.getUserByEmail(email);

        //then
        assertEquals(1, isInserted);
        assertNotEquals(null, getUser);
        assertEquals(email, getUser.getEmail());
    }

    @DisplayName("Select by id")
    @Test
    void getUserByIdTest() {
        //given
        User getUser;

        int isInserted = userMapper.insertUser(user);

        //when
        getUser = userMapper.getUserById(user.getId());

        //then
        assertEquals(1, isInserted);
        assertNotEquals(null, getUser);
        assertEquals("MybatisTest", getUser.getName());
    }

    @DisplayName("Insert user")
    @Test
    void insertUserTest() {
        //given
        User getUser;

        //when
        int isInserted = userMapper.insertUser(user);

        //then
        assertEquals(1, isInserted);
    }
}