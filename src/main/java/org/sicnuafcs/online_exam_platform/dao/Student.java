package org.sicnuafcs.online_exam_platform.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "student")
public class Student {
    public static enum Sex{
        male,
        female;
    }

    @Id
    @Column(length = 16)
    private String stu_id;

    @Column(length = 32)
    private String name;
    @Column(length = 8)
    private String class_id;
    @Column(length = 8)
    private String major_id;
    @Column
    private Integer grade;
    @Column
    private String institute_id;

    @Column
    @Enumerated(EnumType.STRING)
    private Sex sex=Sex.male;

    @Column
    private String password;
    @Column
    private String qq;
    @Column
    private String weixin;
    @Column
    private String email;

    public Sex getSex(){
        return sex;
    }

    public void setSex(Sex sex){
        this.sex=sex;
    }
}
