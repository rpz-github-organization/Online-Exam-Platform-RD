package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.dao.*;
import org.sicnuafcs.online_exam_platform.model.Course;
import org.sicnuafcs.online_exam_platform.model.Exam;
import org.sicnuafcs.online_exam_platform.model.StuExam;
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
    public List<Exam> findStuById(String stu_id, int status) {

        List<Exam> exams = new ArrayList<>();
        if(status == 0){
            StuExam.Status stuExam_status = StuExam.Status.WILL;
            Exam.ProgressStatus exam_status = Exam.ProgressStatus.ING;
            List<Long> exam_idList = stuExamRepository.findExam_idByStu_IdAAndStatus(stu_id, stuExam_status);
            exams = examRepository.findExamsByExam_idAAndProgress_status(exam_idList,exam_status);
        }else if(status == 1){
            StuExam.Status stuExam_status = StuExam.Status.DONE;
            Exam.ProgressStatus exam_status = Exam.ProgressStatus.ING;
            List<Long> exam_idList = stuExamRepository.findExam_idByStu_IdAAndStatus(stu_id, stuExam_status);
            exams = examRepository.findExamsByExam_idAAndProgress_status(exam_idList,exam_status);
        }else if(status == 2){
            StuExam.Status stuExam_status = StuExam.Status.DONE;
            Exam.ProgressStatus exam_status = Exam.ProgressStatus.DONE;
            List<Long> exam_idList = stuExamRepository.findExam_idByStu_IdAAndStatus(stu_id, stuExam_status);
            exams = examRepository.findExamsByExam_idAAndProgress_status(exam_idList,exam_status);
        }else if (status == 3){
            StuExam.Status stuExam_status = StuExam.Status.WILL;
            Exam.ProgressStatus exam_status = Exam.ProgressStatus.DONE;
            List<Long> exam_idList = stuExamRepository.findExam_idByStu_IdAAndStatus(stu_id, stuExam_status);
            exams = examRepository.findExamsByExam_idAAndProgress_status(exam_idList,exam_status);
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


}