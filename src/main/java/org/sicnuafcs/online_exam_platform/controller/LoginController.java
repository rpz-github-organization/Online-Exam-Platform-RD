package org.sicnuafcs.online_exam_platform.controller;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.loadtime.Aj;
import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.model.Login;
import org.sicnuafcs.online_exam_platform.service.Impl.LoginServiceImpl;
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
        request.getSession().setAttribute("userInfo",login1.getId()+"-"+login1.getAuthority()+"-"+"admin");

        return AjaxResponse.success(request.getSession().getAttribute("userInfo"));
    }

    //手机号加密码
    @PostMapping("/phone")
    public @ResponseBody AjaxResponse loginPhone(@Valid @RequestBody Login login, HttpServletRequest request) {
        Login login1 = loginService.loginPhone(login);

        //添加数据到session
        request.getSession().setAttribute("userInfo",login1.getId()+"-"+login1.getAuthority()+"-"+"admin");

        return AjaxResponse.success(request.getSession().getAttribute("userInfo"));
    }

    @PostMapping("/get/session")
    public @ResponseBody AjaxResponse get(HttpServletRequest request) {

        //获取session数据
        Object userInfo = request.getSession().getAttribute("userInfo");
        return AjaxResponse.success(userInfo);
    }


    @PostMapping("/logout")
    public @ResponseBody AjaxResponse logout(HttpServletRequest request) {

        Object userInfo = request.getSession().getAttribute("userInfo");
        if (userInfo == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户未登录");
        }
        Object userInfo1 = userInfo;

        //销毁session
        request.getSession().invalidate();
        userInfo = request.getSession().getAttribute("userInfo");
        if (userInfo != null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "登出失败");
        }
        return AjaxResponse.success(userInfo1);
    }


}
