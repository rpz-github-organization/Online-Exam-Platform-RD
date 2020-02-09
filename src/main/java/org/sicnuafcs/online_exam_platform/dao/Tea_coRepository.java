package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.Tea_co;
import org.sicnuafcs.online_exam_platform.model.Tea_coPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Tea_coRepository extends JpaRepository<Tea_co, Tea_coPk> {
    @Query("select tea_id from Tea_co where co_id = ?1")
    List<String> getTea_idByCo_id(String co_id);
}