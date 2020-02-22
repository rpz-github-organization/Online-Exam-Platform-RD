package org.sicnuafcs.online_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stu_exam")
@IdClass(StuExamPK.class)
public class StuExam implements Serializable {
    public static enum Status {
        WILL,
        DONE
    }
    @Id
    @Column(length = 32,nullable = false)
    @NotNull
    private Long exam_id;
    @Id
    @Column(length = 32,nullable = false)
    @NotBlank(message = "stu_id 不为空")
    private String stu_id;
    @Id
    @Column(length = 32)
    @NotNull
    private Long question_id;
    @Column
    @Enumerated(EnumType.STRING)
    private Question.Type type;

    private Integer num;

    private Integer score;

    private String answer;
    @Enumerated(EnumType.STRING)
    private Status status;
}
