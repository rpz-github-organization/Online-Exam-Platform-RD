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
@Table(name = "teacher")
public class Teacher {
    public static enum Sex{
        male,
        female;
    }

    @Id
    @Column(length = 16,nullable = false)
    private String tea_id;

    @Column
    private String name;
    @Column
    private String dept;

    @Column
    @Enumerated(EnumType.STRING)
    private Sex sex=null;

    @Column
    private String password;
    @Column
    private String qq;
    @Column
    private String weixin;
    @Column
    private String email;
    @Column
    private String telephone;

    public Sex getSex(){
        return sex;
    }

    public void setSex(Sex sex){
        this.sex=sex;
    }
}
