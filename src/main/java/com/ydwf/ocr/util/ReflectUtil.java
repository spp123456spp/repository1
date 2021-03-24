package com.ydwf.ocr.util;

import com.ydwf.bridge.requestbean.IDCardRequestBean;

import java.lang.reflect.Field;

public class ReflectUtil {

    public static String key="D9EA46D637E01A2C4C1C9DDFAB7958ABB9DAF2B01C522FE9";

    public static void decode(Object object,String key,String code) throws Exception{

        Class<?> aClass = object.getClass();
        Field[] fields = aClass.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String name = field.getName();
            Object o = field.get(object);
            if(o!=null&&!name.equals("idcardStr")){
                String string = o.toString();
                String sms4toString = SM4Util.decodeSMS4toString(string, key, code);
                field.set(object,(Object) sms4toString);
                //System.out.println(name);
            }
        }
        //System.out.println(object);
        //return object;
    }

    public static void decode(Object object,String code) throws Exception{

        Class<?> aClass = object.getClass();
        Field[] fields = aClass.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String name = field.getName();
            Object o = field.get(object);
            if(o!=null&&!name.equals("idcardStr")){
                String string = o.toString();
                String sms4toString = SM4Util.decodeSMS4toString(string, code);
                field.set(object,(Object) sms4toString);
                //System.out.println(name);
            }
        }
        //System.out.println(object);
        //return object;
    }



    public static void encode(Object object,String key) throws Exception{

        Class<?> aClass = object.getClass();
        Field[] fields = aClass.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String name = field.getName();
            Object o = field.get(object);
            if(o!=null&&!name.equals("idcardStr")){
                String string = o.toString();
                String sms4toString = SM4Util.encodeSMS4(string, key);
                field.set(object,(Object) sms4toString);
                //System.out.println(name);
            }
        }
        //System.out.println(object);
        //return object;
    }

    public static void encode(Object object) throws Exception{

        Class<?> aClass = object.getClass();
        Field[] fields = aClass.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String name = field.getName();
            Object o = field.get(object);
            if(o!=null&&!name.equals("idcardStr")){
                String string = o.toString();
                String sms4toString = SM4Util.encodeSMS4(string);
                field.set(object,(Object) sms4toString);
                //System.out.println(name);
            }
        }
        //System.out.println(object);
        //return object;
    }

}
