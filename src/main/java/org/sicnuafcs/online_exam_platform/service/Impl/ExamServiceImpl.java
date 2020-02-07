package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.ExamQuestionRepository;
import org.sicnuafcs.online_exam_platform.dao.ExamRepository;
import org.sicnuafcs.online_exam_platform.dao.QuestionRepository;
import org.sicnuafcs.online_exam_platform.model.Exam;
import org.sicnuafcs.online_exam_platform.model.ExamQuestion;
import org.sicnuafcs.online_exam_platform.service.ExamService;
import org.sicnuafcs.online_exam_platform.service.QuestionService;
import org.sicnuafcs.online_exam_platform.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Slf4j
@EnableAsync
@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    ExamRepository examRepository;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    ExamQuestionRepository examQuestionRepository;
    @Override
    public long saveToExam(Exam exam) throws Exception {
        //编辑试卷信息
        if (exam.getExam_id() != 0) {
            examRepository.save(exam);
            return exam.getExam_id();
        } else {
            long exam_id = redisUtils.incr("exam_id");
            exam.setExam_id(exam_id);
            examRepository.save(exam);
            return exam_id;
        }
    }

    @Override
    public void saveQuestionToExam(ExamQuestion examQuestion) throws Exception {
        examQuestionRepository.save(examQuestion);
    }
}
