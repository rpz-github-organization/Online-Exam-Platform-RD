package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.Stu_co;
import org.sicnuafcs.online_exam_platform.model.Stu_coPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Stu_coRepository extends JpaRepository<Stu_co, Stu_coPk> {

    @Query("select u from Stu_co u where u.stu_id = ?1")
    List<Stu_co> getStu_coByStu_id(String stu_id);
}