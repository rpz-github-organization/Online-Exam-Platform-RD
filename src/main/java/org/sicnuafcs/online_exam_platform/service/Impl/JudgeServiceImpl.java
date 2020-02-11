package org.sicnuafcs.online_exam_platform.service.Impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.JudegConfig.JudgeConfig;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.service.JudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description:  xx</p>
 *
 * @author Hobert-Li
 * @version 1.0
 * @create 2020/2/6 18:12
 */

@Slf4j
@EnableAsync
@Service
public class JudgeServiceImpl implements JudgeService {

    public JSONObject judge(String src, String language) {
        if (language == null || language.length() == 0) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "编程语言不能为空");
        }

        String url = "http://121.36.18.182:10085/judge";

        RestTemplate restTemplate = new RestTemplate();

        JSONObject jsonObject = new JSONObject();
        LinkedMultiValueMap body = new LinkedMultiValueMap();

        JudgeConfig.LangConfig langConfig;
        if ("c".equalsIgnoreCase(language)) {
            langConfig = new JudgeConfig.ClangConfig();
        } else if ("cpp".equalsIgnoreCase(language)) {
            langConfig = new JudgeConfig.CPPLangConfig();
        } else if ("java".equalsIgnoreCase(language)) {
            langConfig = new JudgeConfig.JavaLangConfig();
        } else if ("python2".equalsIgnoreCase(language)) {
            langConfig = new JudgeConfig.PY2LangConfig();
        } else if ("python3".equalsIgnoreCase(language)) {
            langConfig = new JudgeConfig.PY3LangConfig();
        } else {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "暂不支持此语言");
        }
        body.add("src", src);
        body.add("language_config", JSON.toJSONString(langConfig));
        body.add("max_cpu_time", 1000);
        body.add("max_memory", 134217728);
        body.add("test_case_id", "normal");
        body.add("output", true);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Judge-Server-Token", "b82fd881d1303ba9794e19b7f4a5e2b79231d065f744e72172ad9ee792909126");

        HttpEntity httpEntity = new HttpEntity(body, headers);

        JSONObject res = null;
        try {
            ResponseEntity<String> strbody = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            res = new JSONObject(strbody.getBody());
        } catch (JSONException e) {
            log.info(e.toString());
        }
        return res;
    }

}
