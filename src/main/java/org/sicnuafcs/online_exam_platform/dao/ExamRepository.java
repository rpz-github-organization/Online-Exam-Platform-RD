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

    @Modifying
    @Transactional
    @Query("update Exam u set u.last_time = :last_time where u.exam_id = :exam_id")
    void saveLast_time(@Param("exam_id") Long exam_id, @Param("last_time") int last_time);

    @Query("select u.exam_id from Exam  u where u.co_id = ?1 and u.tea_id = ?2")
    List<Long> findExamIdByCo_idAAndTea_id(String co_id, String tea_id);

    @Query("select u from Exam u where u.tea_id = ?1 and u.progress_status = ?2")
    List<Exam> findExamsByTea_idAndProgress_status(String Tea_id, Exam.ProgressStatus status);

    @Query("select u.name from Exam u where u.exam_id = ?1")
    String getNameByExam_id(Long exam_id);

    @Modifying
    @Transactional
    @Query("update Exam u set u.is_distribute = :is_distribute where u.exam_id = :exam_id")
    void saveIsDistribute(@Param("exam_id") Long exam_id, @Param("is_distribute") boolean is_distribute);

    @Query("select u.last_time from Exam u where u.exam_id = ?1")
    int getLast_timeByExam_id(Long exam_id);

    @Modifying
    @Transactional
    @Query("update Exam u set u.is_judge = :is_judge where u.exam_id = :exam_id")
    void saveIs_judge(@Param("exam_id") Long exam_id, @Param("is_judge") boolean is_judge);

    @Query("select u.is_distribute from Exam u where u.exam_id = ?1")
    boolean getIs_distributeByExam_id(Long exam_id);

    @Query("select u from Exam u")
    List<Exam> getExams();

    @Query("select max(u.exam_id) from Exam u")
    Long getMaxExamId();
}
