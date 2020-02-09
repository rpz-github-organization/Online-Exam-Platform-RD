package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.StuExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface  StuExamRepository extends JpaRepository<StuExam, String> {

    @Query("select u from StuExam u where u.exam_id = ?1")
    ArrayList<StuExam> getByExam_id(long exam_id);
}
