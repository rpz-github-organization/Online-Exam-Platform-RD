package org.sicnuafcs.online_exam_platform.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "question")

public class Question implements Serializable {
    public static enum Type {
        Single,
        MultipleChoice,
        Judge,
        FillInTheBlank,
        Discussion,
        Normal_Program,
        SpecialJudge_Program;
    }

    @Id
    @Column
    private Long question_id;

    @Column(length = 16)
    private  String tea_id;

    @Column(length = 16)
    private String question;

    @Column(length = 128)
    private String tag;

    @Column(length = 256)
    private  String options;

    @Column
    private String answer;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type = null;

    @Column
    private String tip; //提示
}
