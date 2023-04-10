package web.crud.basic.basicweb.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import web.crud.basic.basicweb.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class EditArticleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("loginUser");

        if (request.getParameter("writerId") == null || user.getId() != Long.parseLong(request.getParameter("writerId"))) {
            response.sendRedirect("/board/1");
            return false;
        }
        return true;
    }
}
