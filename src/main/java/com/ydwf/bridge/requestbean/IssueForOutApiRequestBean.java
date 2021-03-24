package com.ydwf.bridge.requestbean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对外出单接口统一请求报文体
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/9/2 7:32 下午
 */
@ApiModel(value = "对外出单接口统一请求报文体", description = "对外出单接口统一请求报文体")
public class IssueForOutApiRequestBean {

    @ApiModelProperty(value = "接口渠道码", required = true, example = "接口渠道码")
    public String ICODE;

    @ApiModelProperty(value = "加密密文", required = true, example = "加密密文")
    public String DATA;

    @ApiModelProperty(value = "签名串", required = true, example = "签名串")
    public String SIGN;

    @ApiModelProperty(value = "图片信息", required = true, example = "签名串")
    public String CARDSTR;

    @Override
    public String toString() {
        return "IssueForOutApiRequestBean{" +
                "ICODE='" + ICODE + '\'' +
                ", DATA='" + DATA + '\'' +
                ", SIGN='" + SIGN + '\'' +
                '}';
    }

}
