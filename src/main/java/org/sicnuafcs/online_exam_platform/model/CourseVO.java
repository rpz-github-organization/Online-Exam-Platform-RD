package org.sicnuafcs.online_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseVO  {


    private String co_id;

    private String name;

    private String credit;//学分

    private String school_hour;//学时

    private String exam_score;

    private String common_socre;

    private String exam_proportion;//卷面比例

    private Timestamp end_time;

    private Timestamp begin_time;

    private ArrayList major_list;

}
