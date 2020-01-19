package org.sicnuafcs.online_exam_platform.controller;

import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.model.CheckEmail;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.dao.StudentRepository;
import org.sicnuafcs.online_exam_platform.model.Teacher;
import org.sicnuafcs.online_exam_platform.dao.TeacherRepository;
import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
import org.sicnuafcs.online_exam_platform.service.SendMailService;
import org.sicnuafcs.online_exam_platform.service.StudentRestService;
import org.sicnuafcs.online_exam_platform.service.TeacherRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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

    @Autowired
    SendMailService sendMailService;


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

        Optional<Student> stuidList=studentRepository.findById(student.getStu_id());
        //如果用户已存在
        if(stuidList.isPresent()){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户已存在");
        }
        //如果用户不存在

        //验证手机号 电子邮箱等是否存在
        Optional<Student> emailList=studentRepository.findByEmail(student.getEmail());
        if (emailList.isPresent()){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该邮箱已被注册");
        }
        Optional<Student> phoneList=studentRepository.findByTelephone(student.getTelephone());
        if(phoneList.isPresent()){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该电话已被注册");
        }

        //假设其他验证做完

        Student student1=dozerMapper.map(student,Student.class);
        studentRestService.saveStudent(student1);
        return AjaxResponse.success();
    }

    /**
     * 教师注册
     * @param teacher
     * @return
     */
    @PostMapping("/teacher")
    public @ResponseBody AjaxResponse saveTeacher(@Valid @RequestBody Teacher teacher) {
        Optional<Teacher> teacherList=teacherRepository.findById(teacher.getTea_id());
        if(teacherList.isPresent()){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户已存在");
        }

        //验证手机号 电子邮箱等是否存在
        Optional<Teacher> emailList=teacherRepository.findByEmail(teacher.getEmail());
        if (emailList.isPresent()){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该邮箱已被注册");
        }
        Optional<Teacher> phoneList=teacherRepository.findByTelephone(teacher.getTelephone());
        if(phoneList.isPresent()){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该电话已被注册");
        }

        //假设其他验证做完

        //教师端邮箱验证：验证码和邮箱是否一致；
        teacher.setStatus(0);
        CheckEmail checkEmail=new CheckEmail(teacher.getEmail(),teacher.getCode());
        if(!sendMailService.verfication(checkEmail)){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱验证失败");
        }

        //验证成功 把teacher的数据存入数据库
        Teacher teacher1=dozerMapper.map(teacher,Teacher.class);

        teacher1.setStatus(1);
        teacherRestService.saveTeacher(teacher1);

        return AjaxResponse.success();
    }


    /**
     * 发邮件
     * @param receiver
     * @return
     */
    @RequestMapping("/email/{receiver}")
    public @ResponseBody AjaxResponse generateCode(@PathVariable String receiver){
        //如果邮箱格式不正确（正则表达式验证）
        if (!receiver.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱格式不正确");
        }

        /**
         * 判断该邮箱在teacher表里是否存在
         */

        //如果邮箱在teacher表里已存在
        if(teacherRepository.findByEmail(receiver).isPresent()){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该邮箱已存在");
        }

        //如果该邮箱不存在，且该邮箱不为空 则向该邮箱发送验证码邮件

        if(!receiver.equals(null)){
            sendMailService.sendEmail(receiver);
        }
        else{
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,"邮箱号为空");
        }
        return AjaxResponse.success();
    }

}
