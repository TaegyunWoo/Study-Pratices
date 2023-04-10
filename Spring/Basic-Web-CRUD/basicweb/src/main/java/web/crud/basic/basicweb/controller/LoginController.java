package web.crud.basic.basicweb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.crud.basic.basicweb.domain.User;
import web.crud.basic.basicweb.domain.UserMapper;
import web.crud.basic.basicweb.form.LoginForm;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.http.HttpHeaders;

/**
 * 로그인 관련 컨트롤러
 */

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {

    private final UserMapper userMapper;

    @GetMapping("/")
    public String viewLogin(@ModelAttribute("user") LoginForm form,
                            @SessionAttribute(value = "loginUser", required = false) User user) {
        if (user == null) {
            return "home";
        } else {
            return "redirect:/board/1";
        }

    }

    @PostMapping("/")
    public String login(@Validated @ModelAttribute("user") LoginForm form,
                        BindingResult bindingResult,
                        HttpServletRequest request) {

        User user;

        if (bindingResult.hasFieldErrors()) {
            return "home";
        }

        user = getLoginUser(form.getEmail(), form.getPassword());

        if (user == null) {
            bindingResult.reject("wrongLoginInfo", null, null);
        }

        if (bindingResult.hasErrors()) {
            return "home";
        }

        //세션 생성
        HttpSession session = request.getSession(true);
        session.setAttribute("loginUser", user);

        return "redirect:/board/1";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("loginUser");
            log.info("loginUser 속성 삭제");
        }
        return "redirect:/";
    }

    /**
     *
     * @param email 로그인을 시도하는 email
     * @param password 로그인을 시도하는 password
     * @return null 로그인 실패
     * @return user 로그인 성공한 user의 정보
     */
    private User getLoginUser(String email, String password) {
        User user = userMapper.getUserByEmailAndPassword(email, password);
        log.info("user: ", user);
        return user;
    }

}
