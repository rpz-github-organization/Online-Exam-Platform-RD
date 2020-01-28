package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.StudentRepository;
import org.sicnuafcs.online_exam_platform.dao.TeacherRepository;
import org.sicnuafcs.online_exam_platform.model.Login;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class LoginServiceImpl {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;

    //学号或者工号加密码
    public Login LoginId(Login login) {
        //因为学号/工号/手机号没分开 所以需要校验学号位数
        if (login.getKeyword().length() != 10) {
            log.info("学号/工号位数问题");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "学号/工号位数问题");
        }

        Optional<Teacher> teacherList = teacherRepository.findById(login.getKeyword());
        Optional<Student> studentList = studentRepository.findById(login.getKeyword());
        if (studentList.isPresent() || teacherList.isPresent()) {
            if (studentList.isPresent()) {  //如果为学号
                if (BCrypt.checkpw(login.getPassword(),studentList.get().getPassword())) { //如果密码正确
                    log.info("学生登录验证成功");
                    login.setId(studentList.get().getStu_id());
                    login.setAuthority(studentList.get().getAuthority());
                    return login;
                }
                else {
                    throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户名/密码错误");     //密码错误
                }
            }
            else if (teacherList.isPresent()) { //如果为工号
                if (BCrypt.checkpw(login.getPassword(),teacherList.get().getPassword())) {
                    log.info("教师登录验证成功");
                    login.setId(teacherList.get().getTea_id());
                    login.setAuthority(teacherList.get().getAuthority());
                    return login;
                }
                else {
                    throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户名/密码错误");     //密码错误
                }
            }
        }
        throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户不存在");
    }

    //手机号加密码
    public Login loginPhone(Login login) {
        Optional<Teacher> teacherList = teacherRepository.findByTelephone(login.getKeyword());
        Optional<Student> studentList = studentRepository.findByTelephone(login.getKeyword());
        if (studentList.isPresent() || teacherList.isPresent()) {      //如果数据库里面已存在
            if(studentList.isPresent()) {
                if (BCrypt.checkpw(login.getPassword(), studentList.get().getPassword())) {

                    log.info("学生登录验证成功");
                    login.setId(studentList.get().getStu_id());
                    login.setAuthority(studentList.get().getAuthority());
                    return login;

                } else {
                    throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户名/密码错误");     //密码错误
                }
            }
            else if (teacherList.isPresent()) {
                if (BCrypt.checkpw(login.getPassword(), teacherList.get().getPassword())) {
                    log.info("教师登录验证成功");
                    login.setId(teacherList.get().getTea_id());
                    login.setAuthority(teacherList.get().getAuthority());
                    return login;
                } else {
                    throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户名/密码错误");     //密码错误
                }
            }
        }
        throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户不存在");      //用户不存在
    }

}
