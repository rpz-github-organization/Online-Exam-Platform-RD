package org.sicnuafcs.online_exam_platform.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.springframework.lang.Nullable;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableRedisHttpSession
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        //获取参数部分 获得id
        String query = request.getQueryString();
        String id = query.substring(3);

        log.info("登录状态拦截");

        HttpSession session = request.getSession();
        log.info("session:"+session.getAttribute(id));

        //获取用户信息 如果没有用户信息就提示没有登录
        Object sessionId = session.getAttribute(id);
        if (sessionId == null) {
            log.info("没有登录");
//            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "没有登录或登录失败");


//            response.setContentType("text/html;charset=utf-8");
//            response.setStatus(400);
//            response.setHeader("message","没有登录或登陆失败");  //乱码未解决

            response.sendError(400,"没有登录或登陆失败");

            //重定向到登录页面
//            response.sendRedirect(request.getContextPath() + loginUrl);
            return false;
        }
        else {
            Map<String, Object> map = new HashMap<>();
            map.put(id, sessionId);
            log.info("已登录，用户信息："+map);

            return true;
        }

//        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }

}
