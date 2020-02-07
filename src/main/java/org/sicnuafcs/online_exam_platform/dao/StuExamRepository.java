package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.StuExam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  StuExamRepository extends JpaRepository<StuExam, String> {
}
