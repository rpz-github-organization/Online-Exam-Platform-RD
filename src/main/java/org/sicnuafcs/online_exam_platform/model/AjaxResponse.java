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
        resultBean.setMessage("success");
        return resultBean;
    }

    public static AjaxResponse success(Object data) {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(true);
        resultBean.setIsempty(true);
        resultBean.setCode(200);
        resultBean.setMessage("success");
        resultBean.setData(data);
        return resultBean;
    }

    public static AjaxResponse isEmpty(){
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(false);
        resultBean.setIsempty(false);
        resultBean.setCode(400);
        resultBean.setMessage("false");
        return resultBean;
    }

    public static AjaxResponse isEmpty(Object data) {
        AjaxResponse resultBean = new AjaxResponse();
        resultBean.setIsok(false);
        resultBean.setIsempty(false);
        resultBean.setCode(400);
        resultBean.setMessage("false");
        resultBean.setData(data);
        return resultBean;
    }
}