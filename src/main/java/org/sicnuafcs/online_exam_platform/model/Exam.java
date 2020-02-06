package org.sicnuafcs.online_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(ExamPk.class)
@Table(name = "exam")
public class Exam {
    public static enum ProgressStatus {
        ING,
        DONE
    }
    @Id
    @Column
    private String co_id; //课程id

    @Id
    @Column
    private String tea_id; //教师id

    @Id
    @Column
    private int exam_id;

    private ProgressStatus progress_status; //状态
    private String name;
    private String begin_time;
    private int last_time; //持续时间
    @Column(columnDefinition = "text")
    private String single_list; //单选题列表
    @Column(columnDefinition = "text")
    private String fillintheblank_list; //填空题列表
    @Column(columnDefinition = "text")
    private String judge_list; //判断题列表
    @Column(columnDefinition = "text")
    private String discussion_list; //问答题列表
    @Column(columnDefinition = "text")
    private String program_list; //编程题列表

}
