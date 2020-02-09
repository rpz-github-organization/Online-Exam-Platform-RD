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
@Table(name = "exam_question")
@IdClass(ExamQuestionPK.class)
public class ExamQuestion {
    @Id
    @Column
    @NotBlank(message = "exam_id 不为空！")
    private long exam_id;
    @Id
    @Column
    @NotBlank(message = "question_id 不为空！")
    private long question_id;

    @NotBlank(message = "题号不为空")
    private int num;
    @NotBlank(message = "题目类型不为空")
    private Question.Type type;
    @NotBlank(message = "题目分数不为空")
    private int score;
}
