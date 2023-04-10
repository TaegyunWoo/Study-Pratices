package web.crud.basic.basicweb.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import web.crud.basic.basicweb.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class IsLoginUserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("loginUser") == null) {
            request.setAttribute("isLoginUser", false);
        } else {
            request.setAttribute("isLoginUser", true);
        }
        return true;
    }

}
