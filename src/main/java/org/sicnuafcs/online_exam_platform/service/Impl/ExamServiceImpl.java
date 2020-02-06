package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.ExamRepository;
import org.sicnuafcs.online_exam_platform.dao.StudentRepository;
import org.sicnuafcs.online_exam_platform.model.Exam;
import org.sicnuafcs.online_exam_platform.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Slf4j
@EnableAsync
@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    ExamRepository examRepository;
    @Override
    public void saveToExam(Exam exam) throws Exception {
        if (exam.getCo_id().equals("")) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"需要课程id！");
        }
        if (exam.getTea_id().equals("")) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"需要教师id！");
        }
        examRepository.save(exam);
    }
}
