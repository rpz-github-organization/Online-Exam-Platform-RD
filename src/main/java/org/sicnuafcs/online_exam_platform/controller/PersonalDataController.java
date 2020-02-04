package org.sicnuafcs.online_exam_platform.controller;

import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.model.Student;
import org.sicnuafcs.online_exam_platform.model.Teacher;
import org.sicnuafcs.online_exam_platform.service.PersonalDataService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/PersonalData")
public class PersonalDataController {

        @Resource
        PersonalDataService personalDataService;


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


        @PutMapping("/updateTeacherPassword")
        public AjaxResponse updataTeacherPassword(HttpServletRequest request ,@RequestBody Map<String, Object> params){
            Map m = (Map) request.getSession().getAttribute("userInfo");
            String ID = (String) m.get("id");
            String newPassword = (String) params.get("newPassword");


            Teacher newTeacherData = personalDataService.updataTeacherPassword(ID, newPassword);
           if(newTeacherData!=null)
                return AjaxResponse.success(newTeacherData);
            else
                return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "与原始密码相同，修改密码中断"));//返回500
        }
        @PutMapping("/updateStudentPassword")
        public AjaxResponse updataStudentPassword(HttpServletRequest request ,@RequestBody Map<String, Object> params){
        Map m = (Map) request.getSession().getAttribute("userInfo");
        String ID = (String) m.get("id");
        String newPassword = (String) params.get("newPassword");


        Student newStudentData = personalDataService.updataStudentPassword(ID, newPassword);
        if(newStudentData!=null)
            return AjaxResponse.success(newStudentData);
        else
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "与原始密码相同，修改密码中断"));
        }


}
