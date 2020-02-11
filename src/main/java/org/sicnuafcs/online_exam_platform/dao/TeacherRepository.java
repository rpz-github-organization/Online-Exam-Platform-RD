package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
    Optional<Teacher> findByEmail(String email);
    Optional<Teacher> findByTelephone(String telephone);

    @Query(value = "SELECT tea_id FROM teacher WHERE telephone = ?", nativeQuery = true)
    String findTea_idByPhone(String Phone);




}