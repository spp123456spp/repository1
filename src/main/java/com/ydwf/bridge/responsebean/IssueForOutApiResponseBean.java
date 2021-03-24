package com.ydwf.bridge.responsebean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对外出单接口统一响应报文体
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/9/2 7:32 下午
 */
@ApiModel(value = "对外出单接口统一响应报文体", description = "对外出单接口统一响应报文体")
public class IssueForOutApiResponseBean {

    @ApiModelProperty(value = "接口调用状态", required=true, example="true")
    public Boolean FLAG;

    @ApiModelProperty(value = "接口调用结果信息", required=true, example="接口调用成功")
    public String MESSAGE;

    @ApiModelProperty(value = "加密密文", required=true, example = "加密密文")
    public String DATA;

    @ApiModelProperty(value = "签名串", required=true, example = "签名串")
    public String SIGN;

    @Override
    public String toString() {
        return "IssueForOutApiResponseBean{" +
                "FLAG=" + FLAG +
                ", MESSAGE='" + MESSAGE + '\'' +
                ", DATA='" + DATA + '\'' +
                ", SIGN='" + SIGN + '\'' +
                '}';
    }

}
