package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.ExamQuestion;
import org.sicnuafcs.online_exam_platform.model.Question;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, String> {
    ArrayList<ExamQuestion> findByType(Question.Type type);
}
