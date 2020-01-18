package org.sicnuafcs.online_exam_platform.service;

import org.sicnuafcs.online_exam_platform.model.Teacher;

import java.util.Optional;

public interface TeacherRestService {
    Teacher saveTeacher(Teacher teacher);

    void deleteTeacher(String tea_id);

    void updateTeacher(Teacher teacher);

    Optional<Teacher> getTeacher(String tea_id);
}
