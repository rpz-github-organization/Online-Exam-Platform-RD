package org.sicnuafcs.online_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "teacher")
public class Teacher implements Serializable {

    public static enum Sex{
        male,
        female;
    }

    @Id
    @Column(length = 16,nullable = false)
    @NotBlank(message = "工号不能为空")
    private String tea_id;

    @Column
    @NotBlank(message = "姓名不能为空")
    private String name;
    @Column
    private String dept;

    @Column
    @Enumerated(EnumType.STRING)
    private Sex sex=null;

    @Column
    @NotBlank(message = "密码不能为空")
    private String password;

    @Column
    private String qq;
    @Column
    private String weixin;

    @Column
    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @Column
    @NotBlank(message = "电话不能为空")
    private String telephone;

    @Column
    private String code;
    @Column
    private int authority;

    public Sex getSex(){
        return sex;
    }

    public void setSex(Sex sex){
        this.sex = sex;
    }

}
