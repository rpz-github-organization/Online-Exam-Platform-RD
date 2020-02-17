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
    private String cur_time;
    private String status;
    private Integer score;
    private Integer num;
    private ArrayList<String> teat_case_res;
    private String code;
    private String error_message;
    private String username;
    private String compiler;
    private String run_time;
}
