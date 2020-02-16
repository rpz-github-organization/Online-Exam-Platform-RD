package org.sicnuafcs.online_exam_platform.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetQuestion implements Serializable {

    public static enum Type {
        Single,
        MultipleChoice,
        Judge,
        FillInTheBlank,
        Discussion,
        Program,
        Normal_Program,
        SpecialJudge_Program;
    }

    private Long question_id;
    @NotBlank(message = "教师id不为空")
    private  String tea_id;
    private String question;
    private String tag;
    private  String options;
    private String answer;

    @Enumerated(EnumType.STRING)
    private Type type = null;

    private ArrayList<ToTestCase> test_case;

    private String tip; //提示
}
