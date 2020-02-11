package org.sicnuafcs.online_exam_platform.controller;

import lombok.extern.slf4j.Slf4j;

import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;

import org.sicnuafcs.online_exam_platform.config.exception.CustomException;

import org.sicnuafcs.online_exam_platform.service.HomePageService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@Controller
@RestController
@Slf4j
@RequestMapping("/homePage")
public class HomePageController {

    @Autowired
    HomePageService homePageService;

    /*学生首页*/
    //学号登录获取信息
    @RequestMapping("/stu/id")
    public @ResponseBody
    AjaxResponse findStuById(@PathVariable String stu_id,@PathVariable String status) {
        return AjaxResponse.success(homePageService.findStuById(stu_id,status));
    }



    //学生手机号码登录获取信息
    @RequestMapping("/stu/phone")
    public @ResponseBody
    AjaxResponse findStuByPhone(@PathVariable String phone,@PathVariable String status) {
        return AjaxResponse.success(homePageService.findStuByPhone(phone,status));

    }



    /*教师首页*/
    //工号号登录获取信息
    @RequestMapping("/tea/id")
    public @ResponseBody
    AjaxResponse findTeaById(@PathVariable String tea_id) {
        return AjaxResponse.success(homePageService.findTeaById(tea_id));

    }




    //教师手机号码登录获取信息
    @RequestMapping("/tea/phone")//通过教师id
    public @ResponseBody
    AjaxResponse findTeaByPhone(@PathVariable String phone) {
        return AjaxResponse.success(homePageService.findTeaByPhone(phone));

    }
}