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
@Table(name = "stu_exam")
@IdClass(StuExamPK.class)
public class StuExam {
    public static enum Status {
        WILL,
        DONE
    }
    @Id
    @Column
    @NotBlank(message = "exam_id 不为空")
    private Long exam_id;
    @Id
    @Column
    @NotBlank(message = "stu_id 不为空")
    private String stu_id;
    @Id
    @Column
    @NotBlank(message = "question_id 不为空")
    private Long question_id;

    @Enumerated(EnumType.STRING)
    private Question.Type type;

    private Integer num;

    private Integer score;

    private String answer;
    private Status status;
}
