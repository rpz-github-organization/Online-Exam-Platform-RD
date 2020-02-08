package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.ExamQuestion;
import org.sicnuafcs.online_exam_platform.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, String> {
    ArrayList<ExamQuestion> findByType(Question.Type type);
    @Query("select score from ExamQuestion where question_id = ?1 and exam_id = ?2")
    int findScoreById(long question_id, long exam_id);
}
