package org.sicnuafcs.online_exam_platform.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.sicnuafcs.online_exam_platform.dao.Student;
import org.sicnuafcs.online_exam_platform.dao.StudentRepository;
import org.sicnuafcs.online_exam_platform.dao.Teacher;
import org.sicnuafcs.online_exam_platform.dao.TeacherRepository;
import org.sicnuafcs.online_exam_platform.model.AjaxResponse;
import org.sicnuafcs.online_exam_platform.service.StudentRestService;
import org.sicnuafcs.online_exam_platform.service.TeacherRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/register")
public class RegisterController {

    @Resource(name = "studentRestServiceImpl")
    StudentRestService studentRestService;
    @Resource(name = "teacherRestServiceImpl")
    TeacherRestService teacherRestService;

    @Resource
    private Mapper dozerMapper;

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;

    @ApiOperation(value = "添加",tags = "Student",httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code=200,message="成功",response= AjaxResponse.class),
            @ApiResponse(code=400,message="用户输入错误",response=AjaxResponse.class),
            @ApiResponse(code=500,message="系统内部错误",response=AjaxResponse.class)
    })

    @PostMapping("/student")
    public @ResponseBody AjaxResponse saveStudent(@RequestBody Student student){
        String id=student.getStu_id();
        Optional<Student> studentList=studentRepository.findById(id);
        if(studentList.isPresent()==false){
            Student student1=dozerMapper.map(student,Student.class);
            studentRestService.saveStudent(student1);
            return AjaxResponse.success(student1);
        }
        else {
            return AjaxResponse.isEmpty();
        }
    }

    @PostMapping("/teacher")
    public @ResponseBody AjaxResponse saveTeacher(@RequestBody Teacher teacher) {
        String id = teacher.getTea_id();
        Optional<Teacher> teacherList = teacherRepository.findById(id);
        if (teacherList.isPresent() == false) {
            Teacher teacher1=dozerMapper.map(teacher,Teacher.class);
            teacherRestService.saveTeacher(teacher);
            return AjaxResponse.success(teacher);
        } else {
            return AjaxResponse.isEmpty();
        }
    }


}
