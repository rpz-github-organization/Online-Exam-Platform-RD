//package org.sicnuafcs.online_exam_platform.controller;
//
//import lombok.extern.slf4j.Slf4j;
//import org.sicnuafcs.online_exam_platform.model.Student;
//import org.sicnuafcs.online_exam_platform.dao.StudentRepository;
//import org.sicnuafcs.online_exam_platform.model.Teacher;
//import org.sicnuafcs.online_exam_platform.dao.TeacherRepository;
//import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
//import org.sicnuafcs.online_exam_platform.service.StudentRestService;
//import org.sicnuafcs.online_exam_platform.service.TeacherRestService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import java.util.Optional;
//
//@Slf4j
//@Controller
//@RequestMapping("/login")
//public class LoginController {
//    @Resource(name = "studentRestServiceImpl")
//    StudentRestService studentRestService;
//    @Resource(name = "teacherRestServiceImpl")
//    TeacherRestService teacherRestService;
//
//    @Autowired
//    StudentRepository studentRepository;
//    @Autowired
//    TeacherRepository teacherRepository;
//
//    @GetMapping("/student/{stu_id}/{password}")
//    public @ResponseBody AjaxResponse loginStudent(@PathVariable String stu_id,@PathVariable String password){
//        Optional<Student> studentList=studentRepository.findById(stu_id);
//        if(studentList.isPresent()==true){      //如果数据库里面已存在
//            if(studentList.get().getPassword().equals(password)){
//                return AjaxResponse.success();      //登陆成功
//            }
//            else{
////                return AjaxResponse.passworderror(studentList.get());     //密码错误
//            }
//        }
//        else{
////            return AjaxResponse.nouser();      //用户不存在
//        }
//    }
//
//    @GetMapping("/teacher/{tea_id}/{password}")
//    public @ResponseBody AjaxResponse loginTeacher(@PathVariable String tea_id,@PathVariable String password){
//        Optional<Teacher> teacherList=teacherRepository.findById(tea_id);
//        if(teacherList.isPresent()==true){
//            if(teacherList.get().getPassword().equals(password)){
//                return AjaxResponse.success();
//            }
//            else{
//
//            }
//        }
//        else{
//
//        }
//    }
//}
