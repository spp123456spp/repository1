package com.ydwf.bridge.requestbean;

import io.swagger.annotations.ApiModelProperty;

public class OCRIDCARD  {

    @ApiModelProperty(value = "业务流程唯一标识，即 wbappid，可参考 获取 WBappid 指引在人脸核身控制台内申请",example = "id",required = true)
    public String webankAppId;
    @ApiModelProperty(value = "接口版本号",example = "1.0.0",required = true)
    public String version;
    @ApiModelProperty(value = "随机数32 位随机串（字母 + 数字组成的随机数）",example = "AASHKASHKABDSJFVJFAJAAF",required = true)
    public String nonce;
    @ApiModelProperty(value = "签名：使用 生成的签名",example = "id",required = true)
    public String sign;
    @ApiModelProperty(value = "订单号，由合作方上送，每次调用唯一",example = "id",required = true)
    public String orderNo;
    @ApiModelProperty(value = "用户的唯一标识（不要带有特殊字符）",example = "",required = false)
    public String userId;
    @ApiModelProperty(value = "身份证正反面标识  0：人像面  1：国徽面",example = "0",required = true)
    public String cardType;
    @ApiModelProperty(value = "身份证人像面或者国徽面图片的 Base64，大小不超过20MB",example = "idcardStr",required = true)
    public String idcardStr;



}
