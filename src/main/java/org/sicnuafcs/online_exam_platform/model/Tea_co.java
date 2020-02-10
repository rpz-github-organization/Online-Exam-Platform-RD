package org.sicnuafcs.online_exam_platform.model;

import javax.persistence.*;

@Entity

public class Tea_co {



    String co_id;

    @Id
    @Column
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    String tea_id;



}

