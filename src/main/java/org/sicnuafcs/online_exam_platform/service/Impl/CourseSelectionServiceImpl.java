package org.sicnuafcs.online_exam_platform.service.Impl;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.*;
import org.sicnuafcs.online_exam_platform.model.Course;
import org.sicnuafcs.online_exam_platform.model.Stu_co;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.model.Tea_co;
import org.sicnuafcs.online_exam_platform.service.CourseSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class CourseSelectionServiceImpl implements CourseSelectionService {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    MajorRepository majorRepository;

    @Autowired
    Stu_coRepository stu_coRepository;

    @Autowired
    Tea_coRepository tea_coRepository;

    @Override
    public String getClass_id(String stu_id) {
        Optional<Student> student = studentRepository.findById(stu_id);
        if (!student.isPresent()) {
            log.info("用户不存在");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户不存在");
        }
        String class_id = student.get().getClass_id();
        if (class_id == null) {
            log.info("该用户未填写班级信息");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该用户未填写班级信息");
        }
        return class_id;
    }

    @Override
    public String getMajor_id(String stu_id) {
        Optional<Student> student = studentRepository.findById(stu_id);
        if (!student.isPresent()) {
            log.info("用户不存在");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户不存在");
        }
        String class_id = student.get().getMajor_id();
        if (class_id == null) {
            log.info("该用户未填写专业信息");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该用户未填写专业信息");
        }
        return class_id;
    }

    @Override
    public ArrayList<String> getAllCourse_id(String major_id) {
        ArrayList<String> course = new ArrayList<>();   //可选的课程
        List<Course> courses = courseRepository.findAll();  //所有课程
        for (int i = 0; i < courses.size(); i++) {
            ArrayList majors = courses.get(i).getMajor_list();  //每个课程对应的所有的专业
            if (majors.contains(major_id)) {
                course.add(courses.get(i).getCo_id());
            }
        }
        return course;
    }

    @Override
    public Map<String, String> getChosenCoId_TeaId(String stu_id) {
        Map<String, String> coId_teaId = new IdentityHashMap<>();
        List<Stu_co> stu_cos = stu_coRepository.findAll();
        for (int i = 0; i < stu_cos.size(); i++) {
            if (stu_cos.get(i).getStu_id().equals(stu_id)) {
                String id = new String(stu_cos.get(i).getCo_id());
                coId_teaId.put(id, stu_cos.get(i).getTea_id());
            }
        }
        return coId_teaId;
    }

    @Override
    public Map<String, String> getName(Map<String, String> coId_TeaId) {
        Map<String, String> co_Tea = new IdentityHashMap<>();
        //key(co_id)到course中得到name  value(tea_id)到teacher中得到name
        for (Map.Entry<String, String> m : coId_TeaId.entrySet()) {
            String course = new String(courseRepository.findById(m.getKey()).get().getName());
            String teacher = new String(teacherRepository.findById(m.getValue()).get().getName());
            co_Tea.put(course, teacher);
        }

        return co_Tea;
    }

    @Override
    public Map<String, String> getAllCoId_TeaId(ArrayList<String> course_id) {
        Map<String, String> coId_teaId = new IdentityHashMap<>();
        List<Tea_co> tea_cos = tea_coRepository.findAll();
        for (String co_id : course_id) {
            for (int i = 0; i < tea_cos.size(); i++) {
                if (tea_cos.get(i).getCo_id().equals(co_id)) {
                    String id = new String(co_id);
                    coId_teaId.put(id, tea_cos.get(i).getTea_id());
                }
            }
        }
        return coId_teaId;
    }

    @Override
    public String getMajorName(String major_id) {
        return majorRepository.findById(major_id).get().getName();
    }

    @Override
    public Course add(Course course) {
        courseRepository.save(course);
        return course;
    }

}
