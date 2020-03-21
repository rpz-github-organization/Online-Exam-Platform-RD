package org.sicnuafcs.online_exam_platform.service;

import org.sicnuafcs.online_exam_platform.model.Course;
import org.sicnuafcs.online_exam_platform.model.CourseVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface CourseSelectionService {
    String getClass_id(String stu_id);

    String getMajor_id(String stu_id);

    ArrayList<String> getAllCourse_id(String major_id);

    Map<String, String> getChosenCoId_TeaId(String stu_id);

    ArrayList<Map> getIdAndName(Map<String, String> coId_TeaId);

    Map<String, String> getAllCoId_TeaId(ArrayList<String> course_id);

    String getMajorName(String major_id);

    Course add(CourseVO courseVO);

    void saveToStuCo(String str, String stu_id);

    void quitCourse(String co_id, String tea_id, String stu_id);

    ArrayList getDoneExam(String tea_id);

    ArrayList getStuExam(Long exam_id);
    List<Object> getCourseNotTea(String tea_id);
}
