package org.sicnuafcs.online_exam_platform.model;

import javax.persistence.*;

@Entity

@Table(name = "course")

public class Course {

    @Id

    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private String co_id;

    @Column
    private String name;
    @Column
    private String credit;//学分
    @Column
    private String school_hour;//学时
    @Column
    private String exam_score;
    @Column
    private String common_socre;
    @Column
    private String exam_proportion;//卷面比例
    @Column
    private int end_time;
    @Column
    private Long begin_time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
