package web.crud.basic.basicweb;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import web.crud.basic.basicweb.interceptor.EditArticleInterceptor;
import web.crud.basic.basicweb.interceptor.IsLoginUserInterceptor;
import web.crud.basic.basicweb.interceptor.LoginCheckInterceptor;
import web.crud.basic.basicweb.interceptor.LogoutInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IsLoginUserInterceptor())
                .order(1)
                .addPathPatterns("/board/{[0-9]}", "/board/article/{[0-9]}");
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/logout", "/sign-up", "/board/{[0-9]}", "/board/article/{[0-9]}", "/css/bootstrap.min.css");
        registry.addInterceptor(new LogoutInterceptor())
                .order(3)
                .addPathPatterns("/board");
        registry.addInterceptor(new EditArticleInterceptor())
                .order(4)
                .addPathPatterns("/board/edit/**");

    }
}
