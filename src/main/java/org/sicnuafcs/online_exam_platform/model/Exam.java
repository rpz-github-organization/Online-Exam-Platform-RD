package org.sicnuafcs.online_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exam {
    public static enum ProgressStatus {
        ING,
        DONE
    }
    private String co_id; //课程id
    private String tea_id; //教师id
    private int exam_id;
    private ProgressStatus progress_status; //状态
    private String name;
    private String begin_time;
    private String last_time; //持续时间
    private String sinle_list; //单选题列表
    private String fillintheblank_list; //填空题列表
    private String judge_list; //判断题列表
    private String discussion_list; //问答题列表
    private String program_list; //编程题列表

}
