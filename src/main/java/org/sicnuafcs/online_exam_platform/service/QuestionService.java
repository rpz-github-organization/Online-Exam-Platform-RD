package org.sicnuafcs.online_exam_platform.service;

import org.sicnuafcs.online_exam_platform.model.GetQuestion;
import org.sicnuafcs.online_exam_platform.model.Question;

public interface QuestionService {
    long saveQuestion(GetQuestion getQuestion) throws Exception;
}
