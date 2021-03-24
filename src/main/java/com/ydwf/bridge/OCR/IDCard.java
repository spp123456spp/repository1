package com.ydwf.bridge.OCR;

import com.alibaba.fastjson.JSONObject;
import com.ydwf.bridge.base.CommonApiOfInssue;
import com.ydwf.bridge.base.IssueOutException;
import com.ydwf.bridge.projectbaseserver;
import com.ydwf.bridge.requestbean.IDCardRequestBean;
import com.ydwf.bridge.requestbean.IssueForOutApiRequestBean;
import com.ydwf.bridge.requestbean.OCRIDCARD;
import com.ydwf.cloud.framework.DataMap;
import com.ydwf.component.LoginUserInfo;
import com.ydwf.component.ServerContext;
import com.ydwf.ocr.util.IDUtil;
import com.ydwf.ocr.util.PropertyUtil;
import com.ydwf.ocr.util.ReflectUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.*;

@RestController
@Service("discriminate-IDCard")
public class IDCard extends CommonApiOfInssue {

    @Resource
    private RestTemplate restTemplate;

    private static String key="D9EA46D637E01A2C4C1C9DDFAB7958ABB9DAF2B01C522FE9";
    private static String webankAppId;
    private static String secret;
    private static String tokenUrl;
    private static String ticketUrl;
    private static String idcardUrl;

    static {
        webankAppId= PropertyUtil.getProperty("webankAppId", "parameter");
        secret=PropertyUtil.getProperty("secret", "parameter");
        tokenUrl= PropertyUtil.getProperty("tokenUrl", "parameter");
        ticketUrl=PropertyUtil.getProperty("ticketUrl", "parameter");
        idcardUrl= PropertyUtil.getProperty("idcardUrl", "parameter");
    }

    @RequestMapping(value = "/discriminateIDCard",method = RequestMethod.POST)
    public Map discriminateIDCard(
            @ApiIgnore ServerContext context,/*上下文不包含在API参数定义中*/
            @RequestBody IssueForOutApiRequestBean requestBean,
            @ApiIgnore LoginUserInfo loginUserInfo /*上下文不包含在API参数定义中*/) throws Exception{
        String requestStr;
        try{
            requestStr = this.checkAndVerifyRequest(context, requestBean, "H5PageOFISSUE");
        }catch (IssueOutException ex){
            return DataMap.build().setFlag(false).setMessage(context.collectExceptionStack(ex));
        }
        //JSONObject jsonObject = JSONObject.parseObject(requestStr);
        IDCardRequestBean bean = JSONObject.parseObject(requestStr, IDCardRequestBean.class);
        System.out.println(bean);
        OCRIDCARD ocridcard = new OCRIDCARD();
        ocridcard.webankAppId=webankAppId;
        ocridcard.cardType=bean.cardType;
        ocridcard.idcardStr=requestBean.CARDSTR;
        ocridcard.nonce= IDUtil.getItemID(32);
        ocridcard.userId=bean.userId;
        ocridcard.version=bean.version;
        ocridcard.orderNo=bean.orderNo;
        String ticket = getTicket(getToken(bean.version),bean.version);
        String sign = getSign(bean.version, ticket, bean.orderNo, webankAppId, ocridcard.nonce);
        ocridcard.sign=sign;
        /*HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(60*20);*/
        String idCardMap = getIDCard(ocridcard);
        JSONObject jsonObject = JSONObject.parseObject(idCardMap);
        Map<String, Object> idCard = jsonObject.getInnerMap();
        return this.encryptForResponse(idCard);
    }


    //获取token接口
    private String getToken(String version) throws Exception{
        Map<String, String>  map= new HashMap<>();
        //业务流程唯一标识，即 wbappid，可参考 获取 WBappid 指map.put("app_id",webankAppId);引在人脸核身控制台内申请
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



    //获取身份证扫描后得到的信息
    private String getIDCard(OCRIDCARD idCardRequestBean) throws Exception{
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type","application/json");
        HttpEntity<OCRIDCARD> entity = new HttpEntity<OCRIDCARD>(idCardRequestBean, requestHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(
                idcardUrl,
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
