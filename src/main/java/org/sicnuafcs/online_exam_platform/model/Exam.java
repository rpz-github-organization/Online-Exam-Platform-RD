package org.sicnuafcs.online_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exam")
public class Exam implements Serializable {
    public static enum ProgressStatus {
        WILL,
        ING,
        DONE,
    }

    @Column
    @NotBlank(message = "课程号不能为空")
    private String co_id; //课程id

    @Column
    @NotBlank(message = "工号不能为空")
    private String tea_id; //教师id

    @Id
    @Column
    private Long exam_id;

    @Column
    @Enumerated(EnumType.STRING)
    private ProgressStatus progress_status = null; //状态

    private String name;
    private Long begin_time;//时间戳
    private int last_time; //持续时间
    private boolean is_judge; //是否判分
    private boolean is_distribute;//是否发布
}
