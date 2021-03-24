package com.ydwf.bridge.requestbean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class IDCardRequestBean {

    @ApiModelProperty(value = "订单号，由合作方上送，每次调用唯一",example = "123456521",required = true)
    public String orderNo;
    @ApiModelProperty(value = "用户的唯一标识（不要带有特殊字符）",example = "31616165165",required = false)
    public String userId;
    @ApiModelProperty(value = "身份证正反面标识 0：人像面 1：国徽面",example = "1",required = true)
    public String cardType;
    @ApiModelProperty(value = "身份证人像面或者国徽面图片的 Base64，大小不超过20MB",required = true)
    public String idcardStr;
    @ApiModelProperty(value = "版本",example = "1.0.0",required = true)
    public String version;
    @ApiModelProperty(value = "公司名称",example = "中韩保险",required = true)
    public String ICODE;

    @Override
    public String toString() {
        return "IDCardRequestBean{" +
                "orderNo='" + orderNo + '\'' +
                ", userId='" + userId + '\'' +
                ", cardType='" + cardType + '\'' +
                ", idcardStr='" + idcardStr + '\'' +
                ", version='" + version + '\'' +
                ", ICODE='" + ICODE + '\'' +
                '}';
    }
}
