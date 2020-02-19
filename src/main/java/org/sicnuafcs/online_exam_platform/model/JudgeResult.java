package org.sicnuafcs.online_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeResult {
    private String hand_in_time;    //提交时间
    private String status;      //状态
    private Integer score;      //分数`
    private String username;    //用户名`
    private String language;    //语言`
    private Integer num;        //题号`

    private ArrayList<TestCaseRes> teat_case_res;    //测试用例结果`

    private String code;        //代码`

    private Boolean compile_error;      //是否编译失败`
    private String error_message;       //编译失败的信息`

}
