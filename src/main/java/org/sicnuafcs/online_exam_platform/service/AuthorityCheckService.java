package org.sicnuafcs.online_exam_platform.service;

import java.util.Map;

public interface AuthorityCheckService {
    void checkStudentAuthority(Object user);
    void checkTeacherAuthority(Object user);
}
