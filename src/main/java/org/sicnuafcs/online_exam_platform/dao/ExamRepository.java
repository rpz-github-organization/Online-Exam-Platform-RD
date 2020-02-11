package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.Exam;
//import org.sicnuafcs.online_exam_platform.model.Question;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, String> {

    @Query(value = "select u from Exam u where u.exam_id in (:examIdList) and u.progress_status = ?2", nativeQuery = true)
    List<Exam> findExamsByExam_id(List<Long> examIdList,String status);

}
