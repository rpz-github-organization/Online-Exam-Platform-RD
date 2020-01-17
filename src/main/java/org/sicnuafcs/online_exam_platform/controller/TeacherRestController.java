package org.sicnuafcs.online_exam_platform.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.dao.Teacher;
import org.sicnuafcs.online_exam_platform.dao.TeacherRepository;
import org.sicnuafcs.online_exam_platform.model.AjaxResponse;
import org.sicnuafcs.online_exam_platform.service.TeacherRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;


/*
教师表的增删改查
仅作参考 可以调试
跟实际项目无关
 */
@Slf4j
@Controller
@RequestMapping("/rest")
public class TeacherRestController {

    @Resource(name = "teacherRestServiceImpl")
    TeacherRestService teacherRestService;

    @Autowired
    TeacherRepository teacherRepository;

    @ApiOperation(value = "添加老师",tags = "Teacher",httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code=200,message="成功",response= AjaxResponse.class),
            @ApiResponse(code=400,message="用户输入错误",response=AjaxResponse.class),
            @ApiResponse(code=500,message="系统内部错误",response=AjaxResponse.class)
    })

    @PostMapping("/teacher")
    public @ResponseBody AjaxResponse saveTeacher(@RequestBody Teacher teacher) {
        log.info("saveTeacher :{}",teacher);
        log.info("teacherRestService return :"+teacherRestService.saveTeacher(teacher));
        return AjaxResponse.success(teacher);
    }

    @DeleteMapping("/teacher/{tea_id}")
    public @ResponseBody AjaxResponse deleteTeacher(@PathVariable String tea_id){

        teacherRestService.deleteTeacher(tea_id);

        return AjaxResponse.success(tea_id);
    }

    @PutMapping("/teacher/{tea_id}")
    public @ResponseBody AjaxResponse updateArticle(@PathVariable String tea_id,@RequestBody Teacher teacher){
        teacher.setTea_id(tea_id);

        teacherRestService.updateTeacher(teacher);

        return AjaxResponse.success(teacher);
    }

    @GetMapping("/teacher/{tea_id}")
    public @ResponseBody AjaxResponse getTeacher(@PathVariable String tea_id){

        return AjaxResponse.success(teacherRestService.getTeacher(tea_id));
    }



































}
