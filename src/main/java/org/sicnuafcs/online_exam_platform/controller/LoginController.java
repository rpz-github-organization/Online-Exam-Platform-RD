package org.sicnuafcs.online_exam_platform.controller;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.model.Login;
import org.sicnuafcs.online_exam_platform.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    LoginService loginService;

    //学号或者工号加密码
    @PostMapping("/id")
    public @ResponseBody
    AjaxResponse loginId(@Valid @RequestBody Login login, HttpServletRequest request) {
        Login login1 = loginService.LoginId(login);

        //添加数据到session
        request.getSession().setAttribute(login.getId(), request.getSession().getId());

        //添加sessionID到map
        Map<String, Object> map = new HashMap<>();
        map.put(login.getId(), request.getSession().getId());

        return AjaxResponse.success(map);
    }

    //手机号加密码
    @PostMapping("/phone")
    public @ResponseBody AjaxResponse loginPhone(@Valid @RequestBody Login login, HttpServletRequest request) {
        Login login1 = loginService.loginPhone(login);

        //添加数据到session(保存到session和redis)
        request.getSession().setAttribute(login.getId(), request.getSession().getId());

        //添加sessionID到map（传给前端）
        Map<String, Object> map = new HashMap<>();
        map.put(login.getId(), request.getSession().getId());

        return AjaxResponse.success(map);
    }

    @PostMapping("/get/session")
    public @ResponseBody AjaxResponse get(@RequestBody Map map, HttpServletRequest request) {
        //从map中获取id
        String id = map.values().toString().substring(1,map.values().toString().length()-1);
        //获取session数据(sessionId)
        Object sessionId = request.getSession().getAttribute(id);

        Map<String, Object> map1 = new HashMap<>();
        map1.put(id, sessionId);
        return AjaxResponse.success(map1);
    }

    @PostMapping("/logout")
    public @ResponseBody AjaxResponse logout(@RequestBody Map map, HttpServletRequest request) {
        //从map中获取id
        String id = map.values().toString().substring(1,map.values().toString().length()-1);
        //登出前先检查用户是否已登录
        Object sessionId = request.getSession().getAttribute(id);
        if (sessionId == null) {
            log.info("用户未登录");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户未登录");
        }
        Object sessionId1 = sessionId;

        //销毁session
        request.getSession().removeAttribute(id);
        sessionId = request.getSession().getAttribute(id);
        if (sessionId != null) {
            log.info("登出失败");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "登出失败");
        }
        log.info("登出成功");
        Map<String, Object> map1 = new HashMap<>();
        map1.put(id, sessionId1);
        return AjaxResponse.success(map1);
    }

}
