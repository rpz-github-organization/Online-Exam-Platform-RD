package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.StuExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface  StuExamRepository extends JpaRepository<StuExam, String> {
    @Query("select u from StuExam u where u.exam_id = ?1 and u.stu_id = ?2")
    ArrayList<StuExam> getByExam_idAndStu_id(long exam_id, String stu_id);

    @Query("select u from StuExam u where u.exam_id = ?1")
    ArrayList<StuExam> getByExam_id(long exam_id);

    @Query("select distinct u.exam_id from StuExam u where u.stu_id = ?1 and u.status = ?2")
    List<Long> findExam_idByStu_Id(String Stu_id, String status); //distinct 去重
}
