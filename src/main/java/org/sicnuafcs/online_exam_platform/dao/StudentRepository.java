package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findByEmail(String email);
    Optional<Student> findByTelephone(String telephone);

    @Query(value = "SELECT  stu_id FROM student WHERE telephone = ?", nativeQuery = true)
    String findStu_idByPhone(String phone);
}