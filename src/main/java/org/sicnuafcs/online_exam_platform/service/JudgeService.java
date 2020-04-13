package org.sicnuafcs.online_exam_platform.service;

import com.alibaba.fastjson.JSONObject;
import org.sicnuafcs.online_exam_platform.model.GetQuestion;
import org.sicnuafcs.online_exam_platform.model.JudgeResult;
import org.sicnuafcs.online_exam_platform.model.Question;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.ArrayList;
import java.util.concurrent.Future;

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

    //Future<String> writeFile(Long question_id, int type, GetQuestion getQuestion) throws InterruptedException;

    void saveProgramQustionFile(Long question_id, int type, GetQuestion getQuestion);
//    void addToDocker(String path, Long question_id, ArrayList<String> fileNames);

    void deleteFile(Long question_id);

    String transformToMd5(String output);

    JudgeResult transformToResult(JSONObject json, String stu_id, String code, String language, Long question_id, Long exam_id);

    ArrayList<String> getFileNames(Long question_id);

    String getJudgeResult(int result);
}
