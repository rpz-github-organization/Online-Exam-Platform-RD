package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.dao.StudentRepository;
import org.sicnuafcs.online_exam_platform.dao.TeacherRepository;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.model.Teacher;
import org.sicnuafcs.online_exam_platform.service.PersonalDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
@Slf4j
@Service
public class PersonalDataServiceImpl implements PersonalDataService {

    @Resource
    TeacherRepository teacherRepository;
    @Resource
    StudentRepository studentRepository;


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
    public Teacher updataTeacherPassword(String ID,String newPassword) {
        Teacher teacher = teacherRepository.findById(ID).get();
        //
        log.info(String.valueOf(teacher));

        if(teacher.getPassword().equals(newPassword)){
            return null;
//            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "和原始密码相同");
        }
        teacher.setPassword(newPassword);
        Teacher newTeacherData = teacherRepository.save(teacher);
        log.info(String.valueOf(newTeacherData));
        newTeacherData.setPassword(null);
        newTeacherData.setCode(null);
        return newTeacherData;
    }

    @Override
    public Student updataStudentPassword(String ID, String newPassword) {
        Student student = studentRepository.findById(ID).get();
        log.info(String.valueOf(student));

        if(student.getPassword().equals(newPassword)){
            return null;
//            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "和原始密码相同");
        }
        student.setPassword(newPassword);
        Student newStudentData = studentRepository.save(student);
        newStudentData.setPassword(null);
        newStudentData.setCode(null);
        return  newStudentData;

    }
}
