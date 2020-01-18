package org.sicnuafcs.online_exam_platform.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

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
public class Teacher implements Serializable{
    public static enum Sex{
        male,
        female;
    }

    @Id
    @Column(length = 16,nullable = false)
    @NotBlank(message = "tea_id不能为空")
    private String tea_id;

    @Column
    @NotBlank(message = "name不能为空")
    private String name;
    @Column
    private String dept;

    @Column
    @Enumerated(EnumType.STRING)
    private Sex sex=null;

    @Column
    @NotBlank(message = "password不能为空")
    private String password;

    @Column
    private String qq;
    @Column
    private String weixin;

    @Column
    @Email(message = "email格式不正确")
    @NotBlank(message = "email不能为空")
    private String email;

    @Column
    @NotBlank(message = "telephone不能为空")
    private String telephone;

    //邮件码
    /**
     * 状态：0代表未激活，1代表激活
     */
    @Column
    private Integer status;

    /**
     * 生成一段数字，发送到用户邮箱
     */
    @Column
    private String code;

    public Sex getSex(){
        return sex;
    }

    public void setSex(Sex sex){
        this.sex=sex;
    }

}
