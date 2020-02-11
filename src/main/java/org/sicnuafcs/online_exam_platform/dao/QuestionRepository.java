package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.Question;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("select answer from  Question  where question_id = ?1")
    String findAnswerById(long question_id);
}