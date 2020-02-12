package org.sicnuafcs.online_exam_platform.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
import org.sicnuafcs.online_exam_platform.dao.QuestionRepository;
import org.sicnuafcs.online_exam_platform.dao.StuExamRepository;
import org.sicnuafcs.online_exam_platform.model.*;
import org.sicnuafcs.online_exam_platform.service.ExamService;
import org.sicnuafcs.online_exam_platform.service.Impl.ExamServiceImpl;
import org.sicnuafcs.online_exam_platform.service.JudgeService;
import org.sicnuafcs.online_exam_platform.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.spring.web.json.Json;

import javax.validation.Valid;
import java.util.ArrayList;


@Slf4j
@Controller
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    QuestionService questionService;
    @Autowired
    ExamService examService;
    @Autowired
    JudgeService judgeService;
    @Autowired
    StuExamRepository stuExamRepository;
    @Autowired
    QuestionRepository questionRepository;

    @PostMapping("/addQuestion")
    public @ResponseBody
    AjaxResponse saveQuestion(@Valid @RequestBody Question question) throws Exception {
//        if (question.getQuestion_id() != 0) {
//            log.info("更新题目操作");
//        }
        Long question_id = questionService.saveQuestion(question);
        log.info("题目 添加/更新 成功");
        return AjaxResponse.success(question_id);
    }
    @PostMapping("/addExam")
    public @ResponseBody
    AjaxResponse saveToExam(@Valid @RequestBody Exam exam) throws Exception {
        long exam_id = examService.saveToExam(exam);
        log.info("添加/更新 试卷成功");
        return AjaxResponse.success(exam_id);
    }

    @PostMapping("/judgeProgram")
    public @ResponseBody
    AjaxResponse judge(@Valid @RequestBody Program program) throws Exception {
        JSONObject json = judgeService.judge(program.getCode(), program.getLanguage());
        log.info("判题成功");
        return AjaxResponse.success(json);
    }

    @PostMapping("/addQuestionToExam")
    public @ResponseBody
    AjaxResponse saveQuestionToExam(@Valid @RequestBody ExamQuestion examQuestion) throws Exception {
        examService.saveQuestionToExam(examQuestion);
        log.info("添加/编辑 试题到试卷成功");
        return AjaxResponse.success();
    }

    @PostMapping("/getStuExam")
    public @ResponseBody
    AjaxResponse getStuExam(@RequestBody String str) throws Exception {
        //Long exam_id, String stu_id
        long exam_id =Long.parseLong(JSON.parseObject(str).get("exam_id").toString());
        String stu_id = JSON.parseObject(str).get("stu_id").toString();
        System.out.println(str);
        System.out.println(exam_id);
        System.out.println(stu_id);
        ArrayList<StuExam> stuExamArrayList = new ArrayList<>();
        stuExamArrayList = stuExamRepository.getByExam_idAndStu_id(exam_id, stu_id);
        log.info("获取学号为：" + stu_id + "同学的试卷成功");
        return AjaxResponse.success(stuExamArrayList);
    }

    @PostMapping("/getQuestion")
    public @ResponseBody
    AjaxResponse getQuestion(@RequestBody String str) throws Exception {
        Question question;
        Long question_id = Long.parseLong(JSON.parseObject(str).get("question_id").toString());
        question = questionRepository.findById(question_id).get();
        log.info("获取questionid为：" + question_id + "的题目成功");
        return AjaxResponse.success(question);
    }
    @PostMapping("/distributeExamToStudent")
    public @ResponseBody
    AjaxResponse distributeExamToStudent(@RequestBody String str) throws Exception {
        long exam_id =Long.parseLong(JSON.parseObject(str).get("exam_id").toString());
        String co_id = JSON.parseObject(str).get("co_id").toString();
        examService.distributeExamToStudent(exam_id, co_id);
        log.info("为：" + exam_id + "考试分发试卷成功");
        return AjaxResponse.success("success");
    }
}

