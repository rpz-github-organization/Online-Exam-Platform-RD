package org.sicnuafcs.online_exam_platform.service;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.model.Teacher;
import org.sicnuafcs.online_exam_platform.dao.TeacherRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Resource
@Slf4j
@Service
public class TeacherRestServiceImpl implements TeacherRestService {

    //将jpa仓库对象注入
    @Resource
    private TeacherRepository teacherRepository;

    @Override
    public Teacher saveTeacher(Teacher teacher){
        Teacher teacher1=new Teacher(teacher.getTea_id(),teacher.getName(),teacher.getDept(),teacher.getSex(),teacher.getPassword(),teacher.getQq(),teacher.getWeixin(),teacher.getEmail(),teacher.getTelephone(),teacher.getStatus(),teacher.getCode());
        teacherRepository.save(teacher1);

        return teacher1;
    }

    @Override
    public void deleteTeacher(String tea_id){
        teacherRepository.deleteById(tea_id);
    }

    @Override
    public void updateTeacher(Teacher teacher){
        Teacher teacher1=new Teacher(teacher.getTea_id(),teacher.getName(),teacher.getDept(),teacher.getSex(),teacher.getPassword(),teacher.getQq(),teacher.getWeixin(),teacher.getEmail(),teacher.getTelephone(),teacher.getStatus(),teacher.getCode());

        teacherRepository.save(teacher1);
    }

    @Override
    public Optional<Teacher> getTeacher(String tea_id){
        return teacherRepository.findById(tea_id);
    }


}
