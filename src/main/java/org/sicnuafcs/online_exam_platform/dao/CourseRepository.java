package org.sicnuafcs.online_exam_platform.dao;

import org.sicnuafcs.online_exam_platform.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,String> {
    @Query("select u from Course u where u.co_id in (:coIdList)")
    List<Course> findCourseByCo_idIn(List<String> coIdList);

    @Query("select name from Course where co_id = ?1")
    String getNameByCo_id(String co_id);

    @Query("select u from Course u where u.co_id = ?1")
    Course findCourseByCo_id(String co_id);

    @Query("select begin_time from Course where co_id = ?1")
    Timestamp findBeginTimeByCo_id(String co_id);

    @Query("select end_time from Course where co_id = ?1")
    Timestamp findEndTimeByCo_id(String co_id);

}
