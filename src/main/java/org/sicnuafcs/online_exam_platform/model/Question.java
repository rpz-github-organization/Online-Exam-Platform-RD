package org.sicnuafcs.online_exam_platform.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Question")

public class

Question {
    public static enum Type {
        Single,
        MultipleChoice,
        Judge,
        FillInTheBlank,
        Discussion,
        Program;
    }

    @Id
    @Column(length = 16, nullable = false)
    private long question_id;

    @Column(length = 16, nullable = false)
    @NotBlank(message = "教师id不为空")
    private  String tea_id;

    @Column(length = 16, nullable = false)
    private String question;

    @Column(length = 128, nullable = false)
    private String tag;

    @Column(length = 256)
    private  String options;

    @Column(nullable = false)
    private String answer;

    @Enumerated(EnumType.STRING)
    private Type type = null;

    private String input;   //编程题示例输入
    private String output;  //编程题示例输出
    private String tip; //提示
}
