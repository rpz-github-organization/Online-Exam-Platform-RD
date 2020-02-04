package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher,String> , JpaSpecificationExecutor<Teacher> {
    Optional<Teacher> findByEmail(String email);
    Optional<Teacher> findByTelephone(String telephone);
}