package org.sicnuafcs.online_exam_platform.service;

import com.alibaba.fastjson.JSONObject;
import org.sicnuafcs.online_exam_platform.model.GetQuestion;
import org.sicnuafcs.online_exam_platform.model.Question;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.ArrayList;

/**
 * <p>Description:  xx</p>
 *
 * @author Hobert-Li
 * @version 1.0
 * @create 2020/2/6 18:11
 */

public interface JudgeService {

    JSONObject judge(String src, String language, Long testCaseId);

    void addTestCase(GetQuestion getQuestion);

    AsyncResult<String> writeFile(Long question_id, int type);

//    void addToDocker(String path, Long question_id, ArrayList<String> fileNames);

    Question.Type findQuestionType (Long question_id);

//    void deleteFile(String path, ArrayList<String> fileNames);

    String transformToMd5(String output);
}
