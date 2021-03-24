package com.ydwf.ocr.demo;


import com.ydwf.ocr.util.SM4Util;

public class EncodeDemo {

    public static String key="D9EA46D637E01A2C4C1C9DDFAB7958ABB9DAF2B01C522FE9";

    public static void main(String[] args) throws Exception {

        encode("123");
        //decode("2A9557C5DCF984EA24047B591AFC7F62");

    }



    public static void encode(String s) throws Exception{
        //System.out.println("*****************************************************************");
        String encodeSMS4 = SM4Util.encodeSMS4(s,key);
        System.out.println(encodeSMS4);
        //decode(encodeSMS4);
    }

    public static void decode(String s) throws Exception{
        //System.out.println("*****************************************************************");
        String decodeSMS4toString = SM4Util.decodeSMS4toString(s, key,"UTF-8");
        System.out.println(decodeSMS4toString);

    }
}
