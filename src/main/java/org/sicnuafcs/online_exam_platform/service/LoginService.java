package org.sicnuafcs.online_exam_platform.service;

import org.sicnuafcs.online_exam_platform.model.Login;

public interface LoginService {
    //学号或者工号加密码
    Login LoginId(Login login);

    //手机号加密码
    Login loginPhone(Login login);
}
