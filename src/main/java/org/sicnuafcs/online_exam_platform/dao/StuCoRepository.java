package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.StuCo;
import org.sicnuafcs.online_exam_platform.model.StuCoPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StuCoRepository extends JpaRepository<StuCo, StuCoPK> {

    @Query("select u from StuCo u where u.stu_id = ?1")
    List<StuCo> getStu_coByStu_id(String stu_id);

    @Query("select u.stu_id from StuCo u where u.co_id = ?1")
    List<String> findByCo_id(String co_id);
}