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

    @Query("select count(u) from StuCo u where u.co_id = ?1")
    Long findStuNumByCo_id(String co_id);

    @Query("select u.tea_id from StuCo u where u.stu_id = ?1 and u.co_id = ?2")
    String getTeaIdByStu_idAndAndCo_id(String stu_id, String co_id);

    @Query("select u from StuCo u where u.co_id = ?1 and u.tea_id = ?2 and u.stu_id = ?3")
    StuCo getOneById(String co_id, String tea_id, String stu_id);
}