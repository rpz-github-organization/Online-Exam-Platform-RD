package org.sicnuafcs.online_exam_platform.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public com.alibaba.fastjson.JSONObject judge(String src, String language, Long testCaseId) {
        if (language == null || language.length() == 0) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "编程语言不能为空");
        }

        String url = "http://121.36.18.182:10085/judge";

        RestTemplate restTemplate = new RestTemplate();

        JSONObject jsonObject = new JSONObject();
        //LinkedMultiValueMap body = new LinkedMultiValueMap();

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
        Map<String,Object> body = new HashMap<>();
        body.put("src", src);
        body.put("language_config", langConfig);
        body.put("max_cpu_time", 1000);
        body.put("max_memory", 134217728);
        body.put("test_case_id","normal");
        body.put("output", true);
        String jsonStr = JSON.toJSONString(body);
        String result = doPost("http://121.36.18.182:10085/judge", jsonStr);
        log.info("result" + result);
        log.info("strjson" + jsonStr);
        return JSON.parseObject(result);
        /*
        body.add("src", src);
        body.add("language_config", JSON.toJSONString(langConfig));
        body.add("max_cpu_time", 1000);
        body.add("max_memory", 134217728);
        body.add("test_case_id", "normal");
        body.add("test_case_id", testCaseId);
        body.add("output", true);


        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Judge-Server-Token", "b82fd881d1303ba9794e19b7f4a5e2b79231d065f744e72172ad9ee792909126");
        headers.set("Content-Type","application/json");

        HttpEntity httpEntity = new HttpEntity(body, headers);

        JSONObject res = null;
        try {
            ResponseEntity<String> strbody = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            log.info("info"+strbody);
            res = new JSONObject(strbody.getBody());
        } catch (JSONException e) {
            log.info(e.toString());
        }
        return res;
        */
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
    public void writeFile(Long question_id, int type) {
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
                    fileName = new String(path + "/" + i + ".out");
                    try {
                        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
                        bw.write(out.get(i - 1));
                        bw.flush();
                        bw.close();
                    } catch (IOException e) {
                        throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "创建out文件失败");
                    }
                    fileNames.add(i + ".out");
                }
            }
            //创建info文件
            JSONObject info = new JSONObject();
            JSONObject test_cases = new JSONObject();
            if (type == 1) {    //normal
                info.put("spj","false");
                for (int i = 1; i <= in.size(); i++) {
                    JSONObject test_case = new JSONObject();
                    String output_md5 = transformToMd5(out.get(i - 1));
                    test_case.put("stripped_output_md5", output_md5);

                    //output
                    int output_size = out.get(i - 1).split(" ").length;
                    String output_name = i + ".out";
                    test_case.put("output_size", output_size);
                    test_case.put("output_name", output_name);

                    //input
                    int input_size = in.get(i - 1).split(" ").length;
                    String input_name = i + ".in";
                    test_case.put("input_name", input_name);
                    test_case.put("input_size", input_size);

                    test_cases.put(String.valueOf(i), test_case);
                }
                info.put("test_cases", test_cases);
            }
            else if (type == 2) {   //spj
                info.put("spj","true");
                for (int i = 1; i <= in.size(); i++) {
                    JSONObject test_case = new JSONObject();

                    //input
                    int input_size = in.get(i - 1).split(" ").length;
                    String input_name = i + ".in";
                    test_case.put("input_name", input_name);
                    test_case.put("input_size", input_size);

                    test_cases.put(String.valueOf(i), test_case);
                }
                info.put("test_cases", test_cases);
            }
            String fileName = new String(path + "/info");
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
                bw.write(info.toJSONString());
                bw.flush();
                bw.close();
            } catch (IOException e) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "创建info文件失败");
            }
        }
        else {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "题目重复");
        }
        log.info("写入文件成功");

//        //将文件放入docker中
//        addToDocker(path, question_id, fileNames);
//        log.info("放入docker成功");
//
//        //将源文件删除
//        deleteFile(path, fileNames);
    }

//    public void addToDocker(String path, Long question_id, ArrayList<String> fileNames) {
////        String remote = "/test_cases/" + question_id;
////        String container_id = "4564615a125d";
////        //创建question_id目录
////        String command = "mkdir /test_cases/" + question_id;
////        String[] commands = {"docker", "exec", "4564615a125d", "/bin/bash", "-c", command};
////        try {
////            Process p = Runtime.getRuntime().exec(commands);
////        } catch (IOException e) {
////            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "创建目录失败");
////        }
////
////
////        for (String filename : fileNames) {
////            //依次放入docker的相应目录中
////            String resource = path + "/" + filename;
////            Object res = copyArchiveToContainerCmd(container_id, resource, remote);
////        }
////        TestCase testCase = teatCaseRepository.getOneByQuestion_id(question_id);
////        testCase.setIsInDocker(1);
////    }

    public Question.Type findQuestionType (Long question_id) {
        Question.Type type = questionRepository.findTypeByQuestion_id(question_id);
        if (type == null) {
            log.info(question_id + "的题目类型为空 不能执行写下来的操作");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "题目类型为空");
        }
        return type;
    }

//    public void deleteFile(String path, ArrayList<String> fileNames) {
//        for (String fileName : fileNames) {
//            String path1 = path + "/" + fileName;
//            File file = new File(path1);
//            if (file.exists()) {
//                file.delete();
//            }
//            else {
//                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "文件不存在");
//            }
//        }
//        File file = new File(path);
//        if (file.exists()) {
//            file.delete();
//        }
//        else {
//            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "目录不存在");
//        }
//    }

    public String doPost(String URL, String jsonStr){
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try{
            java.net.URL url = new URL(URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("X-Judge-Server-Token", "b82fd881d1303ba9794e19b7f4a5e2b79231d065f744e72172ad9ee792909126");
            //获取输出流
            out = new OutputStreamWriter(conn.getOutputStream());

            out.write(jsonStr);
            out.flush();
            out.close();
            //取得输入流，并使用Reader读取
            if (200 == conn.getResponseCode()){
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null){
                    result.append(line);
                    System.out.println(line);
                }
            }else{
                System.out.println("ResponseCode is an error code:" + conn.getResponseCode());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        return result.toString();
    }

    public String transformToMd5(String output) {
        //加密之后所得字节数组
        byte[] bytes = null;
        try {
            // 获取MD5算法实例 得到一个md5的消息摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //添加要进行计算摘要的信息
            md.update(output.getBytes());
            //得到该摘要
            bytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "md5算法不存在");
        }
        StringBuffer sb = new StringBuffer();
        for (byte aByte : bytes) {
            String s = Integer.toHexString(0xff & aByte);
            if (s.length() == 1) {
                sb.append("0" + s);
            }
            else {
                sb.append(s);
            }
        }
        return sb.toString();
    }
}
