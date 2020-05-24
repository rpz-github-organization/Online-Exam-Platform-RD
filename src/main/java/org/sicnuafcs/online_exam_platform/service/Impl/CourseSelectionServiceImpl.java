package org.sicnuafcs.online_exam_platform.service.Impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.*;
import org.sicnuafcs.online_exam_platform.model.*;
import org.sicnuafcs.online_exam_platform.service.CourseSelectionService;
import org.sicnuafcs.online_exam_platform.service.ExamService;
import org.sicnuafcs.online_exam_platform.service.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
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
    StuCoRepository stuCoRepository;
    @Autowired
    TeaCoRepository teaCoRepository;
    @Resource
    HomePageService homePageService;
    @Autowired
    DozerBeanMapper dozerBeanMapper;
    @Autowired
    ExamRepository examRepository;
    @Autowired
    StuExamRepository stuExamRepository;
    @Autowired
    ExamService examService;
    @Autowired
    CourseSelectionService courseSelectionService;

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
            if (majors.contains(major_id) && !course.contains(courses.get(i).getCo_id())) {
                course.add(courses.get(i).getCo_id());
            }
        }
        return course;
    }


    @Override
    //key(co_id)到course中得到name  value(tea_id)到teacher中得到name
    public ArrayList<Map> getChosenCoId_TeaId(List<StuCo> stuCos) {
        ArrayList<Map> list = new ArrayList();
        for (StuCo stuCo : stuCos) {
            Map<String, Object> one = new HashMap();
            String course = courseRepository.getNameByCo_id(stuCo.getCo_id());
            String teacher = teacherRepository.getNameByTea_id(stuCo.getTea_id());
            one.put("co_id", stuCo.getCo_id());
            one.put("co_name", course);
            one.put("tea_id", stuCo.getTea_id());
            one.put("tea_name", teacher);
            one.put("begin_time", 0);
            one.put("end_time", 0);
            list.add(one);
        }
        return list;
    }
    @Override
    //key(co_id)到course中得到name  value(tea_id)到teacher中得到name
    public ArrayList<Map> getUnChooesCoId_TeaId(List<StuCo> stuCos, String major_id) {
        List<String> allCourse_id = courseSelectionService.getAllCourse_id(major_id);  //在course表里获取专业major_id对应的全部课程co_id
        for (StuCo stuCo : stuCos) {
            if (allCourse_id.contains(stuCo.getCo_id())) {
                allCourse_id.remove(stuCo.getCo_id());
            }
        }
        List<TeaCo> teaCos = teaCoRepository.findByCo_id(allCourse_id);
        ArrayList<Map> list = new ArrayList();
        for (TeaCo teaCo : teaCos) {
            Map<String, Object> one = new HashMap();
            String course = courseRepository.getNameByCo_id(teaCo.getCo_id());
            String teacher = teacherRepository.getNameByTea_id(teaCo.getTea_id());
            one.put("co_id", teaCo.getCo_id());
            one.put("co_name", course);
            one.put("tea_id", teaCo.getTea_id());
            one.put("tea_name", teacher);
            one.put("begin_time", 0);
            one.put("end_time", 0);
            list.add(one);
        }
        return list;
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

    @Override
    public void saveToStuCo(String res, String stu_id) {
        if (stu_id == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "传入的学号不正确");
        }
        ArrayList<String> data = homePageService.String2List(res);

        //查重
        ArrayList<String> coList = new ArrayList();
        for (String in : data) {
            Map map = JSON.parseObject(in);
            String co_id = map.get("co_id").toString();
            coList.add(co_id);
        }
        if (coList.isEmpty()) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "课程选择为空");
        }
        HashSet hashSet = new HashSet(coList);
        if (hashSet.size() != coList.size()) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "课程选择重复");
        }


        for (String in : data) {
            Map map = JSON.parseObject(in);
            String co_id = map.get("co_id").toString();
            if (co_id == null) {
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "传入的课程号不正确");
            }
            String tea_id = null;
            tea_id = stuCoRepository.getTeaIdByStu_idAndAndCo_id(stu_id, co_id);
            if (tea_id == null) {
                //添加
                StuCo stuCo = new StuCo();
                stuCo.setCo_id(co_id);
                stuCo.setStu_id(stu_id);
                stuCo.setTea_id(map.get("tea_id").toString());
                stuCoRepository.save(stuCo);
            }
            else {
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "已选过课程");
            }
        }
    }

    @Override
    public void quitCourse(String co_id, String tea_id, String stu_id) {
        if (stu_id == null || co_id == null || tea_id == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "传入的学号/教师号/课程号不正确");
        }
        StuCo stuCo = new StuCo();
        stuCo.setStu_id(stu_id);
        stuCo.setCo_id(co_id);
        stuCo.setTea_id(tea_id);
        try {
            if (stuCo == stuCoRepository.getOneById(co_id, tea_id, stu_id) || stuCoRepository.getOneById(co_id, tea_id, stu_id)!= null) {
                stuCoRepository.delete(stuCo);
            }
            else {
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "数据不匹配，删除失败");
            }
        }catch (Exception e) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "验证失败");
        }
    }

    @Override
    public ArrayList getDoneExam(String tea_id) {
        ArrayList res = new ArrayList();
        List<Exam> exams = examRepository.findExamsByTea_idAndProgress_status(tea_id, Exam.ProgressStatus.DONE);
        for (Exam exam : exams) {
            if (exam.is_judge()) {
                Map<String, Object> map = new HashMap<>();
                map.put("exam_id", exam.getExam_id());
                map.put("exam_name", exam.getName());
                map.put("begin_time", exam.getBegin_time());
                map.put("last_time", exam.getLast_time());
                map.put("co_name", courseRepository.getNameByCo_id(exam.getCo_id()));
                res.add(map);
            }
        }
        return res;
    }

    @Override
    public ArrayList getStuExam(Long exam_id) {
        ArrayList res = new ArrayList();
        HashSet<String> stuIds = stuExamRepository.getStuIdByExam_id(exam_id);
        for (String stu_id : stuIds) {
            Map map = new HashMap();
            map.put("stu_id", stu_id);
            map.put("stu_name", studentRepository.findNameByStu_id(stu_id));
            map.put("score", examService.getStuExamScore(exam_id, stu_id));
            res.add(map);
        }

        //对结果进行排序
        Collections.sort(res, new Comparator<Map>() {
            @Override
            public int compare(Map m1, Map m2) {
                int diff = (int)m1.get("score") - (int)m2.get("score");
                if (diff < 0) return 1;
                else if (diff > 0) return -1;
                return 0;
            }
        });

        return res;
    }

    @Override
    public List<Object> getCourseNotTea(String tea_id) {
        List<Object> ret = new ArrayList<>();
        //获取教师教授的课程id
        List<String> co_list = teaCoRepository.findCo_idByTea_Id(tea_id);
        List<Course> courses = new ArrayList<>();
        //获取教师未教授的课程
        if (co_list.isEmpty()) {
            courses = courseRepository.findCourse();
        } else {
            log.info(co_list.toString());
            courses = courseRepository.findCourseByCo_idNotIn(co_list);
        }
        for (Course course : courses) {
            Map<String, Object> courseMap = new HashMap<>();
            courseMap.put("co_id", course.getCo_id());
            courseMap.put("name", course.getName());
            courseMap.put("credit", course.getCredit());
            courseMap.put("school_hour", course.getSchool_hour());
            ArrayList<String> majors = homePageService.String2List(course.getMajor());
            List<String> majors_name = new ArrayList<>();
            for (String mayjor_id : majors) {
                majors_name.add(majorRepository.getNameByMajor_id(mayjor_id));
            }
            courseMap.put("mayjor", majors_name);
            ret.add(courseMap);
        }
        return ret;
    }
}
