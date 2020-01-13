package org.sicnuafcs.online_exam_platform.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.dao.Student;
import org.sicnuafcs.online_exam_platform.model.AjaxResponse;
import org.sicnuafcs.online_exam_platform.service.StudentRestServiceImpl;
import org.sicnuafcs.online_exam_platform.service.StudentRestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@Controller
@RequestMapping("/rest")
public class StudentRestController {

    @Resource(name = "studentRestServiceImpl")
    StudentRestService studentRestService;

    @ApiOperation(value = "添加学生",tags = "Student",httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code=200,message="成功",response= AjaxResponse.class),
            @ApiResponse(code=400,message="用户输入错误",response=AjaxResponse.class),
            @ApiResponse(code=500,message="系统内部错误",response=AjaxResponse.class)
    })

    @PostMapping("/student")
    public @ResponseBody AjaxResponse saveStudent(@RequestBody Student student){
          log.info("saveStudent :{}",student);
          log.info("studentRestService return :"+studentRestService.saveStudent(student));
          return AjaxResponse.success(student);
    }

    @DeleteMapping("/student/{stu_id}")
    public @ResponseBody AjaxResponse deleteStudent(@PathVariable String stu_id){

        studentRestService.deleteStudent(stu_id);

        return AjaxResponse.success(stu_id);
    }

    @PutMapping("/student/{stu_id}")
    public @ResponseBody AjaxResponse updateArticle(@PathVariable String stu_id,@RequestBody Student student){
        student.setStu_id(stu_id);

        studentRestService.updateStudent(student);

        return AjaxResponse.success(student);
    }

    @GetMapping("/student/{stu_id}")
    public @ResponseBody AjaxResponse getStudent(@PathVariable String stu_id){

        return AjaxResponse.success(studentRestService.getStudent(stu_id));
    }



































}
