package org.sicnuafcs.online_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Program {
    @NotBlank(message = "代码不为空")
    private String code;
    @NotBlank(message = "语言不为空")
    private String language;
}
