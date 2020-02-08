package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.StuExam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface  StuExamRepository extends JpaRepository<StuExam, String> {
    ArrayList<StuExam> getByExamId(long exam_id);
}
