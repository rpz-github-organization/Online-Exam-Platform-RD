package org.sicnuafcs.online_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exam")
public class Exam {
    public static enum ProgressStatus {
        WILL,
        ING,
        DONE
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

    @Column(length = 16)
    @Enumerated(EnumType.STRING)
    private ProgressStatus progress_status; //状态

    private String name;
    private Long begin_time;//时间戳
    private int last_time; //持续时间
}
