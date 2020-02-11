package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.dao.QuestionRepository;
import org.sicnuafcs.online_exam_platform.model.Question;
import org.sicnuafcs.online_exam_platform.service.QuestionService;
import org.sicnuafcs.online_exam_platform.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Slf4j
@EnableAsync
@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    RedisUtils redisUtils;
    @Override
    public long saveQuestion(Question question) throws Exception {
//        if (question.getType() == null) {
//            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"题目类型为空！");
//        }
//        if(question.getType().equals(Question.Type.Single) && question.getOptions().equals("")) {
//            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"选择题选项为空！");
//        }
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
