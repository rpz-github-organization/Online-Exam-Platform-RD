package org.sicnuafcs.online_exam_platform.config;

import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.ExamQuestionRepository;
import org.sicnuafcs.online_exam_platform.dao.ExamRepository;
import org.sicnuafcs.online_exam_platform.dao.StuExamRepository;
import org.sicnuafcs.online_exam_platform.model.Exam;
import org.sicnuafcs.online_exam_platform.model.Question;
import org.sicnuafcs.online_exam_platform.model.StuExam;
import org.sicnuafcs.online_exam_platform.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Component;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;


@Component
@EnableScheduling
@EnableAsync
public class ChangeExamConfig {
    @Autowired
    ExamRepository examRepository;
    @Autowired
    ExamQuestionRepository examQuestionRepository;
    @Autowired
    ExamService examService;
    @Autowired
    StuExamRepository stuExamRepository;

    @Async
    @Scheduled(fixedDelay = 1000 * 30)  //间隔半分钟
    public void changeExamProgressStatus() throws InterruptedException {
        List<Exam> exams = examRepository.getExams();
        Long time = System.currentTimeMillis();
        int minute = 60000;
        if (!exams.isEmpty()) {
            for (Exam exam : exams) {
                if (exam.getProgress_status().equals(Exam.ProgressStatus.WILL)) {
                    if (exam.getBegin_time() <= time) {  //如果考试开始时间但是考试状态为will
                        examRepository.saveStatus(exam.getExam_id(), Exam.ProgressStatus.ING);
                    }
                }
                if (exam.getProgress_status().equals(Exam.ProgressStatus.ING)) {
                    Long end_time = exam.getBegin_time() + exam.getLast_time() * minute;
                    if (end_time < time) {
                        examRepository.saveStatus(exam.getExam_id(), Exam.ProgressStatus.DONE);
                    }
                }
                if (exam.getProgress_status().equals(Exam.ProgressStatus.DONE)) {
                    //如果考试时间到 判断是否已经阅卷
                    if (!exam.is_judge() && exam.is_distribute()) {
                        //如果未阅卷
                        List types = examQuestionRepository.getTypeByExam_id(exam.getExam_id());
                        //判断是否有讨论题
                        if (types.contains(Question.Type.Discussion)) {
                            //判断选择填空是否已经打过分
                            ArrayList<StuExam> stuExams = stuExamRepository.getByExam_id(exam.getExam_id());
                            for (StuExam stuExam : stuExams) {
                                if (stuExam.getType().equals(Question.Type.Single) || stuExam.getType().equals(Question.Type.Judge)) {
                                    if (stuExam.getScore() == null) {
                                        //选择判断题没有打分
                                        examService.judgeGeneralQuestion(exam.getExam_id());
                                    }
                                    //选择判断已打分
                                    break;
                                }
                            }
                        }
                        else {
                            //如果没有讨论题，则直接打分 且标记该试卷已阅卷结束
                            examService.judgeGeneralQuestion(exam.getExam_id());
                            examRepository.saveIs_judge(exam.getExam_id(),true);
                        }
                    }
                }
            }
        }
        Thread.sleep(1000 * 8 * 30);
    }

}
