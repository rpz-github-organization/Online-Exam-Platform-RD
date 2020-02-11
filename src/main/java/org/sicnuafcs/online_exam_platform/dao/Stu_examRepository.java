package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.Stu_exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface Stu_examRepository extends JpaRepository<Stu_exam,String> {

    @Query(value = "SELECT exam_id FROM stu_exam WHERE stu_id = ?", nativeQuery = true)
    List<String> findExam_idBySea_Id(String Stu_id);

}
