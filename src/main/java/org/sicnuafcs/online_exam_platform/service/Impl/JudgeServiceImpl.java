package org.sicnuafcs.online_exam_platform.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.JudgeConfig.JudgeConfig;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.*;
import org.sicnuafcs.online_exam_platform.model.*;
import org.sicnuafcs.online_exam_platform.service.HomePageService;
import org.sicnuafcs.online_exam_platform.service.JudgeService;
import org.sicnuafcs.online_exam_platform.util.LoadBalanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.*;

@Slf4j
@Service
public class JudgeServiceImpl implements JudgeService {
    @Autowired
    StuExamRepository stuExamRepository;
    @Autowired
    TestCaseRepository testCaseRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    ExamQuestionRepository examQuestionRepository;
    @Autowired
    HomePageService homePageService;

    //负责文件写入的线程池
    //TODO 需交将此处改为手动设置参数，创建线程池
    ExecutorService saveFileExecutor = Executors.newCachedThreadPool();
    //保证并发不出现异常
    static final Map<Long,String> saveStatusMap = new ConcurrentHashMap<>();

    public com.alibaba.fastjson.JSONObject judge(String src, String language, Long testCaseId) {
        if (language == null || language.length() == 0) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "编程语言不能为空");
        }

        String url = LoadBalanceUtils.GetUrl(testCaseId);
        log.info("url:"+url);
        RestTemplate restTemplate = new RestTemplate();

        JSONObject jsonObject = new JSONObject();
        //LinkedMultiValueMap body = new LinkedMultiValueMap();

        JudgeConfig.LangConfig langConfig;
        if ("C".equalsIgnoreCase(language)) {
            langConfig = new JudgeConfig.ClangConfig();
        } else if ("C++".equalsIgnoreCase(language)) {
            langConfig = new JudgeConfig.CPPLangConfig();
        } else if ("Java".equalsIgnoreCase(language)) {
            langConfig = new JudgeConfig.JavaLangConfig();
        } else if ("Python2".equalsIgnoreCase(language)) {
            langConfig = new JudgeConfig.PY2LangConfig();
        } else if ("Python3".equalsIgnoreCase(language)) {
            langConfig = new JudgeConfig.PY3LangConfig();
        } else {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "暂不支持此语言");
        }
        Map<String,Object> body = new HashMap<>();
        body.put("src", src);
        body.put("language_config", langConfig);
        body.put("max_cpu_time", 1000);
        body.put("max_memory", 134217728);
        body.put("test_case_id",testCaseId.toString());
        body.put("output", true);
        String jsonStr = JSON.toJSONString(body);
        String result = doPost(url, jsonStr);
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
        TestCase testCase = new TestCase(getQuestion.getQuestion_id(), homePageService.List2String(in), homePageService.List2String(out));
        testCaseRepository.save(testCase);
    }

    private void writeFile(Long question_id, int type,GetQuestion getQuestion) throws InterruptedException {
        //保存的文件名字
        ArrayList<String> fileNames = new ArrayList<>();

        //从getQuestion中获取in out输入输出数组，并写入文件
        ArrayList<String> in = new ArrayList<>();
        ArrayList<String> out = new ArrayList<>();
        for (ToTestCase toTestCase : getQuestion.getTest_case()) {
            in.add(toTestCase.getInput());
            out.add(toTestCase.getOutput());
        }
        //先创建该id的目录
        String path = "/home/user/ojSystem/test_cases/"+question_id;
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
                    saveStatusMap.put(question_id, "创建in文件失败");
                    return;
                    //throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "创建in文件失败");
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
                        saveStatusMap.put(question_id, "创建out文件失败");
                        return;
                        //throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "创建out文件失败");
                    }
                    fileNames.add(i + ".out");
                }
            }

            //创建info文件
            JSONObject info = new JSONObject();
            JSONObject test_cases = new JSONObject();
            if (type == 1) {    //normal
                info.put("spj",false);
                for (int i = 1; i <= in.size(); i++) {
                    JSONObject test_case = new JSONObject();
                    String output_md5 = transformToMd5(out.get(i - 1));
                    test_case.put("stripped_output_md5", output_md5);

                    //output
                    File file = new File(path + "/" + i +".out");
                    if (!file.exists()) {
                        System.out.println("文件不存在");
                    }
                    Long output_size = file.length();
                    String output_name = i + ".out";
                    test_case.put("output_size", output_size);
                    test_case.put("output_name", output_name);

                    //input
                    File files = new File(path + "/" + i +".in");
                    Long input_size = files.length();
                    String input_name = i + ".in";
                    test_case.put("input_name", input_name);
                    test_case.put("input_size", input_size);

                    test_cases.put(String.valueOf(i), test_case);
                }
                info.put("test_cases", test_cases);
            }
            else if (type == 2) {   //spj
                info.put("spj",true);
                for (int i = 1; i <= in.size(); i++) {
                    JSONObject test_case = new JSONObject();

                    //input
                    File file = new File(path + "/" + i +".in");
                    Long input_size = file.length();
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
                saveStatusMap.put(question_id, "创建info文件失败");
                return;
                //throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "创建info文件失败");
            }
        }
        else {
            saveStatusMap.put(question_id, "题目重复");
            return;
            //throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "题目重复");
        }
        log.info("写入文件成功");
        saveStatusMap.put(question_id, "写入文件成功");
        return;

