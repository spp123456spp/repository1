package com.ydwf.ocr.demo;


import com.ydwf.ocr.util.PropertyUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class WebankDemo {

    public static void main(String[] args) throws Exception {
        getToken();
        //getProperty();
    }

    public static void  getProperty() throws Exception{
        String property = PropertyUtil.getProperty("name", "a.properties");
        System.out.println(property);



    }

    public static String getToken() throws Exception{

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.weixin.qq.com/sns/userinfo");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String toString = EntityUtils.toString(entity, "UTF-8");
        System.out.println(toString);


        return null;
    }


    public static void doPost(){
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();

        // 创建http POST请求
        HttpPost httpPost = new HttpPost("http://www.oschina.net/");

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpPost);
            System.out.println(response.getStatusLine());
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println(content);
            }
        }catch(Exception e){
            e.printStackTrace();

        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
