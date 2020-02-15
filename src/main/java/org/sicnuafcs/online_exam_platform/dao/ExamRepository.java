package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.Exam;
//import org.sicnuafcs.online_exam_platform.model.Question;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, String> {
    @Query("select u from Exam u where u.exam_id in (:examIdList) and u.progress_status = (:status)")
    List<Exam> findExamsByExam_idAAndProgress_status(List<Long> examIdList,Exam.ProgressStatus status);
    @Query("select u from Exam u where u.exam_id = ?1")
    Exam findExamByExam_id(Long exam_id);
}
