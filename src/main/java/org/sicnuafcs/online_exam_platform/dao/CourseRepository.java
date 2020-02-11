package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,String> {
    @Query(value = "SELECT u FROM course u WHERE u.co_id in (:co_idList) ", nativeQuery = true)
    List<Course> findCourseByCo_idIn(List<String> co_idList);
}
