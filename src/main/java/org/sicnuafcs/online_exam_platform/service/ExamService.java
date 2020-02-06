package org.sicnuafcs.online_exam_platform.service;

import org.sicnuafcs.online_exam_platform.model.Exam;

public interface ExamService {
    void saveToExam(Exam exam) throws Exception;
}
