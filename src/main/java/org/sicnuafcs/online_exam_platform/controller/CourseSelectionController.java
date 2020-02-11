package org.sicnuafcs.online_exam_platform.controller;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.model.Course;
import org.sicnuafcs.online_exam_platform.model.GetCourse;
import org.sicnuafcs.online_exam_platform.service.CourseSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/course/selection")
public class CourseSelectionController {
    @Autowired
    CourseSelectionService courseSelectionService;


    /**
     * 获取选课中心页面需要展示的东西
     * 班级号（需要考虑到学院？） 专业名
     * 可选的课程
     * 已选的课程
     * 以<课程名，老师>确定一门课
     * @param map
     * @return
     */
    @RequestMapping("/get")
    public @ResponseBody
    AjaxResponse getCourse(@RequestBody Map map) {
        String stu_id = map.values().toString().substring(1, map.values().toString().length()-1);
        if (stu_id.equals(null) || stu_id.equals("")) {
            log.info("学号为空");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "学号为空");

        }
        String class_id = courseSelectionService.getClass_id(stu_id);
        String major_id = courseSelectionService.getMajor_id(stu_id);
        String major = courseSelectionService.getMajorName(major_id);   //到major类中获取name

        Map<String, Object> map1 = new HashMap<>();
        map1.put("stu_id", stu_id);
        map1.put("class_id", class_id);
        map1.put("major", major);
        return AjaxResponse.success(map1);
    }

    @RequestMapping("/get/Course")
    public @ResponseBody
    AjaxResponse getAlternativeCourse(@RequestBody GetCourse getCourse) {
        String stu_id = getCourse.getStu_id();
        String major_id = courseSelectionService.getMajor_id(stu_id);

        Map<String, String> chosenCoId_TeaId = courseSelectionService.getChosenCoId_TeaId(stu_id);  //在stu_co表里获取学生 已经选择 的课程和老师，co_id和tea_id;

        if (getCourse.getOption() == 1) {    //查询已选课程

            //chosenCoId_TeaId转化为name
            return AjaxResponse.success(courseSelectionService.getName(chosenCoId_TeaId));
        }
        else if (getCourse.getOption() == 0) {
            ArrayList<String> allCourse_id = courseSelectionService.getAllCourse_id(major_id);  //在course表里获取专业major_id对应的全部课程co_id
            Map<String, String> allCoId_TeaId = courseSelectionService.getAllCoId_TeaId(allCourse_id);  //在tea_co类中 全部课程 获取tea_id，与co_id(地址)对应成map

            Set<String> chosenCourseList = new HashSet<>(chosenCoId_TeaId.keySet());    //不能去掉HashSet（但是本身里面也不可能有重复的） 原理？？？
            Set<String> alternativeCourseList = allCoId_TeaId.keySet();
            alternativeCourseList.removeAll(chosenCourseList);       //执行removeAll时用的是equals方法

            //allCoId_TeaId转化为name
            return AjaxResponse.success(courseSelectionService.getName(allCoId_TeaId));
        }
        throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "选项错误");
    }


    @RequestMapping("/add")
    public @ResponseBody
    AjaxResponse add(@RequestBody Course course) {
        Course course1 = courseSelectionService.add(course);
        return AjaxResponse.success(course1);
    }
}
