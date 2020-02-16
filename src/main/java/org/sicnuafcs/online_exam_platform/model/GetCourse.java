package org.sicnuafcs.online_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCourse implements Serializable {
    @NotBlank(message = "学号不能为空")
    private String stu_id;

    @NotBlank(message = "选项不能为空")
    private int option;
}
