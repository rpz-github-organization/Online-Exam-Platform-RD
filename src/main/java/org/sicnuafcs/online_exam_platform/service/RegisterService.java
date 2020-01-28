package org.sicnuafcs.online_exam_platform.service;

import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.model.Teacher;

import java.util.Map;

public interface RegisterService {
    Student saveStudent(Student student) throws Exception;

    Teacher saveTeacher(Teacher teacher) throws Exception;

    boolean sendTeacherEmail(String receiver) throws Exception;

    boolean sendStudentEmail(String receiver) throws Exception;
}
