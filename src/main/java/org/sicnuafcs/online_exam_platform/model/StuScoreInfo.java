package org.sicnuafcs.online_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuScoreInfo {
    private Question.Type type;
    private List<Map<String, Object>> detail;
    private int get;
    private int total;


}
