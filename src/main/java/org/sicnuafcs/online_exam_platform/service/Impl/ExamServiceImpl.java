package org.sicnuafcs.online_exam_platform.service.Impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.*;
import org.sicnuafcs.online_exam_platform.model.*;
import org.sicnuafcs.online_exam_platform.service.ExamService;
import org.sicnuafcs.online_exam_platform.service.HomePageService;
import org.sicnuafcs.online_exam_platform.service.QuestionService;
import org.sicnuafcs.online_exam_platform.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    HomePageService homePageService;
    @Autowired
    DozerBeanMapper dozerBeanMapper;

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

    public void saveToStuExam(String data, Long exam_id, String stu_id) {
        ArrayList<String> stuExams= homePageService.String2List(data);
        for (String in : stuExams) {
            Map map = JSON.parseObject(in);
            StuExam stuExam = new StuExam();
            stuExam.setQuestion_id(Long.parseLong(map.get("question_id").toString()));
            String type = map.get("type").toString();
            switch (type) {
                case "Single":
                    stuExam.setType(Question.Type.Single);
                    break;
                case "MultipleChoice":
                    stuExam.setType(Question.Type.MultipleChoice);
                    break;
                case "Judge":
                    stuExam.setType(Question.Type.Judge);
                    break;
                case "FillInTheBlank":
                    stuExam.setType(Question.Type.FillInTheBlank);
                    break;
                case "Discussion":
                    stuExam.setType(Question.Type.Discussion);
                    break;
                case "Normal_Program":
                    stuExam.setType(Question.Type.Normal_Program);
                    stuExam.setScore(Integer.parseInt(map.get("score").toString()));
                    break;
                case "SpecialJudge_Program":
                    stuExam.setType(Question.Type.SpecialJudge_Program);
                    stuExam.setScore(Integer.parseInt(map.get("score").toString()));
                    break;
            }
            stuExam.setAnswer(map.get("answer").toString());
            stuExam.setExam_id(exam_id);
            stuExam.setStu_id(stu_id);
            stuExam.setStatus(StuExam.Status.DONE);
            stuExamRepository.save(stuExam);
        }
    }

    public Map getDiscussion(Long exam_id) {
        Exam.ProgressStatus examProgramStatus = examRepository.findExamByExam_id(exam_id).getProgress_status();
        if (!examProgramStatus.equals(Exam.ProgressStatus.DONE)) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该考试还未结束");
        }
        Map<String, Object> result = new HashMap<>();

        //题目部分
        List<Long> questionIdList = examQuestionRepository.getQuestionIdListByExam_idAndType(exam_id, Question.Type.Discussion);
        ArrayList<Map> questions = new ArrayList<>();
        for (Long question_id : questionIdList) {
            Map<String, Object> question = new HashMap<>();
            question.put("question_id", question_id);
            question.put("question", questionRepository.getQuestionByQuestion_id(question_id));
            question.put("answer", questionRepository.getAnswerByQuestion_id(question_id));
            questions.add(question);
        }
        result.put("question", questions);

        //学生信息部分
        ArrayList<String> stuIdList = stuExamRepository.getStu_idByQuestion_idAndExam_id(questionIdList.get(0), exam_id);
        ArrayList<Map> stu = new ArrayList<>();
        for (String stu_id : stuIdList) {
            Map<String, Object> stuExam = new HashMap<>();
            stuExam.put("id",stu_id);
            stuExam.put("name", studentRepository.findNameByStu_id(stu_id));
            ArrayList<Map> ress = new ArrayList<>();
            for (Long question_id : questionIdList) {
                Map<String, Object> res = new HashMap<>();
                res.put("question_id", question_id);
                res.put("answer", stuExamRepository.getAnswerById(question_id, exam_id, stu_id));
                ress.add(res);
            }
            stuExam.put("question", ress);
            stu.add(stuExam);
        }
        result.put("stuInfo", stu);
        return result;
    }
}
