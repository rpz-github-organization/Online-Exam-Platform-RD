package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.*;
import org.sicnuafcs.online_exam_platform.model.Course;
import org.sicnuafcs.online_exam_platform.model.Exam;
import org.sicnuafcs.online_exam_platform.model.ExamInfor;
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
    private StuExamRepository stu_examRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private CourseRepository coRepository;
    @Autowired
    private TeacherRepository teaRepository;
    @Autowired
    private Tea_coRepository tea_coRepository;


    @Override
    public Map findStuById(String stu_id, String status) {

        //通过学生id获取学生姓名
        Optional<Student> studentList = studentRepository.findById(stu_id);
        String stu_name = studentList.get().getName();//学生姓名

        //通过学生id获取考试信息
        List<String> exam_idList = stu_examRepository.findExam_idBySea_Id(stu_id);
        int n = exam_idList.size();
        List<ExamInfor> ExamInfor = new ArrayList<>();
        int num = 0;
        for(int index = 0;index < n;index++) {
            Optional<Exam> examList = examRepository.findById(exam_idList.get(index));
            if (examList.get().getProgress_status().equals(status)) {
                String exam_name = examList.get().getName();
                Long begin_time = examList.get().getBegin_time();
                int last_time = examList.get().getLast_time();
                String co_id = examList.get().getCo_id();
                Optional<Course> courseList = coRepository.findById(co_id);
                String co_name = courseList.get().getName();
                ExamInfor examinf = new ExamInfor();
                examinf.setBegin_time(begin_time);
                examinf.setCourse(co_name);
                examinf.setLast_time(last_time);
                examinf.setExam_name(exam_name);
                ExamInfor.add(examinf);
                num++;
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stu_name", stu_name);
        map.put("exam_num",num);
        map.put("exam",ExamInfor);
        if(map.isEmpty()){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"信息获取失败！请重试");
        }
        return map;

    }



    @Override

    public Map findStuByPhone(String Phone, String status) {

        Optional<Student> studentList = studentRepository.findStu_id_idByPhone(Phone);
        //通过学生id获取学生姓名
        String stu_name = studentList.get().getName();//学生姓名
        String stu_id = studentList.get().getStu_id();

        //通过学生id获取考试信息
        List<String> exam_idList = stu_examRepository.findExam_idBySea_Id(stu_id);
        int n = exam_idList.size();
        List<ExamInfor> ExamInfor = new ArrayList<>();
        int num = 0;
        for(int index = 0;index < n;index++) {
            Optional<Exam> examList = examRepository.findById(exam_idList.get(index));
            if (examList.get().getProgress_status().equals(status)) {
                String exam_name = examList.get().getName();
                Long begin_time = examList.get().getBegin_time();
                int last_time = examList.get().getLast_time();
                String co_id = examList.get().getCo_id();
                Optional<Course> courseList = coRepository.findById(co_id);
                String co_name = courseList.get().getName();
                ExamInfor examinf = new ExamInfor();
                examinf.setBegin_time(begin_time);
                examinf.setCourse(co_name);
                examinf.setLast_time(last_time);
                examinf.setExam_name(exam_name);
                ExamInfor.add(examinf);
                num++;
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stu_name", stu_name);
        map.put("exam_num",num);
        map.put("exam",ExamInfor);
        if(map.isEmpty()){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"信息获取失败！请重试");
        }
        return map;

    }



    @Override
    public Map findTeaById(String tea_id) {

        //通过教师id获取教师姓名
        String tea_name = teaRepository.findNameById(tea_id);

        //获取教师所授课程
        List<String> co_idList = tea_coRepository.findCo_idByTea_Id(tea_id);
        List<String> co_name = new ArrayList<>();
        int n = co_idList.size();
        for (String s : co_idList) {
            Optional<Course> courseList = coRepository.findById(s);
            co_name.add(courseList.get().getName());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("course", co_name);
        map.put("course_num",n);
        map.put("name", tea_name);
        if(map.isEmpty()) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "信息获取失败！请重试");
        }
        return map;
    }



    @Override
    public Map findTeaByPhone(String Phone) {

        String tea_id = teaRepository.findTea_idByPhone(Phone);
        String tea_name = teaRepository.findNameById(tea_id);

        /*获取教师所授课程*/
        List<String> co_idList = tea_coRepository.findCo_idByTea_Id(tea_id);
        int n = co_idList.size();
        List<String> co_name = new ArrayList<>();
        for(int index = 0;index < n;index++){
            Optional<Course> courseList = coRepository.findById(co_idList.get(index));
            co_name.add(courseList.get().getName());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("course", co_name);
        map.put("course_num",n);
        map.put("name", tea_name);
        if(map.isEmpty()){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"信息获取失败！请重试");
        }
        return map;

    }

}