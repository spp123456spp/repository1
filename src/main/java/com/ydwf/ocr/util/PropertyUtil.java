package com.ydwf.ocr.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

    private static Properties properties=new Properties();

    public static String getProperty(String name,String propertyName) {
        if(propertyName == null || propertyName.length() == 0
                || propertyName.trim().equals("")){
            throw new NullPointerException("文件名不能为空");
        }else if(propertyName.lastIndexOf(".properties")<0){
            propertyName=propertyName+".properties";
        }
        InputStream resourceAsStream = PropertyUtil.class.getClassLoader().getResourceAsStream(propertyName);
        if(resourceAsStream==null){
            throw new NullPointerException("未找到指定文件");
        }
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String property = properties.getProperty(name);
        return property;
    }


}
