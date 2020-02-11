package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.*;
import org.sicnuafcs.online_exam_platform.model.Course;
import org.sicnuafcs.online_exam_platform.model.Exam;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.service.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
public class HomePageServiceimpl implements HomePageService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StuExamRepository stuExamRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private CourseRepository coRepository;
    @Autowired
    private TeacherRepository teaRepository;
    @Autowired
    private TeaCoRepository teaCoRepository;
    @Autowired
    private CourseRepository courseRepository;


    @Override
    public List<Exam> findStuById(String stu_id, String status) {

        //通过学生id获取考试信息
        List<Long> exam_idList = stuExamRepository.findExam_idByStu_Id(stu_id);
        List<Exam> exams = examRepository.findExamsByExam_idIn(exam_idList);

        return exams;

    }



    @Override

    public  List<Exam> findStuByPhone(String telephone, String status) {
        //根据电话信息获取 stu_id
        String stu_id = studentRepository.findStu_id_idByPhone(telephone);
        //通过学生id获取考试信息
        List<Long> exam_idList = stuExamRepository.findExam_idByStu_Id(stu_id);
        List<Exam> exams = examRepository.findExamsByExam_idIn(exam_idList);

        return exams;

    }


    @Override
    public List<Course> findTeaById(String tea_id) {
        //获取教师所授课程
        List<String> co_idList = teaCoRepository.findCo_idByTea_Id(tea_id);
        List<Course> courses = courseRepository.findCourseByCo_idIn(co_idList);
        return courses;
    }



    @Override
    public List<Course> findTeaByPhone(String Phone) {
        String tea_id = teaRepository.findTea_idByPhone(Phone);
        //获取教师所授课程
        List<String> co_idList = teaCoRepository.findCo_idByTea_Id(tea_id);
        List<Course> courses = courseRepository.findCourseByCo_idIn(co_idList);
        return courses;

    }

}