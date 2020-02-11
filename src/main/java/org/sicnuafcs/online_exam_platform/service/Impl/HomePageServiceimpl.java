package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
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
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teaRepository;
    @Autowired
    private TeaCoRepository teaCoRepository;


    @Override
    public List<Exam> findStuById(String stu_id, String status) {

        List<Exam> exams = new ArrayList<>();
        if(status.equals("0")){
            String stuExam_status = "WILL";
            String exam_status = "ING";
            List<Long> exam_idList = stuExamRepository.findExam_idByStu_Id(stu_id,stuExam_status);
            exams = examRepository.findExamsByExam_id(exam_idList,exam_status);
        }else if(status.equals("1")){
            String stuExam_status = "DONE";
            String exam_status = "ING";
            List<Long> exam_idList = stuExamRepository.findExam_idByStu_Id(stu_id,stuExam_status);
            exams = examRepository.findExamsByExam_id(exam_idList,exam_status);
        }else if(status.equals("2")){
            String stuExam_status = "DONE";
            String exam_status = "DONE";
            List<Long> exam_idList = stuExamRepository.findExam_idByStu_Id(stu_id,stuExam_status);
            exams = examRepository.findExamsByExam_id(exam_idList,exam_status);
        }else {
            String stuExam_status = "WILL";
            String exam_status = "DONE";
            List<Long> exam_idList = stuExamRepository.findExam_idByStu_Id(stu_id,stuExam_status);
            exams = examRepository.findExamsByExam_id(exam_idList,exam_status);
        }

        return exams;

    }



    @Override

    public List<Exam> findStuByPhone(String Phone, String status) {

        String stu_id = studentRepository.findStu_idByPhone(Phone);
        List<Exam> exams = new ArrayList<>();
        if(status.equals("0")){
            String stuExam_status = "WILL";
            String exam_status = "ING";
            List<Long> exam_idList = stuExamRepository.findExam_idByStu_Id(stu_id,stuExam_status);
            exams = examRepository.findExamsByExam_id(exam_idList,exam_status);
        }else if(status.equals("1")){
            String stuExam_status = "DONE";
            String exam_status = "ING";
            List<Long> exam_idList = stuExamRepository.findExam_idByStu_Id(stu_id,stuExam_status);
            exams = examRepository.findExamsByExam_id(exam_idList,exam_status);
        }else if(status.equals("2")){
            String stuExam_status = "DONE";
            String exam_status = "DONE";
            List<Long> exam_idList = stuExamRepository.findExam_idByStu_Id(stu_id,stuExam_status);
            exams = examRepository.findExamsByExam_id(exam_idList,exam_status);
        }else {
            String stuExam_status = "WILL";
            String exam_status = "DONE";
            List<Long> exam_idList = stuExamRepository.findExam_idByStu_Id(stu_id,stuExam_status);
            exams = examRepository.findExamsByExam_id(exam_idList,exam_status);
        }

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