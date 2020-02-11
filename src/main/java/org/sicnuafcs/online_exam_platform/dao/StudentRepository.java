package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    @Query("select u.stu_id from Student u where u.telephone = ?1")
    String findStu_id_idByPhone(String phone);

    @Query("select u from Student u where u.stu_id = ?1")
    Student findStudentByStu_id(String stu_id);

    @Query("select u from Student u where u.email = ?1")
    Student findStudentByEmail(String email);

    @Query("select u from  Student u where u.telephone = ?1")
    Student findStudentByTelephone(String telephone);
}