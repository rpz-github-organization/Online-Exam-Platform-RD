package org.sicnuafcs.online_exam_platform.service;

import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.model.Teacher;

import java.util.Optional;

public interface PersonalDataService {
    public Optional<Teacher> getTeacherData(String ID);
    public Optional<Student> getStudentData(String ID);
    public Teacher updataTeacherPassword(String ID,String newPassword);
    public Student updataStudentPassword(String ID,String newPassword);
}
