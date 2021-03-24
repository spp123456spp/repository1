package com.ydwf.bridge.OCR;

import com.alibaba.fastjson.JSONObject;
import com.ydwf.bridge.base.CommonApiOfInssue;
import com.ydwf.bridge.base.IssueOutException;
import com.ydwf.bridge.requestbean.BankCardRequestBean;
import com.ydwf.bridge.requestbean.IDCardRequestBean;
import com.ydwf.bridge.requestbean.IssueForOutApiRequestBean;
import com.ydwf.bridge.requestbean.OCRBANKCARD;
import com.ydwf.cloud.framework.DataMap;
import com.ydwf.component.LoginUserInfo;
import com.ydwf.component.ServerContext;
import com.ydwf.ocr.util.IDUtil;
import com.ydwf.ocr.util.PropertyUtil;
import com.ydwf.ocr.util.RSAUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Service("discriminate-BankCard-Test")
public class BankCardTest extends CommonApiOfInssue {

    @Resource
    private RestTemplate restTemplate;

    public static String key="D9EA46D637E01A2C4C1C9DDFAB7958ABB9DAF2B01C522FE9";
    private static String webankAppId= "";
    private static String secret="";
    private static String tokenUrl="";
    private static String ticketUrl="";
    private static String bankcardUrl="";

    static {
        webankAppId= PropertyUtil.getProperty("webankAppId", "parameter");
        secret=PropertyUtil.getProperty("secret", "parameter");
        tokenUrl= PropertyUtil.getProperty("tokenUrl", "parameter");
        ticketUrl=PropertyUtil.getProperty("ticketUrl", "parameter");
        bankcardUrl= PropertyUtil.getProperty("bankcardUrl", "parameter");
    }

    @RequestMapping(value = "/discriminateBankCardTest",method = RequestMethod.POST)
    public Map discriminateBankCard(@ApiIgnore ServerContext context,/*上下文不包含在API参数定义中*/
                                    @RequestBody IssueForOutApiRequestBean requestBean,
                                    @ApiIgnore LoginUserInfo loginUserInfo /*上下文不包含在API参数定义中*/) throws Exception{

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


        PublicKey publicKey = RSAUtil.getPublicKeyFromX509("RSA", strPubkey);
        PrivateKey privateKey = RSAUtil.getPrivateKeyFromPKCS8("RSA", strPrikey);
        PublicKey ydwfPublickey = RSAUtil.getPublicKeyFromX509("RSA", strPublickey);
        PrivateKey ydwfPrivateKey = RSAUtil.getPrivateKeyFromPKCS8("RSA", strPrivatekey);


        String requestStr="";
        try{
            // RSA私钥解密
            String base64Str = RSAUtil.decryptString(ydwfPrivateKey, requestBean.DATA);
            // 使用base64 解密 获得数据返回明文
            requestStr = new String(Base64.getDecoder().decode(base64Str), "UTF-8");
            //System.out.println("三、解析数据："+dataStr);
            //requestStr = this.checkAndVerifyRequest(context, requestBean, "H5PageOFISSUE");
        }catch (IssueOutException ex){
            return DataMap.build().setFlag(false).setMessage(context.collectExceptionStack(ex));
        }
        BankCardRequestBean bean = JSONObject.parseObject(requestStr, BankCardRequestBean.class);
        System.out.println(bean);
        OCRBANKCARD ocrbankcard = new OCRBANKCARD();
        ocrbankcard.webankAppId=webankAppId;
        ocrbankcard.bankcardStr=requestBean.CARDSTR;
        ocrbankcard.nonce= IDUtil.getItemID(32);
        ocrbankcard.userId=bean.userId;
        ocrbankcard.version=bean.version;
        ocrbankcard.orderNo=bean.orderNo;
        String ticket = getTicket(getToken(bean.version),bean.version);
        String sign = getSign(bean.version, ticket, bean.orderNo, webankAppId, ocrbankcard.nonce);
        ocrbankcard.sign=sign;
        /*HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(60*20);*/
        String idCardMap = getBankCard(ocrbankcard);
        JSONObject jsonObject = JSONObject.parseObject(idCardMap);
        Map<String, Object> idCard = jsonObject.getInnerMap();
        return idCard;
    }


    //获取token接口
    private String getToken(String version) throws Exception{
        Map<String, String>  map= new HashMap<>();
        //业务流程唯一标识，即 wbappid，可参考 获取 WBappid 指引在人脸核身控制台内申请
        map.put("app_id",webankAppId);
        //wbappid 对应的密钥，申请 wbappid 时得到，可参考 获取 WBappid 指引在人脸核身控制台内申请
        map.put("secret",secret);
        //授权类型，默认值为：client_credential（必须小写）
        map.put("grant_type","client_credential");
        //版本号，默认值为：1.0.0
        map.put("version",version);
        String msg = getMsg(tokenUrl, map);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        Map<String, Object> innerMap = jsonObject.getInnerMap();
        String access_token = "";
        if((Boolean) innerMap.get("success")){
            access_token = innerMap.get("access_token").toString();
        }
        return access_token;
    }


    //获取ticket参数，用来生成sign签名
    private String getTicket(String access_token,String version) throws Exception{
        Map<String, String>  map= new HashMap<>();
        //业务流程唯一标识，即 wbappid，可参考 获取 WBappid 指引在人脸核身控制台内申请
        map.put("app_id",webankAppId);
        //请根据 获取 Access Token 指引进行获取
        map.put("access_token",access_token);
        //授权类型，默认值为：SIGN（必须小写）
        map.put("type","SIGN");
        //版本号，默认值为：1.0.0
        map.put("version",version);
        String msg = getMsg(ticketUrl, map);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        Map<String, Object> innerMap = jsonObject.getInnerMap();
        List list = (List) innerMap.get("tickets");
        Map map1 = (Map) list.get(0);
        String value = map1.get("value").toString();
        return value;
    }

    //获取签名
    private String getSign(String version,String ticket,String orderNo,String webankAppId,String nonce ) throws Exception{
        String s = version + ticket + orderNo + webankAppId + nonce;
        String sign = DigestUtils.shaHex(s);
        return sign;
    }

    //获取银行卡扫描后得到的信息
    private String getBankCard(OCRBANKCARD idCardRequestBean) throws Exception{
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type","application/json");
        HttpEntity<OCRBANKCARD> entity = new HttpEntity<OCRBANKCARD>(idCardRequestBean, requestHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(
                bankcardUrl,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<String>() {
                }
        );
        String body = exchange.getBody();
        return body;
    }


    //get方法公用接口调用
    private String getMsg(String url,Map<String,String> map) throws Exception{
        if(map.size()>0){
            url=url+"?";
            for(String key : map.keySet()){
                url=url+key+"="+map.get(key)+"&";
            }
            url=url.substring(0,url.length()-1);
        }
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<String>() {
                });
        String body = exchange.getBody();
        return body;
    }


}
