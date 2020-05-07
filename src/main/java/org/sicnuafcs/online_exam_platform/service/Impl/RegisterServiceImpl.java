package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.mindrot.jbcrypt.BCrypt;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.MajorInstitudeRepository;
import org.sicnuafcs.online_exam_platform.dao.StudentRepository;
import org.sicnuafcs.online_exam_platform.dao.TeacherRepository;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.model.Teacher;
import org.sicnuafcs.online_exam_platform.service.RegisterService;
import org.sicnuafcs.online_exam_platform.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.NonUniqueResultException;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {
    @Resource
    private Mapper dozerMapper;

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    SendMailService sendMailService;
    @Autowired
    MajorInstitudeRepository majorInstitudeRepository;


    @Override
    public void saveStudent(Student student) throws Exception {
        String stu_id = student.getStu_id();
        Student stu = studentRepository.findStudentByStu_id(stu_id);
        //用户已存在
        if (stu != null) {
            log.info("用户已存在");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户已存在");
        }

        //学号长度固定为10位 密码不少于8位
        if (student.getStu_id().length() != 10) {
            log.info("学号位数错误");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"学号位数错误");
        }
        if (student.getPassword().length() < 8) {
            log.info("密码位数过少");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"密码位数过少");
        }

        //邮箱格式
        if (!student.getEmail().matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            log.info("邮箱格式不正确");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱格式不正确");
        }
//
//        //验证手机号（老师学生表里是否唯一） 电子邮箱是否存在
//        Student stu1 = studentRepository.findStudentByEmail(student.getEmail());
//        if (stu1 != null) {
//            log.info("该邮箱已被注册");
//            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该邮箱已被注册");
//        }
//
//        Student stu2 = studentRepository.findStudentByTelephone(student.getTelephone());
//        Teacher tea2 = teacherRepository.findTeacherByTelephone(student.getTelephone());
//        if (stu2 != null || tea2 != null) {
//            log.info("该电话已被注册");
//            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该电话已被注册");
//        }


//        //邮箱验证：验证码和邮箱是否一致；
//        if(!sendMailService.verification(student.getEmail(),student.getCode())){
//            log.info("邮箱验证不一样");
//            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱验证失败");
//        }

        //密码加密存储
        try {
            student.setPassword(BCrypt.hashpw(student.getPassword(),BCrypt.gensalt()));
            log.info("密码加密成功");
        }catch (Exception e){
            log.info("密码加密失败");
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,"密码加密失败");
        }

        //存入数据库
        student.setAuthority(0);

        //设置class_id institude_id major_id
        int grade = Integer.parseInt(stu_id.substring(0,4));
        String institude_id = stu_id.substring(4,6);
        String class_id = stu_id.substring(6,8);
        System.out.println(grade + " " + institude_id + " " + class_id);
        student.setInstitute_id(institude_id);
        student.setClass_id(class_id);
        student.setGrade(grade);

        String major_id = majorInstitudeRepository.findMajorIdByClass_idAndGradeAndInstitude_id(class_id, grade, institude_id);
        student.setMajor_id(major_id);


        studentRepository.save(student);

    }

    @Override
    public void saveTeacher(Teacher teacher) throws Exception {
        Teacher tea = teacherRepository.findTeacherByTea_id(teacher.getTea_id());
        if (tea != null) {
            log.info("用户已存在");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户已存在");
        }

        //工号长度固定为10位 密码不少于8位
        if (teacher.getTea_id().length() != 8) {
            log.info("工号位数错误");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"工号位数错误");
        }
        if (teacher.getPassword().length() < 8) {
            log.info("密码位数过少");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"密码位数过少");
        }

        //邮箱格式验证
        String result = teacher.getEmail().substring(teacher.getEmail().length()-13,teacher.getEmail().length());
        if (!(teacher.getEmail().matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+") && result.equals("@edu.cn"))) {
            log.info("邮箱格式不正确");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱格式不正确");
        }

        //验证手机号(老师学生表里唯一) 电子邮箱等是否存在
        Teacher tea1 = teacherRepository.findTeacherByEmail(teacher.getEmail());
        if (tea1 != null) {
            log.info("该邮箱已被注册");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该邮箱已被注册");
        }
        Student stu2 = studentRepository.findStudentByTelephone(teacher.getTelephone());
        Teacher tea2 = teacherRepository.findTeacherByTelephone(teacher.getTelephone());
        if (stu2 != null || tea2 != null) {
            log.info("该电话已被注册");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该电话已被注册");
        }

        //邮箱验证：验证码和邮箱是否一致；
        if (!sendMailService.verification(teacher.getEmail(),teacher.getCode())) {
            log.info("邮箱验证不一致");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱验证失败");
        }

        //密码存储加密
        try {
            teacher.setPassword(BCrypt.hashpw(teacher.getPassword(), BCrypt.gensalt()));
            log.info("密码加密成功");
        }catch (Exception e) {
            log.info("密码加密失败");
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,"密码加密失败");
        }

        //验证成功 把teacher的数据存入数据库
        teacher.setAuthority(1);
        teacherRepository.save(teacher);       //存入数据库

    }

    @Override
    public void checkTeacherRepeat(String email) {
        //如果邮箱在teacher表里已存在
        if (teacherRepository.findTeacherByEmail(email) != null) {
            log.info("该邮箱已存在");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该邮箱已存在");
        }
    }
    @Override
    public void sendTeacherEmail(String receiver) throws Exception {
        //如果邮箱格式不正确（正则表达式验证）
        String result = receiver.substring(receiver.length()-13,receiver.length());
        if (!(receiver.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+") && result.equals("@edu.cn"))) {
            log.info("邮箱格式不正确");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱格式不正确");
        }

        //向该邮箱发送验证码邮件
        sendMailService.sendEmail(receiver);

    }

    @Override
    public void checkStudentRepeat(String email) {
        //如果邮箱在student表里已存在
        try{
            Student student = studentRepository.findStudentByEmail(email);
            if (student != null) {
                log.info("该邮箱已存在");
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该邮箱已存在");
            }
        }catch (NonUniqueResultException e) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该邮箱已存在");
        } catch (Exception e) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,"系统未知异常");
        }

    }
    @Override
    public  void sendStudentEmail(String receiver) throws Exception {
        //如果邮箱格式不正确（正则表达式验证）
        if (!(receiver.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+"))) {
            log.info("邮箱格式不正确");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱格式不正确");
        }

        //向该邮箱发送验证码邮件
        sendMailService.sendEmail(receiver);
    }
}
