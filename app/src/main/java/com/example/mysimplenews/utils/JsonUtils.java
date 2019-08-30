package com.example.mysimplenews.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

/**
 *Json转换工具类
 */

public class JsonUtils {

    private static Gson mGson=new Gson();

    //将对象转换为Json字符串
    public static <T>String serialize(T object){return mGson.toJson(object);}

    //将json字符串转换为对象
    public static <T> T deserialize(String json,Class<T> clz){return mGson.fromJson(json,clz);}

    //将json对象转换为实体对象
    public static <T> T deserialize(JsonObject json, Class<T> clz){return mGson.fromJson(json,clz);}

    //将json字符串转换为对象
    public static <T> T deserialize(String json, Type type){return mGson.fromJson(json,type);}




}
