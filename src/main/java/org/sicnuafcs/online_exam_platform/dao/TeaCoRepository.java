package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.TeaCo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface TeaCoRepository extends JpaRepository<TeaCo,String> {

    @Query(value = "SELECT co_id FROM tea_co WHERE tea_id = ?", nativeQuery = true)
    List<String> findCo_idByTea_Id(String tea_id);

}