package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.StudentRepository;
import org.sicnuafcs.online_exam_platform.dao.TeacherRepository;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.model.Teacher;
import org.sicnuafcs.online_exam_platform.service.PersonalDataService;
import org.sicnuafcs.online_exam_platform.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;
@Slf4j
@Service
public class PersonalDataServiceImpl implements PersonalDataService {

    @Resource
    TeacherRepository teacherRepository;
    @Resource
    StudentRepository studentRepository;
    @Autowired
    SendMailService sendMailService;

    @Override
    public Optional<Teacher> getTeacherData(String ID) {
        Optional<Teacher> teacherList = teacherRepository.findById(ID);
        teacherList.get().setPassword(null);
        teacherList.get().setCode(null);
        return teacherList;
    }

    @Override
    public Optional<Student> getStudentData(String ID) {
        Optional<Student> studentList = studentRepository.findById(ID);
        studentList.get().setPassword(null);
        studentList.get().setCode(null);
        return studentList;
    }

    @Override
    public Teacher updateTeacherData(String ID,Map<String, Object> params) {
        Teacher teacher = teacherRepository.findById(ID).get();
//        log.info(String.valueOf(teacher));
        String newPassword = (String) params.get("newPassword"),
                newEmail= (String) params.get("newEmail"),
                newTelephone = (String) params.get("newTelephone"),
                code = (String) params.get("code");;

        //邮箱验证：验证码和邮箱是否一致；
        if (!sendMailService.verification(teacher.getEmail(), code)) {
            log.info("邮箱验证不一致");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱验证失败");
        }
        //位数不够前端 检测
        //这里只是检测是否重复

        if(teacher.getPassword().equals(newPassword)){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "和原始密码相同");
        }
        //格式不正确前端检测
//        String result = teacher.getEmail().substring(teacher.getEmail().length()-13,teacher.getEmail().length());
//        if (!(teacher.getEmail().matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+") && result.equals("@sicnu.edu.cn"))) {
//            log.info("邮箱格式不正确");
//            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱格式不正确");
//        }
        Optional<Teacher> emailList = teacherRepository.findByEmail(newEmail);
        if (emailList.isPresent()) {
            log.info("该邮箱已被注册");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该邮箱已被注册");
        }
        Optional<Student> studentPhoneList = studentRepository.findByTelephone(newTelephone);
        Optional<Teacher> teacherPhoneList = teacherRepository.findByTelephone(newTelephone);
        if (studentPhoneList.isPresent() || teacherPhoneList.isPresent()) {
            log.info("该电话已被注册");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该电话已被注册");
        }
        teacher.setPassword(newPassword);
        teacher.setEmail(newEmail);
        teacher.setTelephone(newTelephone);
        Teacher newTeacherData = teacherRepository.save(teacher);
            log.info(String.valueOf(newTeacherData));
        newTeacherData.setPassword(null);
        newTeacherData.setCode(null);
        return newTeacherData;
    }

    @Override
    public Student updateStudentData(String ID, Map<String, Object> params) {
        Student student = studentRepository.findById(ID).get();
        String newPassword = (String) params.get("newPassword"),
                newEmail= (String) params.get("newEmail"),
                newTelephone = (String) params.get("newTelephone"),
                code = (String) params.get("code");
        //邮箱验证：验证码和邮箱是否一致；
        if (!sendMailService.verification(student.getEmail(), code)) {
            log.info("邮箱验证不一致");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱验证失败");
        }
        if(student.getPassword().equals(newPassword)){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "和原始密码相同");
        }
        Optional<Teacher> emailList = teacherRepository.findByEmail(newEmail);
        if (emailList.isPresent()) {
            log.info("该邮箱已被注册");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该邮箱已被注册");
        }
        Optional<Student> studentPhoneList = studentRepository.findByTelephone(newTelephone);
        Optional<Teacher> teacherPhoneList = teacherRepository.findByTelephone(newTelephone);
        if (studentPhoneList.isPresent() || teacherPhoneList.isPresent()) {
            log.info("该电话已被注册");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该电话已被注册");
        }
        student.setPassword(newPassword);
        student.setEmail(newEmail);
        student.setTelephone(newTelephone);
        Student newStudentData = studentRepository.save(student);
            log.info(String.valueOf(newStudentData));
        newStudentData.setPassword(null);
        newStudentData.setCode(null);
        return newStudentData;

    }

    @Override
    public Teacher editTeacherBaseData(String ID,Teacher newTeacherData) {
        Teacher teacher = teacherRepository.findById(ID).get();

        teacher.setName(newTeacherData.getName());
        teacher.setInstitute_id(newTeacherData.getInstitute_id());
        teacher.setQq(newTeacherData.getQq());
        teacher.setWeixin(newTeacherData.getWeixin());
        teacher.setSex(newTeacherData.getSex());

        Teacher teacher1 = teacherRepository.save(teacher);
        teacher1.setPassword(null);
        teacher1.setCode(null);
        return teacher1;
    }

    @Override
    public Student editStudentBaseData(String ID,Student newStudentData) {
        Student student = studentRepository.findById(ID).get();

//        student.setClass_id(newStudentData.getClass_id());
//        student.setGrade(newStudentData.getGrade());
//        student.setInstitute_id(newStudentData.getInstitute_id());
//        student.setMajor_id(newStudentData.getMajor_id());
        student.setName(newStudentData.getName());
        student.setQq(newStudentData.getQq());
        student.setSex(newStudentData.getSex());
        student.setWeixin(newStudentData.getWeixin());
        Student student1 = studentRepository.save(student);
        student1.setPassword(null);
        student1.setCode(null);
        return student1;
    }
}
