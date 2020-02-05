package org.sicnuafcs.online_exam_platform.service;

import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.model.Teacher;

import java.util.Map;
import java.util.Optional;

public interface PersonalDataService {
     Optional<Teacher> getTeacherData(String ID);
     Optional<Student> getStudentData(String ID);
     Teacher updateTeacherData(String ID, Map<String, Object> params);
     Student updateStudentData(String ID,Map<String, Object> params);
     Teacher editTeacherBaseData(String ID,Teacher newTeacherData);
     Student editStudentBaseData(String ID,Student newStudentData);
     void checkTeacherEmail(String ID);
     void checkStudentEmail(String ID);
}
