package org.sicnuafcs.online_exam_platform.controller;

import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.CheckRepository;
import org.sicnuafcs.online_exam_platform.model.Check;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.dao.StudentRepository;
import org.sicnuafcs.online_exam_platform.model.Teacher;
import org.sicnuafcs.online_exam_platform.dao.TeacherRepository;
import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
import org.sicnuafcs.online_exam_platform.service.EmailService;
import org.sicnuafcs.online_exam_platform.service.StudentRestService;
import org.sicnuafcs.online_exam_platform.service.TeacherRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Optional;

import static org.sicnuafcs.online_exam_platform.util.VerCodeGenerateUtil.generateVerCode;

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
    @Autowired
    CheckRepository checkRepository;

    @Autowired
    EmailService emailService;


    @PostMapping("/student")
    public @ResponseBody AjaxResponse saveStudent(@Valid @RequestBody Student student) throws Exception{
        /*
        逻辑：（假设判空已经实现 后台还是要做判空 防止有人绕过前端直接访问后端）
        先判断传过来的数据是否为空（@NotBlank注解）
        不为空的话再判断是否已经存在（逻辑：if循环，如果已存在的话则抛出400异常）
        如果不存在的话再判断格式是否正确:
            学号/工号的格式
            姓名的格式
            邮箱（只用@valid注解 判空和判断格式没有先后之分 是随机顺序）
            电话
            密码
            （如果不是注册时候应该还要看其他参数的格式 比如说性别之类的）

        如果格式都正确的话 并且是教师端的话就验证电子邮箱是否正确 学生端不用：
            电子邮件验证注册功能细化：
            发送验证码
        如果电子邮箱正确的话就返回注册成功
         */

        String id=student.getStu_id();
        Optional<Student> studentList=studentRepository.findById(id);
        //如果用户已存在
        if(studentList.isPresent()==true){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户已存在");
        }
        //如果用户不存在

        //假设其他验证做完
        Student student1=dozerMapper.map(student,Student.class);
        studentRestService.saveStudent(student1);
        return AjaxResponse.success();
    }


    @PostMapping("/teacher")
    public @ResponseBody AjaxResponse saveTeacher(@Valid @RequestBody Teacher teacher) {
        String id=teacher.getTea_id();
        Optional<Teacher> teacherList=teacherRepository.findById(id);
        if(teacherList.isPresent()==true){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户已存在");
        }

        //假设其他验证做完

        //教师端邮箱验证
        Optional<Check> checks=checkRepository.findByCheckEmail(teacher.getEmail());
        if(checks.isPresent()==false){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"信息为空");
        }
        Check check=checks.get();
        teacher.setStatus(0);
        if(!(teacher.getEmail().equals(check.getCheckEmail())&&teacher.getCode().equals(check.getCheckCode()))&&(!check.getCheckEmail().equals(null)&&(!check.getCheckCode().equals(null)))){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"验证码不正确");
        }
        Teacher teacher1=dozerMapper.map(teacher,Teacher.class);
        teacherRestService.saveTeacher(teacher1);
        teacher.setStatus(1);
        return AjaxResponse.success();
    }


    @RequestMapping("/email/{receiver}")
    public @ResponseBody AjaxResponse generateCode(@PathVariable String receiver){
        //如果邮箱格式不正确（正则表达式验证）
        if (!receiver.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"email格式不正确");
        }

        /**
         * 判断该邮箱在teacher表里是否存在
         */

        if(teacherRepository.findByEmail(receiver).isPresent()==true){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"email已存在");
        }

        //如果该邮箱不存在
        //判断邮箱在email_code表里是否存在 因为可能重复验证 要更新表里面的code和email的对应关系
        if(!receiver.equals(null)){
            Check check=new Check();
            check.setCheckEmail(receiver);
            try {
                check.setCheckCode(generateVerCode());
                emailService.sendEmailVerCode(check.getCheckEmail(),check.getCheckCode());
            }catch (Exception e){
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR,"email发送失败");
            }
            checkRepository.save(check);
        }
        else{
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,"email为空");
        }

        return AjaxResponse.success();
    }

}
