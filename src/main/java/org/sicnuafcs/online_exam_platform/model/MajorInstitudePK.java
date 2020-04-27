package org.sicnuafcs.online_exam_platform.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class MajorInstitudePK implements Serializable {
    private String institude_id;
    private int grade;
    private String class_id;
}
