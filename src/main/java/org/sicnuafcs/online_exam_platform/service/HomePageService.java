package org.sicnuafcs.online_exam_platform.service;

import org.sicnuafcs.online_exam_platform.model.Course;
import org.sicnuafcs.online_exam_platform.model.Exam;

import java.util.List;
import java.util.Map;

public interface HomePageService {
    public List<Exam> findStuById(String stu_id, int status) ;

    public List<Course> findTeaById(String tea_id);

}
