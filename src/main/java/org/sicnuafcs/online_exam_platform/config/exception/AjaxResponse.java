package org.sicnuafcs.online_exam_platform.config.exception;

import lombok.Data;

/*
统一响应前端的数据结构
 */

@Data
public class AjaxResponse {

    private boolean isok;    // ajax请求是否成功
    private int code;        // http status code
    private String message;  //请求失败的的提示信息。
    private Object data;     //请求成功时，需要响应给前端的数据

    private AjaxResponse() {

    }
    //请求出现异常时的响应数据封装
    public static AjaxResponse error(CustomException e) {

        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(false);
        resultBean.setCode(e.getCode());
        if(e.getCode() == CustomExceptionType.USER_INPUT_ERROR.getCode()){      //400异常
            resultBean.setMessage(e.getMessage());
        }else if(e.getCode() == CustomExceptionType.SYSTEM_ERROR.getCode()){    //500异常
            resultBean.setMessage(e.getMessage() + ",系统出现异常");
        }else{                                                                  //999异常
            resultBean.setMessage("系统出现未知异常");
        }
        return resultBean;
    }

    //请求成功时的响应数据封装，没有响应数据（比如删除修改成功）
    public static AjaxResponse success() {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(true);
        resultBean.setCode(200);
        resultBean.setMessage("操作成功");
        return resultBean;
    }

    //请求成功时的响应数据封装，有响应数据（比如查询成功）
    public static AjaxResponse success(Object data) {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(true);
        resultBean.setCode(200);
        resultBean.setMessage("操作成功");
        resultBean.setData(data);
        return resultBean;
    }
}