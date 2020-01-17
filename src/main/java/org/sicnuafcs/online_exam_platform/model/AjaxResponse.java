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
    private boolean isexist;
    private boolean isok;
    private int code;
    private String message;
    private Object data;

    private AjaxResponse() {
        this.isexist=false;
        this.isok=false;
    }

    public static AjaxResponse success() {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(true);
        resultBean.setIsexist(false);
        resultBean.setCode(200);
        resultBean.setMessage("成功");
        return resultBean;
    }

    /*
    便于调试 不实际使用
     */
    public static AjaxResponse success(Object data) {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(true);
        resultBean.setIsexist(false);
        resultBean.setCode(200);
        resultBean.setMessage("成功");
        resultBean.setData(data);
        return resultBean;
    }


    //注册失败时返回
    public static AjaxResponse hasexist(){
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(false);
        resultBean.setIsexist(true);
        resultBean.setCode(400);
        resultBean.setMessage("用户已存在");
        return resultBean;
    }

    //登录失败时返回
    public static AjaxResponse passworderror(Object data) {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(false);
        resultBean.setIsexist(true);
        resultBean.setCode(400);
        resultBean.setMessage("密码错误");
        return resultBean;
    }

    //登录失败时返回
    public static AjaxResponse nouser() {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(false);
        resultBean.setIsexist(false);
        resultBean.setCode(400);
        resultBean.setMessage("用户不存在");
        return resultBean;
    }
}