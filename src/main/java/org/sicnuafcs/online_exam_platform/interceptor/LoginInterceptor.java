package org.sicnuafcs.online_exam_platform.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Slf4j
@EnableRedisHttpSession
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        log.info("登录状态拦截");
        HttpSession session = request.getSession();
        log.info("userInfo:"+session.getAttribute("userInfo"));
        //获取用户信息 如果没有用户信息就提示没有登录
        Object userInfo = session.getAttribute("userInfo");
            if (userInfo == null) {
            log.info("没有登录");
//            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "没有登录或登录失败");
            response.sendError(401,"没有登录或登陆失败");
            return false;
        }else {
            log.info("已登录，用户信息："+userInfo);
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }

}
