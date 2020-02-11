package org.sicnuafcs.online_exam_platform.service;

import org.sicnuafcs.online_exam_platform.model.Question;

public interface QuestionService {
    long saveQuestion(Question question) throws Exception;
}
