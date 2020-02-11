package org.sicnuafcs.online_exam_platform.service;

import org.sicnuafcs.online_exam_platform.model.Exam;
import org.sicnuafcs.online_exam_platform.model.ExamQuestion;

public interface ExamService {
    long saveToExam(Exam exam) throws Exception;
    void saveQuestionToExam(ExamQuestion examQuestion) throws Exception;
}
