package org.sicnuafcs.online_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@IdClass(MajorInstitudePK.class)
@Table(name = "major_institude")
public class MajorInstitude implements Serializable{
    @Id
    @Column(length = 32,nullable = false)
    private int grade;

    @Id
    @Column(length = 32,nullable = false)
    private String institude_id;

    @Id
    @Column(length = 32,nullable = false)
    private String class_id;

    @Column(length = 32,nullable = false)
    private String major_id;
}
