package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;

import org.sicnuafcs.online_exam_platform.dao.TeacherRepository;
import org.sicnuafcs.online_exam_platform.model.Teacher;
import org.sicnuafcs.online_exam_platform.service.AuthorityService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Resource
@Slf4j
@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Resource
    TeacherRepository teacherRepository;
    @Override
    public List find(int page,int authority) {

        List<Teacher> l ;
        int size = 2;

        //根绝限权得到用户
        //假设是4
        Specification spec = null;

        Pageable pageable =  PageRequest.of(page-1,size);
        Page<Teacher> teacherPage = teacherRepository.findAll(spec,pageable);
        l = teacherPage.getContent();

        System.out.println("打印台检测"+l);
        return l;
    }
}
