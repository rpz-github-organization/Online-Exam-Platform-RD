package org.sicnuafcs.online_exam_platform.service;

import java.util.Map;

public interface HomePageService {
    public Map findStuById(String stu_id, String status) ;

    public Map findStuByPhone(String Phone,String status);

    public Map findTeaById(String tea_id);

    public Map findTeaByPhone(String Phone);
}
