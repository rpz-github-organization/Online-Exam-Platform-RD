package org.sicnuafcs.online_exam_platform.service;

import org.sicnuafcs.online_exam_platform.model.Course;
import org.sicnuafcs.online_exam_platform.model.Exam;

import java.util.List;
import java.util.Map;

public interface HomePageService {
    public List<Exam> findStuById(String stu_id, String status) ;

    public  List<Exam> findStuByPhone(String Phone,String status);

    public List<Course> findTeaById(String tea_id);

    public List<Course> findTeaByPhone(String Phone);
}
