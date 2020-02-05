package org.sicnuafcs.online_exam_platform.model;

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
@IdClass(Tea_coPk.class)
@Table(name = "tea_co")
public class Tea_co {
    @Id
    @Column
    private String co_id;

    @Id
    @Column
    private String tea_id;
}
