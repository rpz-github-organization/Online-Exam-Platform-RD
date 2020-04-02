package org.sicnuafcs.online_exam_platform.service;

import org.sicnuafcs.online_exam_platform.model.Course;
import org.sicnuafcs.online_exam_platform.model.CourseVO;
import org.sicnuafcs.online_exam_platform.model.Exam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface HomePageService {
    List<Object> findStuById(String stu_id, int status) ;

    List<CourseVO> findTeaById(String tea_id);

    String List2String(ArrayList<String> list);

    ArrayList<String> String2List(String s);

}
