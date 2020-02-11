package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.StuExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface  StuExamRepository extends JpaRepository<StuExam, String> {

   // @Query("select u from StuExam u where u.exam_id = ?1")
    //ArrayList<StuExam> getByExam_id(long exam_id);

    @Query(value = "SELECT exam_id FROM stu_exam WHERE stu_id = ? and status = ? ", nativeQuery = true)
    List<Long> findExam_idByStu_Id(String Stu_id,String status);
}
