package org.sicnuafcs.online_exam_platform.service;

import org.springframework.boot.configurationprocessor.json.JSONObject;

/**
 * <p>Description:  xx</p>
 *
 * @author Hobert-Li
 * @version 1.0
 * @create 2020/2/6 18:11
 */

public interface JudgeService {

    JSONObject judge(String src, String language, Integer testCaseId);
}
