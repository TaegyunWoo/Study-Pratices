package web.crud.basic.basicweb.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.crud.basic.basicweb.domain.User;
import web.crud.basic.basicweb.domain.UserMapper;

@RequiredArgsConstructor
@Service
public class SignUpService {

    private final UserMapper userMapper;

    public boolean isDifferentPassword(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return false;
        }
        return true;
    }

    /**
     * @param email 중복 확인할 이메일
     * @return false 중복X
     * @return true 중복O
     */
    public boolean isExistEmail(String email) {
        User user = userMapper.getUserByEmail(email);
        if (user == null) {
            return false;
        }
        return true;
    }


    @Transactional
    public void addUser(User user) {
        userMapper.insertUser(user);
    }

}
