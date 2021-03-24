package com.ydwf.bridge.requestbean;

import io.swagger.annotations.ApiModelProperty;

public class BankCardRequestBean {


    @ApiModelProperty(value = "订单号，由合作方上送，每次调用唯一",example = "123456521",required = true)
    public String orderNo;
    @ApiModelProperty(value = "用户的唯一标识（不要带有特殊字符）",example = "31616165165",required = false)
    public String userId;
    @ApiModelProperty(value = "银行卡正面图片的 Base64，大小不超过3MB",required = true)
    public String bankcardStr;
    @ApiModelProperty(value = "版本",example = "1.0.0",required = true)
    public String version;


}
