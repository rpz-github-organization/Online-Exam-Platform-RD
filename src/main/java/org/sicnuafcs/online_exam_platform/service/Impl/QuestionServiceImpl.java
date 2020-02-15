package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.QuestionRepository;
import org.sicnuafcs.online_exam_platform.model.GetQuestion;
import org.sicnuafcs.online_exam_platform.model.Question;
import org.sicnuafcs.online_exam_platform.service.QuestionService;
import org.sicnuafcs.online_exam_platform.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@EnableAsync
@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    RedisUtils redisUtils;
    @Resource
    private Mapper dozerMapper;

    @Override
    public long saveQuestion(GetQuestion getQuestion) throws Exception {
        //将getQuestion类转化为question类
        Question question = dozerMapper.map(getQuestion, Question.class);

        if (question.getQuestion_id() != null) {
            questionRepository.save(question);
            return question.getQuestion_id();

        } else {
            long question_id = redisUtils.incr("question_id");
            question.setQuestion_id(question_id);
            questionRepository.save(question);
            return question_id;
        }
    }

}
