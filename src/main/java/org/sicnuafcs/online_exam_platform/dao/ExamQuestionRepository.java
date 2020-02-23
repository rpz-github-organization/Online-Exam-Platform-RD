package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.ExamQuestion;
import org.sicnuafcs.online_exam_platform.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, String> {
    @Query("select u from ExamQuestion u where u.exam_id = (:exam_id) and u.type = (:type)")
    ArrayList<ExamQuestion> findByExam_idAndType(Long exam_id, Question.Type type);
    @Query("select u.score from ExamQuestion u where u.question_id = ?1 and u.exam_id = ?2")
    int findScoreById(Long question_id, Long exam_id);
    @Query("select u.num from ExamQuestion u where u.question_id = ?1 and u.exam_id = ?2")
    int findNumById(Long question_id, Long exam_id);
    @Query("select u.question_id from ExamQuestion u where u.exam_id = ?1 and type = ?2")
    List<Long> getQuestionIdListByExam_idAndType(Long exam_id, Question.Type type);
}
