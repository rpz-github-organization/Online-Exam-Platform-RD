package org.sicnuafcs.online_exam_platform.controller;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.asm.internal.ProgramElement;
import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
import org.sicnuafcs.online_exam_platform.model.Exam;
import org.sicnuafcs.online_exam_platform.model.Program;
import org.sicnuafcs.online_exam_platform.model.Question;
import org.sicnuafcs.online_exam_platform.service.ExamService;
import org.sicnuafcs.online_exam_platform.service.JudgeService;
import org.sicnuafcs.online_exam_platform.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;


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

    @PostMapping("/addQuestion")
    public @ResponseBody
    AjaxResponse saveQuestion(@Valid @RequestBody Question question) throws Exception {
        if (question.getQuestion_id() != null) {
            log.info("更新题目操作");
        }
        Long question_id = questionService.saveQuestion(question);
        log.info("题目 添加/更新 成功");
        return AjaxResponse.success(question_id);
    }
    @PostMapping("/addExam")
    public @ResponseBody
    AjaxResponse saveToExam(@Valid @RequestBody Exam exam) throws Exception {
        examService.saveToExam(exam);
        log.info("添加/更新 试卷成功");
        return AjaxResponse.success();
    }

    @PostMapping("/judgeProgram")
    public @ResponseBody
    AjaxResponse judge(@Valid @RequestBody Program program) throws Exception {
        JSONObject json = judgeService.judge(program.getCode(), program.getLanguage());
        log.info("判题成功");
        return AjaxResponse.success(json);
    }

}

