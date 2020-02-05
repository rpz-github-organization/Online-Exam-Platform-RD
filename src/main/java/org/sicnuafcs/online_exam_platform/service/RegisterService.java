package org.sicnuafcs.online_exam_platform.service;

import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.model.Teacher;

public interface RegisterService {
    void saveStudent(Student student) throws Exception;

    void saveTeacher(Teacher teacher) throws Exception;

    void checkTeacherRepeat(String email);

    void sendTeacherEmail(String receiver) throws Exception;

    void checkStudentRepeat(String email);

    void sendStudentEmail(String receiver) throws Exception;
}
