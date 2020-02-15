package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.*;
import org.sicnuafcs.online_exam_platform.model.*;
import org.sicnuafcs.online_exam_platform.service.ExamService;
import org.sicnuafcs.online_exam_platform.service.QuestionService;
import org.sicnuafcs.online_exam_platform.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
    @Autowired
    StuExamRepository stuExamRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    QuestionServiceImpl questionServiceImpl;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    StuCoRepository stuCoRepository;

    @Override
    public long saveToExam(Exam exam) throws Exception {
        //编辑试卷信息
        if (exam.getExam_id() != null) {
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

    @Override
    public void distributeExamToStudent(long exma_id, String co_id) throws Exception {
        ArrayList<String> studentList = (ArrayList<String>)stuCoRepository.findByCo_id(co_id);
        ArrayList<ExamQuestion> singleList = examQuestionRepository.findByExam_idAndType(exma_id, Question.Type.Single);
        ArrayList<ExamQuestion> judgeList = examQuestionRepository.findByExam_idAndType(exma_id, Question.Type.Judge);
        ArrayList<ExamQuestion> discussList = examQuestionRepository.findByExam_idAndType(exma_id, Question.Type.Discussion);
        ArrayList<ExamQuestion> normal_programList = examQuestionRepository.findByExam_idAndType(exma_id, Question.Type.Normal_Program);
        ArrayList<ExamQuestion> specialJudge_programList = examQuestionRepository.findByExam_idAndType(exma_id, Question.Type.SpecialJudge_Program);
        for (String stu_id : studentList) {
            randomCommon(singleList);
            randomCommon(judgeList);
            StuExam stuExam = new StuExam();
            stuExam.setStatus(StuExam.Status.WILL);
            for (ExamQuestion single : singleList) {
                stuExam.setStu_id(stu_id);
                stuExam.setExam_id(exma_id);
                stuExam.setQuestion_id(single.getQuestion_id());
                stuExam.setType(Question.Type.Single);
                stuExam.setNum(single.getNum());
                log.info("single num:" + stuExam.getNum());
                stuExamRepository.save(stuExam);
            }
            for (ExamQuestion judge : judgeList) {
                stuExam.setStu_id(stu_id);
                stuExam.setExam_id(exma_id);
                stuExam.setQuestion_id(judge.getQuestion_id());
                stuExam.setType(Question.Type.Judge);
                stuExam.setNum(judge.getNum());
                log.info("judge num:" + stuExam.getNum());
                stuExamRepository.save(stuExam);
            }
            for (ExamQuestion discuss : discussList) {
                stuExam.setStu_id(stu_id);
                stuExam.setExam_id(exma_id);
                stuExam.setQuestion_id(discuss.getQuestion_id());
                stuExam.setType(Question.Type.Discussion);
                stuExam.setNum(discuss.getNum());
                stuExamRepository.save(stuExam);
            }
            for (ExamQuestion normal_program : normal_programList) {
                stuExam.setStu_id(stu_id);
                stuExam.setExam_id(exma_id);
                stuExam.setQuestion_id(normal_program.getQuestion_id());
                stuExam.setType(Question.Type.Normal_Program);
                stuExam.setNum(normal_program.getNum());
                stuExamRepository.save(stuExam);
            }
            for (ExamQuestion specialJudge_program : specialJudge_programList) {
                stuExam.setStu_id(stu_id);
                stuExam.setExam_id(exma_id);
                stuExam.setQuestion_id(specialJudge_program.getQuestion_id());
                stuExam.setType(Question.Type.SpecialJudge_Program);
                stuExam.setNum(specialJudge_program.getNum());
                stuExamRepository.save(stuExam);
            }
        }
    }
    //选择题判断题 自动判分
    public void judgeGeneralQuestion(long exam_id) throws Exception {
        ArrayList<StuExam> stuExams = stuExamRepository.getByExam_id(exam_id);
        for (StuExam stuExam : stuExams) {
            if (stuExam.getType() != Question.Type.Single || stuExam.getType() != Question.Type.Judge) {
                continue;
            }
            long question_id = stuExam.getQuestion_id();
            if (stuExam.getAnswer().equals(questionRepository.findAnswerById(question_id))){
                stuExam.setScore(examQuestionRepository.findScoreById(question_id, exam_id));
            } else {
                stuExam.setScore(0);
            }
        }
    }

    //打乱题目顺序
    public  void randomCommon(ArrayList<ExamQuestion> questionsList) {
        log.info("size:", questionsList.size());
        int n = questionsList.size();
        int count = 0;
        Random random = new Random(1000);
        int num;
        for (ExamQuestion examQuestion : questionsList) {
            examQuestion.setNum(0);
        }
        while (count < n) {
            num = random.nextInt(n+1);
            num++;
            boolean flag = true;
            log.info("num:" + num);
            //判断是否已经存在
            for (ExamQuestion examQuestion : questionsList) {
                if (examQuestion.getNum() == num) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                questionsList.get(count).setNum(num);
                count++;
            }
        }
    }
}
