package org.sicnuafcs.online_exam_platform.config;

import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.ExamRepository;
import org.sicnuafcs.online_exam_platform.model.Exam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@EnableScheduling
@EnableAsync
public class ChangeExamProgressStatusConfig {
    @Autowired
    ExamRepository examRepository;

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
                else if (exam.getProgress_status().equals(Exam.ProgressStatus.ING)) {
                    Long end_time = exam.getBegin_time() + exam.getLast_time() * minute;
                    if (end_time < time) {
                        examRepository.saveStatus(exam.getExam_id(), Exam.ProgressStatus.DONE);
                    }
                }
            }
        }
        Thread.sleep(1000 * 8 * 30);
    }

}
