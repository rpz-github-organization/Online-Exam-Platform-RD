package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
    @Query("select u from Teacher u where u.tea_id = ?1")
    Teacher findTeacherByTea_id(String tea_id);

    @Query("select u from Teacher u where u.email = ?1")
    Teacher findTeacherByEmail(String email);

    @Query("select u from Teacher u where u.telephone = ?1")
    Teacher findTeacherByTelephone(String telephone);

    @Query("select name from Teacher where tea_id = ?1")
    String getNameByTea_id(String tea_id);
}