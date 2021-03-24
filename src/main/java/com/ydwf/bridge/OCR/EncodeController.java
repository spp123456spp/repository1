package com.ydwf.bridge.OCR;

import com.alibaba.fastjson.JSONObject;
import com.ydwf.bridge.base.CommonApiOfInssue;
import com.ydwf.bridge.requestbean.IDCardRequestBean;
import com.ydwf.bridge.requestbean.IssueForOutApiRequestBean;
import com.ydwf.bridge.requestbean.OCRIDCARD;
import com.ydwf.ocr.util.ConvertFileUtil;
import com.ydwf.ocr.util.RSAUtil;
import com.ydwf.ocr.util.ReflectUtil;
import com.ydwf.ocr.util.SM4Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
public class EncodeController extends CommonApiOfInssue {
    private static String key="D9EA46D637E01A2C4C1C9DDFAB7958ABB9DAF2B01C522FE9";

    @Autowired
    private RestTemplate restTemplate;


    @RequestMapping(value = "encode",method = RequestMethod.POST)
    public IssueForOutApiRequestBean encode(@RequestBody IDCardRequestBean bean) throws Exception{

        /**
         * 中韩分销测试环境公钥。
         */
        String strPublickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/leEThASPjn3kIMCMJTpMjnogJU9KSgY2avxySyTNhMcps7E/Idf9LGOf/RhyYFbLUf8lXHWn3hQkbgdFNu/tvT9G69pgfGrutnnIYWSJwKVKa8lST9IvocLOCC3r6YuzAgcmVfGYI/i7c7hdZ82qMJHT/DhyexRy7+BLGol2pwIDAQAB";

        /**
         * 中韩分销测试环境私钥。
         */
        String strPrivatekey = "MIIBNgIBADANBgkqhkiG9w0BAQEFAASCASAwggEcAgEAAoGBAL+V4ROEBI+OfeQgwIwlOkyOeiAlT0pKBjZq/HJLJM2ExymzsT8h1/0sY5/9GHJgVstR/yVcdafeFCRuB0U27+29P0br2mB8au62echhZInApUpryVJP0i+hws4ILevpi7MCByZV8Zgj+LtzuF1nzaowkdP8OHJ7FHLv4EsaiXanAgEAAoGAbjHH7tCtSuDbeh54gWNcP/JnPhwXmhQvtJcesqKT/X3UjCDl3vkZYW5psvkBWbAd2/5CsfuXVMGAdl+u4Nmzb9z4cdm2kXS5OkAWbzX34Y+JJAtCAY0j7/NO6ilFXbCVtORceEHM08v2kwmeqBU+DUxOo74jAaPFbwlBkKk1dfkCAQACAQACAQACAQACAQA=";

        /**
         * 公钥 -- 渠道
         */
        String strPubkey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOJeNKbX6OL8VFaoCSNPfNLcKHryIaqJ/GXjWvP1YmVbitqxVbrVwEJLCrhLRUO+lCq8UOI71xnx7574F82qpMuRpd9Ok480NqX7Nr3vjbWPFsusBjvTlfmrljmrUFsCueVPmeXgJO4bM/xedew+IzJCHbTaSldWk1Wttlc+BTrQIDAQAB";

        /**
         * 私钥-- 渠道
         */
        String strPrikey = "MIIBNgIBADANBgkqhkiG9w0BAQEFAASCASAwggEcAgEAAoGBAI4l40ptfo4vxUVqgJI0980twoevIhqon8ZeNa8/ViZVuK2rFVutXAQksKuEtFQ76UKrxQ4jvXGfHvnvgXzaqky5Gl306TjzQ2pfs2ve+NtY8Wy6wGO9OV+auWOatQWwK55U+Z5eAk7hsz/F517D4jMkIdtNpKV1aTVa22Vz4FOtAgEAAoGAcHn4VH85YabZEKlKHOZ+ocKwp45jjPaanmsTTZzvPRwdKoYyOz18jua9SKqTygcx9ohWP272Sv6ekyP5sOKRVz1UVD8SxRMTqYCV7UX9OVsG16jkWotZb++HTXlIwhncWC5sQIq6OgC9x1ootEuU//KxkDPukm38DadlkqlyPuECAQACAQACAQACAQACAQA=";


        /**
         * 渠道公钥
         */
        PublicKey publicKey = RSAUtil.getPublicKeyFromX509("RSA", strPubkey);

        /**
         * 渠道私钥
         */
        PrivateKey privateKey = RSAUtil.getPrivateKeyFromPKCS8("RSA", strPrikey);

        /**
         * 中韩人寿分销公钥
         */
        PublicKey ydwfPublickey = RSAUtil.getPublicKeyFromX509("RSA", strPublickey);

        String idcardStr = ConvertFileUtil.convertFileToBase64(bean.idcardStr);
        bean.idcardStr = "";

        String string = JSONObject.toJSONString(bean);
        Map map1 = JSONObject.parseObject(string, Map.class);
        long l3 = System.currentTimeMillis();
        //data base64
        String RequestBodyBase64 = Base64.getEncoder()
                .encodeToString(JSONObject.toJSONString(map1).getBytes(Charset.forName("UTF-8")));
        //RSA公钥加密
        String encriptRequestBody = RSAUtil.encryptString(ydwfPublickey, RequestBodyBase64);

        //私钥加签
        String sign = RSAUtil.signature(encriptRequestBody, privateKey);

        System.out.println("一、加密后数据：" + encriptRequestBody);
        System.out.println("二、签名数据：" + sign);

        long l = System.currentTimeMillis();
        System.out.println((l-l3));

        PublicKey iPublicKey = RSAUtil.getPublicKeyFromX509("RSA", strPubkey);
        boolean signFlag0 =  RSAUtil.signatureVerfiy(encriptRequestBody, sign, publicKey);
        long l2 = System.currentTimeMillis()-l;
        System.out.println(signFlag0);
        System.out.println(l2);
        /**
         * 己方私钥
         */
        PrivateKey ownPrivateKey = RSAUtil.getPrivateKeyFromPKCS8("RSA", strPrivatekey);


        // RSA私钥解密
        //String base64Str = RSAUtil.decryptString(ownPrivateKey, encriptRequestBody);
        // 使用base64 解密 获得数据返回明文
        //String dataStr = new String(Base64.getDecoder().decode(base64Str), "UTF-8");
        //System.out.println("三、解析数据："+dataStr);

        long l1 = System.currentTimeMillis()-l;
        System.out.println(l1/1000);
        IssueForOutApiRequestBean issue = new IssueForOutApiRequestBean();
        issue.DATA=encriptRequestBody;
        issue.SIGN=sign;
        issue.CARDSTR=idcardStr;
        issue.ICODE=bean.ICODE;
        //getIDCard(issue);
        return issue;
    }



    //获取身份证扫描后得到的信息
    private String getIDCard(IssueForOutApiRequestBean idCardRequestBean) throws Exception{
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type","application/json");
        HttpEntity<IssueForOutApiRequestBean> entity = new HttpEntity<IssueForOutApiRequestBean>(idCardRequestBean, requestHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(
                "http://localhost:8080/discriminateIDCardTest",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<String>() {
                }
        );
        String body = exchange.getBody();
        return body;
    }





}
