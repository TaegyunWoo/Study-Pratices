package web.crud.basic.basicweb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import web.crud.basic.basicweb.Service.SignUpService;
import web.crud.basic.basicweb.domain.User;
import web.crud.basic.basicweb.form.SignUpForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SignUpController {

    private final SignUpService signUpService;

    @GetMapping("/sign-up")
    public String showSignUp(@ModelAttribute("user") SignUpForm form) {
        return "add-user";
    }

    @PostMapping("/sign-up")
    public String signUp(@Validated @ModelAttribute("user") SignUpForm form,
                         BindingResult bindingResult,
                         HttpServletRequest request) {

        /*
         비밀번호 일치 확인
         */
        if (signUpService.isDifferentPassword(form.getPassword(), form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword","differentPassword");
        }

        /*
         중복 이메일 검사
         */
        if (signUpService.isExistEmail(form.getEmail())) {
            bindingResult.rejectValue("email", "sameEmail", new Object[] {form.getEmail()}, null);
        }
        if (bindingResult.hasErrors()) {
            return "add-user";
        }

        User newUser = new User();
        newUser.setEmail(form.getEmail());
        newUser.setName(form.getName());
        newUser.setPassword(form.getPassword());
        signUpService.addUser(newUser);

        log.info("{}", newUser.getId());

        //세션 생성
        HttpSession session = request.getSession(true);
        session.setAttribute("loginUser", newUser);

        return "redirect:/";
    }

}