//        //将文件放入docker中
//        addToDocker(path, question_id, fileNames);
//        log.info("放入docker成功");
    }

    @Override
    public void saveProgramQustionFile(Long question_id, int type, GetQuestion getQuestion) {
        if (question_id == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "题号不能为空");
        }
        saveStatusMap.put(question_id,"正在创建");

        saveFileExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    writeFile(question_id,type,getQuestion);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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

    public void deleteFile(Long question_id) {
        ArrayList<String> fileNames = getFileNames(question_id);
        String path = "/home/user/ojSystem/test_cases/" + question_id;   //文件夹路径
        for (String fileName : fileNames) {
            String path1 = path + "/" + fileName;
            File file = new File(path1);
            if (file.exists()) {
                boolean res = file.delete();
                if (!res) {
                    throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "in/out文件删除失败");
                }
            }
            else {
                log.info("文件不存在");
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "文件不存在");
            }
        }
        File file = new File(path + "/info");
        if (file.exists()) {
            boolean res = file.delete();
            if (!res) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "info文件删除失败");
            }
        }
        else {
            log.info("info文件不存在");
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "info文件不存在");
        }
        File files = new File(path);
        if (files.exists()) {
            boolean res = files.delete();
            if (!res) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "文件夹删除失败");
            }
        }
        else {
            log.info("目录不存在");
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "目录不存在");
        }
    }

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
            //conn.setConnectTimeout(30000);
            //conn.setReadTimeout(10000);
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

    public JudgeResult transformToResult(JSONObject json, String stu_id, String code, String language, Long question_id, Long exam_id) {
        JudgeResult judgeResult = new JudgeResult();
        try {
            String err = json.getString("err");
            if (err == null) {
                //编译成功 返回的json串
                judgeResult.setCompile_error(false);
                judgeResult.setError_message(null);

                judgeResult.setCode(code);
                judgeResult.setLanguage(language);

                //获取用户名
                judgeResult.setUsername(studentRepository.findNameByStu_id(stu_id));

                //获取测试用例结果列表
                int count = 0;
                int right = 0;  //正确测试用例的个数
                ArrayList<TestCaseRes> list = new ArrayList<>();
                JSONArray test_case_res = json.getJSONArray("data");
                for (Object test_case : test_case_res) {
                    TestCaseRes testCaseRes = new TestCaseRes();
                    JSONObject res = (JSONObject) test_case;
                    testCaseRes.setCase_num(Integer.valueOf(res.get("test_case").toString()));
                    testCaseRes.setRun_time(res.get("real_time").toString() + "ms");
                    Double memory = Double.parseDouble(res.get("memory").toString()) /1024 /1024;
                    DecimalFormat df = new DecimalFormat("0.00");
                    String memoryStr = df.format(memory.floatValue());
                    testCaseRes.setMemory(memoryStr  + "KB");  //单位
                    int result = Integer.parseInt(res.get("result").toString());
                    String judgeres = getJudgeResult(result);
                    if (judgeres.equals("答案正确")) {
                        right++;
                    }
                    testCaseRes.setResult(judgeres);
                    list.add(testCaseRes);
                    count++;
                }
                judgeResult.setTest_case_res(list);

                //状态 分数 代码 语言
                int Full = examQuestionRepository.findScoreById(question_id, exam_id);
                Integer r_score = stuExamRepository.getByExam_idAndStu_idAndQuestion_id(exam_id, stu_id, question_id).getScore();  //数据库存储的分数
                if (right == count) {
                    judgeResult.setStatus("答案正确");
                    judgeResult.setScore(Full);
                    stuExamRepository.saveScore(Full, question_id, exam_id, stu_id);
                    stuExamRepository.saveAnswer(code, question_id, exam_id, stu_id);
                } else if (right < count && right > 0) {
                    judgeResult.setStatus("部分正确");
                    judgeResult.setScore(right * Full / count);
                    if (r_score == null || r_score <= right * Full / count) {
                        stuExamRepository.saveScore(right * Full / count, question_id, exam_id, stu_id);
                        stuExamRepository.saveAnswer(code, question_id, exam_id, stu_id);
                    }
                } else if (right == 0) {
                    judgeResult.setStatus("答案错误");
                    judgeResult.setScore(0);
                    if (r_score == null || r_score == 0) {
                        stuExamRepository.saveScore(0, question_id, exam_id, stu_id);
                        stuExamRepository.saveAnswer(code, question_id, exam_id, stu_id);
                    }
                }

                //题号
                judgeResult.setNum(examQuestionRepository.findNumById(question_id, exam_id));
            } else {
                //编译失败
                judgeResult.setCompile_error(true);
                judgeResult.setError_message(json.getString("data"));
            }
            return judgeResult;
        }catch (Exception e) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "判题结果为空");
        }
    }

    public ArrayList<String> getFileNames(Long question_id) {
        ArrayList<String> fileNames = new ArrayList<>();

        if (question_id == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "题号不能为空");
        }
        //获取in 输入数组
        ArrayList<String> in = homePageService.String2List(testCaseRepository.getOneByQuestion_id(question_id).getInput());
        for (int i = 1; i <= in.size(); i++) {
            fileNames.add(i + ".in");
            fileNames.add(i + ".out");
        }
        return fileNames;
    }

    public String getJudgeResult(int result) {
        String str = null;
        switch (result) {
            case 0:
                str = "答案正确";
                break;
            case -1:
                str = "答案错误";
                break;
            case 1:
                str = "运行超时";
                break;
            case 2:
                str = "运行超时";
                break;
            case 3:
                str = "内存超限";
                break;
            case 4:
                str = "运行时错误";
                break;
            case 5:
                str = "系统错误";
                break;

        }
        return str;
    }
}
