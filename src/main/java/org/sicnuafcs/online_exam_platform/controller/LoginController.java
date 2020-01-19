package org.sicnuafcs.online_exam_platform.controller;

        import lombok.extern.slf4j.Slf4j;
        import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
        import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
        import org.sicnuafcs.online_exam_platform.model.Student;
        import org.sicnuafcs.online_exam_platform.dao.StudentRepository;
        import org.sicnuafcs.online_exam_platform.model.Teacher;
        import org.sicnuafcs.online_exam_platform.dao.TeacherRepository;
        import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
        import org.sicnuafcs.online_exam_platform.service.StudentRestService;
        import org.sicnuafcs.online_exam_platform.service.TeacherRestService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.*;

        import javax.annotation.Resource;
        import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {
    @Resource(name = "studentRestServiceImpl")
    StudentRestService studentRestService;
    @Resource(name = "teacherRestServiceImpl")
    TeacherRestService teacherRestService;

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;

    //学号/工号加密码
    @GetMapping("/student/stu_id/{stu_id}/{password}")
    public @ResponseBody
    AjaxResponse loginIdStudent(@PathVariable String stu_id, @PathVariable String password) {
        Optional<Student> studentList = studentRepository.findById(stu_id);
        if (studentList.isPresent()) {      //如果数据库里面已存在
            if (studentList.get().getPassword().equals(password)) {
                return AjaxResponse.success();      //登陆成功
            } else {
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "密码错误");     //密码错误
            }
        } else {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户不存在");      //用户不存在
        }
    }

    @GetMapping("/teacher/tea_id/{tea_id}/{password}")
    public @ResponseBody
    AjaxResponse loginIdTeacher(@PathVariable String tea_id, @PathVariable String password) {
        Optional<Teacher> teacherList = teacherRepository.findById(tea_id);
        if (teacherList.isPresent()) {
            if (teacherList.get().getPassword().equals(password)) {
                return AjaxResponse.success();
            } else {
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "密码错误");     //密码错误
            }
        } else {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户不存在");      //用户不存在
        }
    }


    //手机号加密码
    @GetMapping("/student/phone/{telephone}/{password}")
    public @ResponseBody
    AjaxResponse loginPhoneStudent(@PathVariable String telephone, @PathVariable String password) {
        Optional<Student> studentList = studentRepository.findByTelephone(telephone);
        if (studentList.isPresent()) {      //如果数据库里面已存在
            if (studentList.get().getPassword().equals(password)) {
                return AjaxResponse.success();      //登陆成功
            } else {
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "密码错误");     //密码错误
            }
        } else {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户不存在");      //用户不存在
        }
    }

    @GetMapping("/teacher/phone/{telephone}/{password}")
    public @ResponseBody
    AjaxResponse loginPhoneTeacher(@PathVariable String telephone, @PathVariable String password) {
        Optional<Teacher> teacherList = teacherRepository.findByTelephone(telephone);
        if (teacherList.isPresent()) {
            if (teacherList.get().getPassword().equals(password)) {
                return AjaxResponse.success();
            } else {
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "密码错误");     //密码错误
            }
        } else {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户不存在");      //用户不存在
        }
    }
}
