package org.sicnuafcs.online_exam_platform.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.loadtime.Aj;
import org.sicnuafcs.online_exam_platform.config.DozerBeanMapperConfigure;
import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.QuestionRepository;
import org.sicnuafcs.online_exam_platform.dao.StuExamRepository;
import org.sicnuafcs.online_exam_platform.dao.*;
import org.sicnuafcs.online_exam_platform.model.*;
import org.sicnuafcs.online_exam_platform.service.AuthorityCheckService;
import org.sicnuafcs.online_exam_platform.service.ExamService;
import org.sicnuafcs.online_exam_platform.service.JudgeService;
import org.sicnuafcs.online_exam_platform.service.QuestionService;
import org.sicnuafcs.online_exam_platform.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;


@Slf4j
@Controller
@EnableAsync
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    QuestionService questionService;
    @Autowired
    ExamService examService;
    @Autowired
    JudgeService judgeService;
    @Autowired
    StuExamRepository stuExamRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ExamRepository examRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    DozerBeanMapperConfigure dozerBeanMapperConfigure;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    TestCaseRepository testCaseRepository;
    @Autowired
    AuthorityCheckService authorityCheckService;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    ExamQuestionRepository examQuestionRepository;
    /**
     * 老师添加/更新题目
     * @param getQuestion
     * @return
     * @throws Exception
     */
    @PostMapping("/addQuestion")
    public @ResponseBody
    AjaxResponse saveQuestion(@Valid @RequestBody GetQuestion getQuestion, HttpServletRequest httpServletRequest) throws Exception {
        authorityCheckService.checkTeacherAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        //先判断是否为添加题目
        Long question_id = getQuestion.getQuestion_id();
        Future<String> future = null;
        if (question_id == null) {
            question_id = redisUtils.incr("question_id");   //添加题目 id不存在 就新建一个question_id
            getQuestion.setQuestion_id(question_id);


            //如果是编程题
            if (getQuestion.getType() == (GetQuestion.Type.Normal_Program) || getQuestion.getType() == (GetQuestion.Type.SpecialJudge_Program)) {
                //去question类中找到type
                int type = 0; //类型1:normal;类型2：special judge
                if (getQuestion.getType() == GetQuestion.Type.Normal_Program) {   //判断编程题目类型
                    type = 1;
                } else {
                    type = 2;
                }
                future = judgeService.writeFile(question_id, type, getQuestion);
            }
        }
        else {
            //如果为修改 而且是编程题 删除之前的文件并重新创建
            if (getQuestion.getType() == (GetQuestion.Type.Normal_Program) || getQuestion.getType() == (GetQuestion.Type.SpecialJudge_Program)) {
                //删除
                judgeService.deleteFile(getQuestion.getQuestion_id());

                //再创建 去question类中找到type
                int type = 0; //类型1:normal;类型2：special judge
                if (getQuestion.getType() == GetQuestion.Type.Normal_Program) {   //判断编程题目类型
                    type = 1;
                } else {
                    type = 2;
                }
                future = judgeService.writeFile(question_id, type, getQuestion);
            }
        }

        questionService.saveQuestion(getQuestion);  //保存到question表
        try {
            future.get();
        } catch (Exception e) {
            throw new CustomException(CustomExceptionType.OTHER_ERROR, e.getMessage());
        }

        log.info("题目 添加/更新 成功");
        if (getQuestion.getType() == (GetQuestion.Type.Normal_Program) || getQuestion.getType() == (GetQuestion.Type.SpecialJudge_Program)) {
            judgeService.addTestCase(getQuestion);   //保存到test_case表
            log.info("添加/更新 测试用例成功");
        }
        return AjaxResponse.success(question_id);
    }

    /**
     * 老师添加/更新考试
     * @param exam
     * @return
     * @throws Exception
     */
    @PostMapping("/addExam")
    public @ResponseBody
    AjaxResponse saveToExam(@Valid @RequestBody Exam exam, HttpServletRequest httpServletRequest) throws Exception {
        authorityCheckService.checkTeacherAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        long exam_id = examService.saveToExam(exam);
        log.info("添加/更新 试卷成功");
        return AjaxResponse.success(exam_id);
    }

    /**
     * 学生编程题判题
     * @param program
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/judgeProgram")
    public @ResponseBody
    AjaxResponse judge(@Valid @RequestBody Program program, HttpServletRequest request, HttpServletRequest httpServletRequest) throws Exception {
        authorityCheckService.checkStudentAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        Map userInfo = (Map) request.getSession().getAttribute("userInfo");
        String stu_id = String.valueOf(userInfo.get("id"));
        JSONObject json = judgeService.judge(program.getCode(), program.getLanguage(), program.getQuestion_id());
        log.info("判题成功");
        JudgeResult judgeResult = judgeService.transformToResult(json, stu_id, program.getCode(), program.getLanguage(), program.getQuestion_id(), program.getExam_id());
        return AjaxResponse.success(judgeResult);
    }

    /**
     * 老师添加/更新题目到试卷
     * @param examQuestion
     * @return
     * @throws Exception
     */
    @PostMapping("/addQuestionToExam")
    public @ResponseBody
    AjaxResponse saveQuestionToExam(@Valid @RequestBody ExamQuestion examQuestion, HttpServletRequest httpServletRequest) throws Exception {
        authorityCheckService.checkTeacherAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        examService.saveQuestionToExam(examQuestion);
        log.info("添加/编辑 试题到试卷成功");
        return AjaxResponse.success();
    }

    /**
     * 学生获取学生试卷
     * @param str
     * @return
     * @throws Exception
     */
    @PostMapping("/getStuExam")
    public @ResponseBody
    AjaxResponse getStuExam(@RequestBody String str, HttpServletRequest httpServletRequest) throws Exception {
        authorityCheckService.checkStudentAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        //Long exam_id, String stu_id
        long exam_id =Long.parseLong(JSON.parseObject(str).get("exam_id").toString());
        String stu_id = JSON.parseObject(str).get("stu_id").toString();
//        System.out.println(str);
//        System.out.println(exam_id);
//        System.out.println(stu_id);
        ArrayList<StuExam> stuExamArrayList = new ArrayList<>();
        stuExamArrayList = stuExamRepository.getByExam_idAndStu_id(exam_id, stu_id);
        log.info("获取学号为：" + stu_id + "同学的试卷成功");
        return AjaxResponse.success(stuExamArrayList);
    }

    /**
     *学生获取题目结果
     * @param str
     * @return
     * @throws Exception
     */
    @PostMapping("/getQuestion")
    public @ResponseBody
    AjaxResponse getQuestion(@RequestBody String str, HttpServletRequest httpServletRequest) throws Exception {
        authorityCheckService.checkStudentAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        Question question;
        Long question_id = Long.parseLong(JSON.parseObject(str).get("question_id").toString());
        question = questionRepository.findById(question_id).get();
        if (question == null) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "do not exist question"));
        }
        Map<String, Object> ret = new HashMap<>();
        ret.put("question_id", question.getQuestion_id());
        ret.put("question", question.getQuestion());
        ret.put("type", question.getType());
        ret.put("answer", question.getAnswer());
        ret.put("tag", question.getTag());
        ret.put("tea_id", question.getTea_id());
        ret.put("tip", question.getTip());
        ret.put("options", question.getOptions());
        if (question.getType() == Question.Type.SpecialJudge_Program || question.getType() == Question.Type.Normal_Program) {
            TestCase testCase = testCaseRepository.getOneByQuestion_id(question_id);
            if (testCase != null) {
                if (testCase.getInput().size() > 0 ) {
                    ret.put("input", testCase.getInput().get(0));
                }
                if (testCase.getOutput().size() > 0) {
                    ret.put("output", testCase.getOutput().get(0));
                }
            }
        }
        log.info("获取questionid为：" + question_id + "的题目成功");
        return AjaxResponse.success(ret);
    }

    /**
     *学生获取考试信息
     * @param str
     * @return
     * @throws Exception
     */
    @PostMapping("/stuExamInfo")
    public @ResponseBody
    AjaxResponse stuExamInfo(@RequestBody String str, HttpServletRequest httpServletRequest) throws Exception {
        authorityCheckService.checkStudentAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        long exam_id = Long.parseLong(JSON.parseObject(str).get("exam_id").toString());
        Exam exam = examRepository.findExamByExam_id(exam_id);
        log.info("exam:" + exam.toString());
        Map<String, Object> ret = new HashMap<>();
        ret.put("name", exam.getName());
        ret.put("co_name", courseRepository.getNameByCo_id(exam.getCo_id()));
        ret.put("tea_name", teacherRepository.getNameByTea_id(exam.getTea_id()));
        ret.put("begin_time", exam.getBegin_time());
        ret.put("last_time", exam.getLast_time());
        if (exam.getProgress_status() == Exam.ProgressStatus.ING) {
            ret.put("status", 1);
        } else if (exam.getProgress_status() == Exam.ProgressStatus.WILL) {
            ret.put("status", 0);
        } else{
            ret.put("status", -1);
        }
        return AjaxResponse.success(ret);
    }

    /**
     * 学生提交试卷
     * id 答案 分数 和type
     */
    @PostMapping("/handInExam")
    public @ResponseBody AjaxResponse handInExam(@RequestBody String str, HttpServletRequest httpServletRequest) {
        authorityCheckService.checkStudentAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        Map m = (Map) httpServletRequest.getSession().getAttribute("userInfo");
        String stu_id = (String) m.get("id");
        long exam_id = Long.parseLong(JSON.parseObject(str).get("exam_id").toString());
        String data = JSON.parseObject(str).get("data").toString();
        examService.saveToStuExam(data, exam_id, stu_id);
        return AjaxResponse.success("success");
    }

    /**
     * 教师获取学生问答题部分
     * @param str
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/getDiscussion")
    public @ResponseBody AjaxResponse getDiscussion(@RequestBody String str, HttpServletRequest httpServletRequest) {
        authorityCheckService.checkTeacherAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        long exam_id = Long.parseLong(JSON.parseObject(str).get("exam_id").toString());
        Map data = examService.getDiscussion(exam_id);
        return AjaxResponse.success(data);
    }

    /**
     * 教师提交学生分数
     * @param req
     * @return
     */
    @PostMapping("/handInScore")
    public @ResponseBody AjaxResponse handInScore(@RequestBody HandInScore req, HttpServletRequest httpServletRequest) {
        authorityCheckService.checkTeacherAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        try {
            String stu_id = req.getStu_id();
            Long exam_id = req.getExam_id();
            for (StuExam stuExam: req.getScoreList()) {
                stuExamRepository.saveScore(stuExam.getScore(), stuExam.getQuestion_id(), exam_id, stu_id);
            }
            return AjaxResponse.success("success!");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResponse.error(new CustomException(CustomExceptionType.SYSTEM_ERROR, e.getMessage()));
        }
    }

    /**
     * 教师改变考试状态
     */
    @PostMapping("/changeExamStatus")
    public @ResponseBody AjaxResponse changeExamStatus(@RequestBody Exam req, HttpServletRequest httpServletRequest) {
        authorityCheckService.checkTeacherAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        try {
            if (req.getExam_id() != null && req.getProgress_status() != null) {
                examRepository.saveStatus(req.getExam_id(), req.getProgress_status());
                return AjaxResponse.success("success!");
            } else {
                return AjaxResponse.error(new CustomException(CustomExceptionType.SYSTEM_ERROR, "examid or status empty!"));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResponse.error(new CustomException(CustomExceptionType.SYSTEM_ERROR, e.getMessage()));
        }
    }

    /**
     * 学生考试详情页
     * 考试id
     * 考试名字 时间 时长 状态（考试未开始 考试中 考试结束未评分 考试结束已评分）考生人数 实际考试人数
     */
    @PostMapping("/getExamInfo")
    public @ResponseBody AjaxResponse getExamInfo(@RequestBody String str, HttpServletRequest httpServletRequest) {
        authorityCheckService.checkStudentAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        long exam_id = Long.parseLong(JSON.parseObject(str).get("exam_id").toString());
        int option = 1;
        Map res = examService.getExamInfo(exam_id, option);
        return AjaxResponse.success(res);
    }
    /**
     * 学生成绩详情页
     * 传入参数：试卷号、学号
     */
    @PostMapping("getStuScoreInfo")
    public @ResponseBody AjaxResponse getStuScoreInfo(@RequestBody String str, HttpServletRequest httpServletRequest) {
        authorityCheckService.checkStudentAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        try {
            String stu_id = JSON.parseObject(str).get("stu_id").toString();
            long exam_id = Long.parseLong(JSON.parseObject(str).get("exam_id").toString());
            Map<String, Object> ret = new HashMap<>();
            Exam exam = examRepository.findExamByExam_id(exam_id);
            if (exam == null) {
                AjaxResponse.error(new CustomException(CustomExceptionType.SYSTEM_ERROR, "error exam_id!"));
            }
            if (!exam.is_judge() || exam.getProgress_status() != Exam.ProgressStatus.DONE) {
                AjaxResponse.error(new CustomException(CustomExceptionType.SYSTEM_ERROR, "this exam is not judge! or is not done"));
            }
            List<Map<String,Object>> ques = examService.getStuScoreInfo(exam_id,stu_id);
            String stu_name = studentRepository.findNameByStu_id(stu_id);
            String tea_name = teacherRepository.getNameByTea_id(exam.getTea_id());
            ret.put("name", exam.getName());
            ret.put("stu_name", stu_name);
            ret.put("tea_name", tea_name);
            ret.put("begin_time", exam.getBegin_time());
            ret.put("Ques", ques);
            return AjaxResponse.success(ret);
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResponse.error(new CustomException(CustomExceptionType.SYSTEM_ERROR, e.getMessage()));
        }
    }

    /**
     * 老师分发试卷
     * @param str
     * @return
     * @throws Exception
     */
    @PostMapping("/distributeExamToStudent")
    public @ResponseBody
    AjaxResponse distributeExamToStudent(@RequestBody String str, HttpServletRequest httpServletRequest) throws Exception {
        authorityCheckService.checkTeacherAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        long exam_id =Long.parseLong(JSON.parseObject(str).get("exam_id").toString());
        String co_id = JSON.parseObject(str).get("co_id").toString();
        examService.distributeExamToStudent(exam_id, co_id);
        log.info("为：" + exam_id + "考试分发试卷成功");
        return AjaxResponse.success("success");
    }
    /**
     *教师获取学生题目结果
     * @param str
     * @return
     * @throws Exception
     */
    @PostMapping("/getQuestionTea")
    public @ResponseBody
    AjaxResponse getQuestionStu(@RequestBody String str, HttpServletRequest httpServletRequest) throws Exception {
        authorityCheckService.checkStudentAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        try {
            Question question;
            StuExam stuExam;
            ExamQuestion examQuestion;
            Long exam_id = Long.parseLong(JSON.parseObject(str).get("exam_id").toString());
            Long question_id = Long.parseLong(JSON.parseObject(str).get("question_id").toString());
            String stu_id = JSON.parseObject(str).get("stu_id").toString();
            question = questionRepository.findById(question_id).get();
            stuExam = stuExamRepository.getByExam_idAndStu_idAndQuestion_id(exam_id, stu_id, question_id);
            if (question == null || stuExam == null) {
                return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "question wrong"));
            }
            int score = examQuestionRepository.findScoreById(question_id, exam_id);
            Map<String, Object> ret = new HashMap<>();
            ret.put("question_id", question.getQuestion_id());
            ret.put("question", question.getQuestion());
            ret.put("type", question.getType());
            ret.put("answer", question.getAnswer());
            ret.put("stu_answer", stuExam.getAnswer());
            ret.put("options", question.getOptions());
            ret.put("getScore", stuExam.getScore());
            ret.put("score", score);
            if (question.getType() == Question.Type.SpecialJudge_Program || question.getType() == Question.Type.Normal_Program) {
                TestCase testCase = testCaseRepository.getOneByQuestion_id(question_id);
                if (testCase != null) {
                    if (testCase.getInput().size() > 0) {
                        ret.put("input", testCase.getInput().get(0));
                    }
                    if (testCase.getOutput().size() > 0) {
                        ret.put("output", testCase.getOutput().get(0));
                    }
                }
            }
            log.info("获取questionid为：" + question_id + "的题目成功");
            return AjaxResponse.success(ret);
        } catch (Exception e) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.SYSTEM_ERROR, e.getMessage()));
        }
    }

}

