package org.sicnuafcs.online_exam_platform.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExamQuestionPK implements Serializable {
    private long exam_id;
    private long question_id;
}
