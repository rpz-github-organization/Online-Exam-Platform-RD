package org.sicnuafcs.online_exam_platform.service.Impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.JudegConfig.JudgeConfig;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.QuestionRepository;
import org.sicnuafcs.online_exam_platform.dao.TeatCaseRepository;
//import org.sicnuafcs.online_exam_platform.model.TestCase;
import org.sicnuafcs.online_exam_platform.model.GetQuestion;
import org.sicnuafcs.online_exam_platform.model.Question;
import org.sicnuafcs.online_exam_platform.model.TestCase;
import org.sicnuafcs.online_exam_platform.model.ToTestCase;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.sicnuafcs.online_exam_platform.util.DockerUtils.*;

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
    @Autowired
    TeatCaseRepository teatCaseRepository;
    @Autowired
    QuestionRepository questionRepository;

    public JSONObject judge(String src, String language, Long testCaseId) {
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
        body.add("test_case_id", testCaseId);
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

    public void addTestCase(GetQuestion getQuestion) {
        if (getQuestion.getQuestion_id() == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "题号不能为空");
        }
        ArrayList<String> in = new ArrayList<>();
        ArrayList<String> out = new ArrayList<>();
        for (ToTestCase toTestCase : getQuestion.getTest_case()) {
            in.add(toTestCase.getInput());
            out.add(toTestCase.getOutput());
        }
        int isInDocker = 0;
        TestCase testCase = new TestCase(getQuestion.getQuestion_id(), in, out, isInDocker);
        teatCaseRepository.save(testCase);
    }

    //异步怎么实现
    public void writeFileToDocker(Long question_id, int type) {
        //保存的文件名字
        ArrayList<String> fileNames = new ArrayList<>();

        if (question_id == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "题号不能为空");
        }

        //获取in out输入输出数组，并写入文件
        ArrayList<String> in = teatCaseRepository.getOneByQuestion_id(question_id).getInput();
        ArrayList<String> out = teatCaseRepository.getOneByQuestion_id(question_id).getOutput();
        //先创建该id的目录
        String path = "/home/user/ojSystemSvr/test_cases/"+question_id;
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
            for (int i = 1; i <= in.size(); i++) {
                //创建.in文件
                String fileName = new String(path + "/" + i + ".in");
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
                    bw.write(in.get(i - 1));
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "创建in文件失败");
                }
                fileNames.add(i + ".in");
                //创建.out文件
                if (type == 1) {
                    fileName = new String(path + "/" + i + ".out.txt");
                    try {
                        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
                        bw.write(out.get(i - 1));
                        bw.flush();
                        bw.close();
                    } catch (IOException e) {
                        throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "创建out文件失败");
                    }
                    fileNames.add(i + ".out.txt");
                }
            }
        }
        else {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "题目重复");
        }
        log.info("写入文件成功");

        //将文件放入docker中
        addToDocker(path, question_id, fileNames);
        log.info("放入docker成功");

        //将源文件删除
        deleteFile(path, fileNames);
    }
    public void addToDocker(String path, Long question_id, ArrayList<String> fileNames) {
        String remote = "/test_cases/" + question_id;
        String container_id = "4564615a125d";
        //创建question_id目录
        String command = "mkdir /test_cases/" + question_id;
        String[] commands = {"docker", "exec", "4564615a125d", "/bin/bash", "-c", command};
        try {
            Process p = Runtime.getRuntime().exec(commands);
        } catch (IOException e) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "创建目录失败");
        }


        for (String filename : fileNames) {
            //依次放入docker的相应目录中
            String resource = path + "/" + filename;
            Object res = copyArchiveToContainerCmd(container_id, resource, remote);
        }
        TestCase testCase = teatCaseRepository.getOneByQuestion_id(question_id);
        testCase.setIsInDocker(1);
    }

    public Question.Type findQuestionType (Long question_id) {
        Question.Type type = questionRepository.findTypeByQuestion_id(question_id);
        if (type == null) {
            log.info(question_id + "的题目类型为空 不能执行写下来的操作");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "题目类型为空");
        }
        return type;
    }

    public void deleteFile(String path, ArrayList<String> fileNames) {
        for (String fileName : fileNames) {
            String path1 = path + "/" + fileName;
            File file = new File(path1);
            if (file.exists()) {
                file.delete();
            }
            else {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "文件不存在");
            }
        }
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        else {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "目录不存在");
        }
    }
}
