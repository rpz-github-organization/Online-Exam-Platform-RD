package org.sicnuafcs.online_exam_platform.dao;

import io.lettuce.core.dynamic.annotation.Param;
import org.sicnuafcs.online_exam_platform.model.Exam;
//import org.sicnuafcs.online_exam_platform.model.Question;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, String> {
    @Query("select u from Exam u where u.exam_id in (:examIdList) and u.progress_status = (:status)")
    List<Exam> findExamsByExam_idAAndProgress_status(List<Long> examIdList,Exam.ProgressStatus status);
    @Query("select u from Exam u where u.exam_id = ?1")
    Exam findExamByExam_id(Long exam_id);
    @Query("select u from Exam  u where u.co_id = ?1 and u.tea_id = ?2")
    List<Exam> findExamsByCo_idAAndTea_id(String co_id, String tea_id);

    @Modifying
    @Transactional
    @Query("update Exam u set u.progress_status = :status where u.exam_id = :exam_id")
    void saveStatus(@Param("exam_id") Long exam_id, @Param("status") Exam.ProgressStatus status);

}
