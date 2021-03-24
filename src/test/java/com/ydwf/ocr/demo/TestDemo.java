package com.ydwf.ocr.demo;

import com.alibaba.fastjson.JSONObject;
import com.ydwf.bridge.requestbean.OCRIDCARD;
import com.ydwf.ocr.demo.Company;
import com.ydwf.ocr.util.IDUtil;
import com.ydwf.ocr.util.PropertyUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDemo {


    @Test
    public void test1(){

        Company company1 = Company.中韩;
        System.out.println(company1.getKey());
    }
    //get方法公用接口调用
    @Test
    public void test2() throws Exception{
        Map<String, String>  map= new HashMap<>();
        //业务流程唯一标识，即 wbappid，可参考 获取 WBappid 指引在人脸核身控制台内申请
        map.put("app_id","");
        //请根据 获取 Access Token 指引进行获取
        map.put("access_token","");
        //授权类型，默认值为：SIGN（必须小写）
        map.put("type","SIGN");
        //版本号，默认值为：1.0.0
        map.put("version","1.0.0");
        Map msg = getSign("https://idasc.webank.com/api/oauth2/api_ticket", map,Map.class);
        /*List list = (List) msg.get("tickets");
        Map map1 = (Map) list.get(0);
        String value = map1.get("value").toString();*/
    }
    /**
     * https://graph.qq.com/user/get_simple_userinfo?
     * access_token=1234ABD1234ABD&
     * oauth_consumer_key=12345&
     * openid=B08D412EEC4000FFC37CAABBDC1234CC&
     * format=json
     * */


    @Test
    public void test3() throws Exception{
        Map<String, String>  map= new HashMap<>();
        //业务流程唯一标识，即 wbappid，可参考 获取 WBappid 指引在人脸核身控制台内申请
        map.put("oauth_consumer_key","12345");
        //请根据 获取 Access Token 指引进行获取
        map.put("access_token","1234ABD1234ABD");
        //授权类型，默认值为：SIGN（必须小写）
        map.put("openid","B08D412EEC4000FFC37CAABBDC1234CC");
        //版本号，默认值为：1.0.0
        map.put("format","json");
        Map msg = getSign("https://graph.qq.com/user/get_simple_userinfo", map,Map.class);
        System.out.println(msg);
    }

    @Test
    public void test4(){
        System.out.println(IDUtil.getItemID(32));
    }

    @Test
    public void test5(){
        String sign = DigestUtils.shaHex("1.0.0FxlAe3HFtEy73Um0pJNzIUriwtfnS3KRcPXiesd5ulS4XRAIcT0FbfaP52dwZf5Saabc1457895464appId001kHoSxvLZGxSoFsjxlbzEoUzh5PAnTU7T");
        System.out.println(sign);
    }


    @Test
    public void test6() throws Exception{
        String idCardMap = getIDCard(new OCRIDCARD());
        JSONObject jsonObject = JSONObject.parseObject(idCardMap);
        Map<String, Object> idCard = jsonObject.getInnerMap();
        Object code = idCard.get("code");
        System.out.println(code);
        System.out.println(idCard);
    }

    @Test
    public void test7() throws Exception{
        String property = PropertyUtil.getProperty("webankAppId", "parameter");
        System.out.println(property);
    }

    //获取身份证扫描后得到的信息
    private String getIDCard(OCRIDCARD idCardRequestBean) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type","application/json");
        HttpEntity<OCRIDCARD> entity = new HttpEntity<OCRIDCARD>(idCardRequestBean, requestHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(
                "https://ida.webank.com/api/paas/idcardocrapp",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<String>() {
                }
        );
        String body = exchange.getBody();
        return body;
    }


    private <T>Map getSign(String url, Map<String,String> map, Class<T> C) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
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
        JSONObject jsonObject = JSONObject.parseObject(body);
        Map<String, Object> innerMap = jsonObject.getInnerMap();
        return innerMap;
    }


}
