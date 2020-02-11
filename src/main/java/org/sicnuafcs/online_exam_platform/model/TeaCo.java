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
@Table(name = "tea_co")
public class TeaCo {
    @Column
    String co_id;

    @Id
    @Column
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    String tea_id;

}

