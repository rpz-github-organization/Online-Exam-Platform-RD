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

    private String stu_id=null;
    private String tea_id=null;
    private String email=null;
    private String telephone=null;
    private String password=null;
    private String code=null;
}
