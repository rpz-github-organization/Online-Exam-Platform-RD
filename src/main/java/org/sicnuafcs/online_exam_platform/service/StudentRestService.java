package org.sicnuafcs.online_exam_platform.service;

import org.sicnuafcs.online_exam_platform.dao.Student;

import java.util.Optional;

public interface StudentRestService{

    Student saveStudent(Student student);

    void deleteStudent(String stu_id);

    void updateStudent(Student student);

    Optional<Student> getStudent(String stu_id);

}
