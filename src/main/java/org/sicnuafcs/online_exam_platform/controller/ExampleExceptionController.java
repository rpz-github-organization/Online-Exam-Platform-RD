package org.sicnuafcs.online_exam_platform.controller;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.AjaxResponse;
import org.sicnuafcs.online_exam_platform.service.ExampleExceptionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 异常controller模板
 */
@Slf4j
@Controller
@RequestMapping("/example")
public class ExampleExceptionController {
    @Resource
    ExampleExceptionService exampleExceptionService;

    @GetMapping("/ex/system")
    public @ResponseBody
    AjaxResponse system() {

        exampleExceptionService.systemBizError();

        return AjaxResponse.success();
    }


    @GetMapping("/ex/user")
    public @ResponseBody  AjaxResponse user(Integer input) {

        return AjaxResponse.success(exampleExceptionService.userBizError(input));
    }
}
