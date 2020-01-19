package org.sicnuafcs.online_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.imageio.spi.ImageInputStreamSpi;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    @Column(length = 16,nullable = false)
    @NotBlank(message = "stu_id不能为空")
    private String stu_id;

    @Column(length = 32,nullable = false)
    @NotBlank(message = "name不能为空")
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


    public Sex getSex(){
        return sex;
    }

    public void setSex(Sex sex){
        this.sex=sex;
    }
}
