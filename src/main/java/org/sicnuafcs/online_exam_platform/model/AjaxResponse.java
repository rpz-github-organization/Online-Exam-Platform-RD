package org.sicnuafcs.online_exam_platform.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
@ApiModel
public class AjaxResponse {

    @ApiModelProperty("是否请求成功")
    private boolean isempty;
    private boolean isok;
    private int code;
    private String message;
    private Object data;

    private AjaxResponse() {
        isempty=true;
        isok=false;
    }

    public static AjaxResponse success() {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(true);
        resultBean.setIsempty(true);
        resultBean.setCode(200);
        resultBean.setMessage("成功");
        return resultBean;
    }

    public static AjaxResponse success(Object data) {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(true);
        resultBean.setIsempty(true);
        resultBean.setCode(200);
        resultBean.setMessage("成功");
        resultBean.setData(data);
        return resultBean;
    }

    public static AjaxResponse isfalse() {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(false);
        resultBean.setIsempty(true);
        resultBean.setCode(400);
        resultBean.setMessage("用户不存在");
        return resultBean;
    }

    public static AjaxResponse isfalse(Object data) {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(false);
        resultBean.setIsempty(false);
        resultBean.setCode(400);
        resultBean.setMessage("密码错误");
        resultBean.setData(data);
        return resultBean;
    }

    public static AjaxResponse isEmpty(){
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(false);
        resultBean.setIsempty(false);
        resultBean.setCode(400);
        resultBean.setMessage("用户已存在");
        return resultBean;
    }

    public static AjaxResponse isEmpty(Object data) {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(false);
        resultBean.setIsempty(false);
        resultBean.setCode(400);
        resultBean.setMessage("用户已存在");
        resultBean.setData(data);
        return resultBean;
    }
}