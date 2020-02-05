package org.sicnuafcs.online_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "course")
public class Course {
    @Id
    @Column(length = 16,nullable = false)
    private String co_id;

    @Column
    private String name;

    @Column
    private String credit;//学分

    @Column
    private String school_hour;//学时

    @Column
    private String exam_score;

    @Column
    private String common_socre;

    @Column
    private String exam_proportion;//卷面比例

    @Column
    private Timestamp end_time;

    @Column
    private Timestamp begin_time;

    @Column
    private ArrayList major_list;

//    public String getCo_id() {
//        return co_id;
//    }
//
//    public void setCo_id(String co_id) {
//        this.co_id = co_id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getCredit() {
//        return credit;
//    }
//
//    public void setCredit(String credit) {
//        this.credit = credit;
//    }
//
//    public String getSchool_hour() {
//        return school_hour;
//    }
//
//    public void setSchool_hour(String school_hour) {
//        this.school_hour = school_hour;
//    }
//
//    public String getExam_score() {
//        return exam_score;
//    }
//
//    public void setExam_score(String exam_score) {
//        this.exam_score = exam_score;
//    }
//
//    public String getCommon_score() {
//        return common_socre;
//    }
//
//    public void setCommon_score(String common_socre) {
//        this.common_socre = common_socre;
//    }
//
//    public String getExam_proportion() {
//        return exam_proportion;
//    }
//
//    public void setExam_proportion(String exam_proportion) {
//        this.exam_proportion = exam_proportion;
//    }
//
//    public Timestamp getEnd_time() {
//        return end_time;
//    }
//
//    public void setEnd_time(Timestamp end_time) {
//        this.end_time = end_time;
//    }
//
//    public Timestamp getBegin_time() {
//        return begin_time;
//    }
//
//    public void setBegin_time(Timestamp begin_time) {
//        this.begin_time = begin_time;
//    }
}
