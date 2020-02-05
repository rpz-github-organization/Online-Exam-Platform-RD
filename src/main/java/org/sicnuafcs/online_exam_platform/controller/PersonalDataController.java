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
//        @Autowired
//        RegisterService registerService;
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

        //此处的邮箱验证 是否是本人操作
        @GetMapping("/checkTeacherEmail")
        public @ResponseBody AjaxResponse sendTeacherEmail(HttpServletRequest request) throws Exception {
            Map m = (Map) request.getSession().getAttribute("userInfo");
            String ID = (String) m.get("id");
            personalDataService.checkTeacherEmail(ID);
            log.info("发送邮件成功");
            return AjaxResponse.success();
         }

        @GetMapping("/checkStudentEmail")
        public @ResponseBody AjaxResponse sendStudentEmail(HttpServletRequest request) throws Exception {
            Map m = (Map) request.getSession().getAttribute("userInfo");
            String ID = (String) m.get("id");
            personalDataService.checkStudentEmail(ID);
            log.info("发送邮件成功");
            return AjaxResponse.success();

        }
//        //看某个邮箱否在使用，
//        @PostMapping("/checkNewEmail")
//        public @ResponseBody AjaxResponse sendNewEmail(@RequestBody Map<String, Object> params) throws Exception {
//            String newEmail = (String) params.get("newEamil");
////            personalDataService.checkTeacherEmail(newEmail);
////            registerService.sendStudentEmail(newEmail);
//            sendMailService.sendEmail(newEmail);
//            log.info("发送邮件成功");
//            return AjaxResponse.success();
//        }



        @PostMapping("/updateTeacherData")
        public AjaxResponse updataTeacherPassword(HttpServletRequest request ,@RequestBody Map<String, Object> params){
            Map m = (Map) request.getSession().getAttribute("userInfo");
            String ID = (String) m.get("id");
            Teacher newTeacherData = personalDataService.updateTeacherData(ID, params);
            return AjaxResponse.success(newTeacherData);
        }
        @PostMapping("/updateStudentData")
        public AjaxResponse updataStudentPassword(HttpServletRequest request ,@RequestBody Map<String, Object> params){
            Map m = (Map) request.getSession().getAttribute("userInfo");
            String ID = (String) m.get("id");
            Student newStudentData = personalDataService.updateStudentData(ID, params);
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

