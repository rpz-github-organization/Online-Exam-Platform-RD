package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Repository
public interface TeatCaseRepository extends JpaRepository<TestCase, Long> {
    @Query("select u from TestCase u where u.question_id = ?1")
    TestCase getOneByQuestion_id(Long question_id);

}
