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
@IdClass(TeaCoPK.class)
@Table(name = "tea_co")
public class TeaCo implements Serializable{
    @Id
    @Column
    private String co_id;

    @Id
    @Column
    private String tea_id;
}

