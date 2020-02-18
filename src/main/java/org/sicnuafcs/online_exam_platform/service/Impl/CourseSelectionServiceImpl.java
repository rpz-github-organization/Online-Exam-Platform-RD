package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.*;
import org.sicnuafcs.online_exam_platform.model.Course;
import org.sicnuafcs.online_exam_platform.model.CourseVO;
import org.sicnuafcs.online_exam_platform.model.StuCo;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.service.CourseSelectionService;
import org.sicnuafcs.online_exam_platform.service.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

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
    StuCoRepository stuCoRepository;
    @Autowired
    TeaCoRepository teaCoRepository;
    @Resource
    HomePageService homePageService;
    @Autowired
    DozerBeanMapper dozerBeanMapper;

    @Override
    public String getClass_id(String stu_id) {
        Student stu = studentRepository.findStudentByStu_id(stu_id);
        if (stu == null) {
            log.info("用户不存在");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户不存在");
        }
        String class_id = stu.getClass_id();
        if (class_id == null) {
            log.info("该用户未填写班级信息");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该用户未填写班级信息");
        }
        return class_id;
    }

    @Override
    public String getMajor_id(String stu_id) {
        Student stu = studentRepository.findStudentByStu_id(stu_id);
        if (stu == null) {
            log.info("用户不存在");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户不存在");
        }
        String class_id = stu.getMajor_id();
        if (class_id == null) {
            log.info("该用户未填写专业信息");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该用户未填写专业信息");
        }
        return class_id;
    }

    @Override
    //在course表里获取(major_list)专业major_id对应的全部课程co_id
    public ArrayList<String> getAllCourse_id(String major_id) {
        ArrayList<String> course = new ArrayList<>();   //可选的课程
        List<Course> courses = courseRepository.findAll();  //所有课程
        for (int i = 0; i < courses.size(); i++) {
            ArrayList<String> majors = homePageService.String2List(courses.get(i).getMajor());  //每个课程对应的所有的专业
            if (majors.contains(major_id)) {
                course.add(courses.get(i).getCo_id());
            }
        }
        return course;
    }

    @Override
    //在stu_co表里根据stu_id 获取学生 已经选择 的课程和老师，co_id和tea_id;
    public Map<String, String> getChosenCoId_TeaId(String stu_id) {
        Map<String, String> coId_teaId = new IdentityHashMap<>();
        List<StuCo> stu_cos = stuCoRepository.getStu_coByStu_id(stu_id);
        for (int i = 0; i < stu_cos.size(); i++) {
            String id = new String(stu_cos.get(i).getCo_id());
            coId_teaId.put(id, stu_cos.get(i).getTea_id());
        }
        return coId_teaId;
    }

    @Override
    //key(co_id)到course中得到name  value(tea_id)到teacher中得到name
    public Map<String, String> getName(Map<String, String> coId_TeaId) {
        Map<String, String> co_Tea = new IdentityHashMap<>();
        for (Map.Entry<String, String> m : coId_TeaId.entrySet()) {
            String course = new String(courseRepository.getNameByCo_id(m.getKey()));
            String teacher = new String(teacherRepository.getNameByTea_id(m.getValue()));
            co_Tea.put(course, teacher);
        }

        return co_Tea;
    }

    @Override
    //在tea_co类中 根据co_id获取tea_id，与co_id(地址)对应成map
    public Map<String, String> getAllCoId_TeaId(ArrayList<String> course_id) {  //course_id从course表里已读取出来
        Map<String, String> coId_teaId = new IdentityHashMap<>();
        for (String co_id : course_id) {
            List<String> tea_ids = teaCoRepository.getTea_idByCo_id(co_id);
            for (int i = 0; i < tea_ids.size(); i++) {
                    String id = new String(co_id);
                    coId_teaId.put(id, tea_ids.get(i));
                }
            }
        return coId_teaId;
    }

    @Override
    public String getMajorName(String major_id) {
        return majorRepository.getNameByMajor_id(major_id);
    }

    @Override
    public Course add(CourseVO courseVO) {
        Course course = dozerBeanMapper.map(courseVO, Course.class);
        if (courseVO.getMajor_list() != null) {
            course.setMajor(homePageService.List2String(courseVO.getMajor_list()));
        }
        courseRepository.save(course);
        return course;
    }

}
