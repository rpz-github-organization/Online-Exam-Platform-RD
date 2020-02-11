package org.sicnuafcs.online_exam_platform.model;

import javax.persistence.*;

@Entity
public class Stu_exam {
    @Column
    private String exam_id;

    @Id
    @Column
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private String stu_id;



}
