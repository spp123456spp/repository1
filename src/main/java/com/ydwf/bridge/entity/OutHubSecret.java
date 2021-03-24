package com.ydwf.bridge.entity;
/***************
*此文件由代码生成器自动生成，
*开发人员请务在此文件中修改代码，以防被代码生成器自动删除
******************/

import com.ydwf.component.Column;
import com.ydwf.component.PKId;
import com.ydwf.component.PersistantEntity;
import com.ydwf.component.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 对外接入密钥配置表
 *
 * @author : huangshuanbao
 * @date : 2020/9/2 8:44 下午
 *
 */
@Table(name = "TBOUTHUBSECRET")
@ApiModel(value="OutHubSecret对象", description="对外接入密钥配置表")
public class OutHubSecret extends PersistantEntity  implements Serializable {

    /**
    * 接入CODE
    */
    @ApiModelProperty(value = "HUBCODE",required = true,example = "HUBCODE")
    @PKId
    @Column(name = "HUBCODE")
    public String HUBCODE;


    /**
    * 接口渠道码
    */
    @ApiModelProperty(value = "接口渠道码",required = true,example = "接口渠道码")
    @Column(name = "ICODE")
    public String ICODE;

    /**
     * 接口渠道描述
     */
    @ApiModelProperty(value = "接口渠道描述",required = true,example = "接口渠道描述")
    @Column(name = "ICODEDESC")
    public String ICODEDESC;

    /**
     * 己方RSA公钥
     */
    @ApiModelProperty(value = "己方RSA公钥",required = true,example = "己方RSA公钥")
    @Column(name = "OWNPUBLIC")
    public String OWNPUBLIC;

    /**
     * 己方RSA私钥
     */
    @ApiModelProperty(value = "己方RSA私钥",required = true,example = "己方RSA私钥")
    @Column(name = "OWNPRIVITE")
    public String OWNPRIVITE;

    /**
     * 渠道方RSA公钥
     */
    @ApiModelProperty(value = "渠道方RSA公钥",required = true,example = "渠道方RSA公钥")
    @Column(name = "IPUBLIC")
    public String IPUBLIC;

    /**
     * 渠道方RSA私钥
     */
    @ApiModelProperty(value = "渠道方RSA私钥",required = true,example = "渠道方RSA私钥")
    @Column(name = "IPRIVATE")
    public String IPRIVATE;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态：1-启用 0-禁用",required = true,example = "状态：1-启用 0-禁用")
    @Column(name = "STATUS")
    public Integer STATUS;

    /**
     * 接口所属接入类型 APIOFISSUE-api出单接入
     */
    @ApiModelProperty(value = "接口所属接入类型",required = true,example = "接口所属接入类型")
    @Column(name = "INTERFACETYPE")
    public String INTERFACETYPE;

    @Override
    public String toString() {
        return "OutHubSecret{" +
                "HUBCODE='" + HUBCODE + '\'' +
                ", ICODE='" + ICODE + '\'' +
                ", ICODEDESC='" + ICODEDESC + '\'' +
                ", OWNPUBLIC='" + OWNPUBLIC + '\'' +
                ", OWNPRIVITE='" + OWNPRIVITE + '\'' +
                ", IPUBLIC='" + IPUBLIC + '\'' +
                ", IPRIVATE='" + IPRIVATE + '\'' +
                ", STATUS=" + STATUS +
                ", INTERFACETYPE='" + INTERFACETYPE + '\'' +
                '}';
    }

}
