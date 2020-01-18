package org.sicnuafcs.online_exam_platform.service;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.dao.StudentRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Resource
@Slf4j
@Service
public class StudentRestServiceImpl implements StudentRestService {

    //将jpa仓库对象注入
    @Resource
    private StudentRepository studentRepository;

    @Override
    public Student saveStudent(Student student){
        Student student1=new Student(student.getStu_id(),student.getName(),student.getClass_id(),student.getMajor_id(),student.getGrade(),student.getInstitute_id(),student.getSex(),student.getPassword(),student.getQq(),student.getWeixin(),student.getEmail(),student.getTelephone(),student.getStatus(),student.getCode());
        studentRepository.save(student1);

        return student1;
    }

    @Override
    public void deleteStudent(String stu_id){
        studentRepository.deleteById(stu_id);
    }

    @Override
    public void updateStudent(Student student){
        Student student1=new Student(student.getStu_id(),student.getName(),student.getClass_id(),student.getMajor_id(),student.getGrade(),student.getInstitute_id(),student.getSex(),student.getPassword(),student.getQq(),student.getWeixin(),student.getEmail(),student.getTelephone(),student.getStatus(),student.getCode());
        studentRepository.save(student1);
    }

    @Override
    public Optional<Student> getStudent(String stu_id){
        return studentRepository.findById(stu_id);
    }


}
