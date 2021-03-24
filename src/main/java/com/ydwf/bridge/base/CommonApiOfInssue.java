package com.ydwf.bridge.base;

import com.alibaba.fastjson.JSONObject;
import com.ydwf.bridge.entity.OutHubSecret;
import com.ydwf.bridge.projectbaseserver;
import com.ydwf.bridge.requestbean.IssueForOutApiRequestBean;
import com.ydwf.bridge.responsebean.IssueForOutApiResponseBean;
import com.ydwf.cloud.framework.DataFormat;
import com.ydwf.cloud.framework.DataMap;
import com.ydwf.component.ServerContext;
import com.ydwf.component.ServerUtils;
import com.ydwf.ocr.util.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public abstract class CommonApiOfInssue extends projectbaseserver {

    private Logger logger = LoggerFactory.getLogger(CommonApiOfInssue.class);

    protected OutHubSecret outInterfaceSecret;

    /**
     * 获取对外接口密钥配置
     *
     * @param ICODE 接口渠道码
     * @param INTERFACETYPE 接口类型
     * @return OutHubSecret
     */
    public void getOutInterfaceSecret(ServerContext context, String ICODE, String INTERFACETYPE) throws Exception {
        OutHubSecret outInterfaceSecret = new OutHubSecret();
        outInterfaceSecret.ICODE = ICODE;
        outInterfaceSecret.INTERFACETYPE = INTERFACETYPE;
        outInterfaceSecret = outInterfaceSecret.getObjectByPeroperty(context);
        this.outInterfaceSecret = outInterfaceSecret;
        logger.debug("对外出单接口渠道方调用时查询到密钥配置为：{}", this.outInterfaceSecret);
    }

    protected String checkAndVerifyRequest(ServerContext context, IssueForOutApiRequestBean requestBean, String interfaceType) throws Exception {

        if(requestBean.ICODE == null || "".equals(requestBean.ICODE)){
            throw new IssueOutException("接口渠道码不能为空");
        }

        if(requestBean.DATA == null || "".equals(requestBean.DATA)){
            throw new IssueOutException("加密密文不能为空");
        }

        if(requestBean.SIGN == null || "".equals(requestBean.SIGN)){
            throw new IssueOutException("签名串不能为空");
        }

        // 获取接口调用密钥配置
        getOutInterfaceSecret(context, requestBean.ICODE, interfaceType);
        if(this.outInterfaceSecret==null){
            throw new IssueOutException("接口渠道编码异常");
        }
        if(this.outInterfaceSecret.STATUS==null || this.outInterfaceSecret.STATUS!=1){
            throw new IssueOutException("无效的接口渠道接入");
        }

        // 验签并解密请求报文
        //公钥验签
        /**
         * 渠道公钥
         */
        PublicKey iPublicKey = RSAUtil.getPublicKeyFromX509("RSA", outInterfaceSecret.IPUBLIC);
        /**
         * 己方私钥
         */
        PrivateKey ownPrivateKey = RSAUtil.getPrivateKeyFromPKCS8("RSA", outInterfaceSecret.OWNPRIVITE);

        boolean signFlag =  RSAUtil.signatureVerfiy(requestBean.DATA, requestBean.SIGN, iPublicKey);
        if(signFlag) {
            // RSA私钥解密
            String base64Str = RSAUtil.decryptString(ownPrivateKey, requestBean.DATA);
            // 使用base64 解密 获得数据返回明文
            String dataStr = new String(Base64.getDecoder().decode(base64Str), "UTF-8");
            return dataStr;
        }else {
            throw new IssueOutException("验签失败");
        }

    }

    protected HashMap encryptForResponse(Map resultMap) throws Exception {
        //data base64
        String RequestBodyBase64 = Base64.getEncoder()
                .encodeToString(JSONObject.toJSONString(resultMap).getBytes(Charset.forName("UTF-8")));
        // 渠道方公钥
        PublicKey iPublickey = RSAUtil.getPublicKeyFromX509("RSA", outInterfaceSecret.IPUBLIC);
        // RSA公钥加密
        String encryptRequestBody = RSAUtil.encryptString(iPublickey, RequestBodyBase64);
        // 己方私钥
        PrivateKey ownPrivateKey = RSAUtil.getPrivateKeyFromPKCS8("RSA", outInterfaceSecret.OWNPRIVITE);
        // RSA私钥加签
        String sign = RSAUtil.signature(encryptRequestBody, ownPrivateKey);
        Map map = new HashMap();
        map.put("ICODE", outInterfaceSecret.ICODE);
        map.put("DATA", encryptRequestBody);
        map.put("SIGN", sign);
        return new DataMap().setFlag(true).setMessage("接口响应成功").setAll(map).setDataformat(DataFormat.JSON);
    }

    protected Map encryptForRequest(String req) throws Exception {
        //data base64
        String RequestBodyBase64 = Base64.getEncoder()
                .encodeToString(req.getBytes(Charset.forName("UTF-8")));
        // 渠道方公钥
        PublicKey iPublickey = RSAUtil.getPublicKeyFromX509("RSA", outInterfaceSecret.IPUBLIC);
        // RSA公钥加密
        String encryptRequestBody = RSAUtil.encryptString(iPublickey, RequestBodyBase64);
        // 己方私钥
        PrivateKey ownPrivateKey = RSAUtil.getPrivateKeyFromPKCS8("RSA", outInterfaceSecret.OWNPRIVITE);
        // RSA私钥加签
        String sign = RSAUtil.signature(encryptRequestBody, ownPrivateKey);
        Map map = new HashMap();
        map.put("ICODE", outInterfaceSecret.ICODE);
        map.put("DATA", encryptRequestBody);
        map.put("SIGN", sign);
        return map;
    }

    protected String checkAndVerifyResponse(IssueForOutApiResponseBean responseBean) throws Exception {

        if(responseBean.DATA == null || "".equals(responseBean.DATA)){
            throw new IssueOutException("加密密文不能为空");
        }

        if(responseBean.SIGN == null || "".equals(responseBean.SIGN)){
            throw new IssueOutException("签名串不能为空");
        }

        if(this.outInterfaceSecret==null){
            throw new IssueOutException("接口渠道编码异常");
        }

        // 验签并解密请求报文
        //公钥验签
        /**
         * 渠道公钥
         */
        PublicKey iPublicKey = RSAUtil.getPublicKeyFromX509("RSA", outInterfaceSecret.IPUBLIC);
        /**
         * 己方私钥
         */
        PrivateKey ownPrivateKey = RSAUtil.getPrivateKeyFromPKCS8("RSA", outInterfaceSecret.OWNPRIVITE);

        boolean signFlag =  RSAUtil.signatureVerfiy(responseBean.DATA, responseBean.SIGN, iPublicKey);
        if(signFlag) {
            // RSA私钥解密
            String base64Str = RSAUtil.decryptString(ownPrivateKey, responseBean.DATA);
            // 使用base64 解密 获得数据返回明文
            String dataStr = new String(Base64.getDecoder().decode(base64Str), "UTF-8");
            return dataStr;
        }else {
            throw new IssueOutException("验签失败");
        }

    }

    protected Map commonApiLogin(ServerContext context, String icode, String cpsId) throws Exception {
        // cps信息查询后经办人登录 放入access_token
        Map loginParam = new HashMap();
        loginParam.put("CPSID", cpsId);
        loginParam.put("ICODE", icode);
        return ServerUtils.callServer(context, "AMS-ChannelApiIssueAuthLogin", loginParam);
    }

    //protected abstract void check(ChannelApissueRequestBean dataBean) throws Exception;

}
