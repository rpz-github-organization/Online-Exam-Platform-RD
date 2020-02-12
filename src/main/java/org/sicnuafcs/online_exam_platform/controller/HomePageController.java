package org.sicnuafcs.online_exam_platform.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;

import org.sicnuafcs.online_exam_platform.config.exception.CustomException;

import org.sicnuafcs.online_exam_platform.model.Course;
import org.sicnuafcs.online_exam_platform.model.Exam;
import org.sicnuafcs.online_exam_platform.service.HomePageService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    AjaxResponse findStuById(@RequestBody String str) {
        //Long exam_id, String stu_id
        String stu_id = JSON.parseObject(str).get("stu_id").toString();
        Integer status = Integer.valueOf(JSON.parseObject(str).get("status").toString());
        List<Exam> json = homePageService.findStuById(stu_id,status);
        return AjaxResponse.success(json);

    }


    /*教师首页*/
    //工号号登录获取信息
    @RequestMapping("/tea/id")
    public @ResponseBody
    AjaxResponse findTeaById(@RequestBody String str) {
        String tea_id = JSON.parseObject(str).get("tea_id").toString();
        List<Course> json = homePageService.findTeaById(tea_id);
        return AjaxResponse.success(json);

    }

}