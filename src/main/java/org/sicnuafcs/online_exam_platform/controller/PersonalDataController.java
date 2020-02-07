package org.sicnuafcs.online_exam_platform.controller;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.model.Teacher;
import org.sicnuafcs.online_exam_platform.service.PersonalDataService;
import org.sicnuafcs.online_exam_platform.service.RegisterService;
import org.sicnuafcs.online_exam_platform.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/PersonalData")
public class PersonalDataController {

        @Resource
        PersonalDataService personalDataService;
        @Autowired
        RegisterService registerService;
        @Autowired
        SendMailService sendMailService;
        @GetMapping("/getTeacher")
        public AjaxResponse getTeacherData(HttpServletRequest request){

            Map m = (Map) request.getSession().getAttribute("userInfo");
            String ID = (String) m.get("id");

            Optional teacherData = personalDataService.getTeacherData(ID);
            if(teacherData.isPresent())
                return AjaxResponse.success(teacherData);//返回teacher类Data
            else
                return AjaxResponse.error(new CustomException(CustomExceptionType.SYSTEM_ERROR, "查找个人资料错误"));//返回500
        }
        @GetMapping("/getStudent")
         public AjaxResponse getStudentData(HttpServletRequest request){
        Map m = (Map) request.getSession().getAttribute("userInfo");
        String ID = (String) m.get("id");
        Optional studentData = personalDataService.getStudentData(ID);
        if(studentData.isPresent())
            return AjaxResponse.success(studentData);//返回teacher类
        else
            return AjaxResponse.error(new CustomException(CustomExceptionType.SYSTEM_ERROR, "查找个人资料错误"));//返回500
        }

        //此处的邮箱验证 是否是教师操作
        @PostMapping("/checkTeacherEmail")
        public @ResponseBody AjaxResponse sendTeacherEmail(@RequestBody Map<String, Object> params) throws Exception {
          String email = (String) params.get("email");
            personalDataService.checkTeacherEmail(email);
            log.info("发送邮件成功");
            return AjaxResponse.success();
         }
         //此处的邮箱验证 是否是本人操作
        @PostMapping("/checkStudentEmail")
        public @ResponseBody AjaxResponse sendStudentEmail(@RequestBody Map<String, Object> params) throws Exception {
            String email = (String) params.get("email");
            personalDataService.checkStudentEmail(email);
            log.info("发送邮件成功");
            return AjaxResponse.success();

        }
        //更新教师邮箱
        @PostMapping("/updateTeacherNewEmail")
        public @ResponseBody AjaxResponse updateTeacherNewEmail(HttpServletRequest request,@RequestBody Map<String, Object> params) throws Exception {
            Map m = (Map) request.getSession().getAttribute("userInfo");
            String ID = (String) m.get("id");
            personalDataService.updateTeacherEmail(ID,params);
            return AjaxResponse.success();
        }
        //更新邮箱
        @PostMapping("/updateStudentNewEmail")
        public @ResponseBody AjaxResponse updateStudentNewEmail(HttpServletRequest request,@RequestBody Map<String, Object> params) throws Exception {
            Map m = (Map) request.getSession().getAttribute("userInfo");
            String ID = (String) m.get("id");
            personalDataService.updateStudentEmail(ID,params);
            return AjaxResponse.success();
        }


        //更新密码
        @PostMapping("/updateTeacherPassword")
        public AjaxResponse updataTeacherPassword(HttpServletRequest request ,@RequestBody Map<String, Object> params){
            Map m = (Map) request.getSession().getAttribute("userInfo");
            String ID = (String) m.get("id");
            Teacher newTeacherData = personalDataService.updateTeacherPassword(ID, params);
            return AjaxResponse.success(newTeacherData);
        }
        //更新密码
        @PostMapping("/updateStudentPassword")
        public AjaxResponse updataStudentPassword(HttpServletRequest request ,@RequestBody Map<String, Object> params){
            Map m = (Map) request.getSession().getAttribute("userInfo");
            String ID = (String) m.get("id");
            Student newStudentData = personalDataService.updateStudentPassword(ID, params);
            return AjaxResponse.success(newStudentData);
        }

        @PostMapping("/editTeacherBaseData")
        public AjaxResponse editTeacherBaseData(HttpServletRequest request,@RequestBody Teacher newTeacherData){
            Map m = (Map) request.getSession().getAttribute("userInfo");
            String ID = (String) m.get("id");
            return AjaxResponse.success(personalDataService.editTeacherBaseData(ID,newTeacherData));
        }
        @PostMapping("/editStudentBaseData")
        public AjaxResponse editStudentBaseData(HttpServletRequest request,@RequestBody Student newStudentData){
            Map m = (Map) request.getSession().getAttribute("userInfo");
            String ID = (String) m.get("id");
            return AjaxResponse.success(personalDataService.editStudentBaseData(ID,newStudentData));
        }

}

