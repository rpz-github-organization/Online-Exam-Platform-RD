package org.sicnuafcs.online_exam_platform.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder
public class Register {

    private String stu_id;
    private String tea_id;
    private String email;
    private String telephone;
    private String password;
}
