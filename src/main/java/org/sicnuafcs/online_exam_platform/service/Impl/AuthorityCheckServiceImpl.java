package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.service.AuthorityCheckService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AuthorityCheckServiceImpl implements AuthorityCheckService {

    @Override
    public void checkStudentAuthority(Object user) {
        Map userInfo = (Map)user;
        int authority = Integer.parseInt(userInfo.get("authority").toString());
        if (authority != 0) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该用户无权限");
        }
    }

    @Override
    public void checkTeacherAuthority(Object user) {
        Map userInfo = (Map)user;
        int authority = Integer.parseInt(userInfo.get("authority").toString());
        if (authority != 1) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该用户无权限");
        }
    }

    @Override
    public void checkLoginStatus(Object user) {
        Map userInfo = (Map)user;
        int authority = Integer.parseInt(userInfo.get("authority").toString());
        if (authority != 1 && authority != 0) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该用户无权限");
        }
    }
}
