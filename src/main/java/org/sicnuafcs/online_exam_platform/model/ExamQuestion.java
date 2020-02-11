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
    private Long exam_id;
    @Id
    @Column
    private Long question_id;

    private Integer num;

    @Enumerated(EnumType.STRING)
    private Question.Type type = null;

    private Integer score;
}
