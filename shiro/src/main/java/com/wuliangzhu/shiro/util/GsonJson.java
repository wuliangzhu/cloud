package com.wuliangzhu.shiro.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonJson {
    /**
     * 将json转换成对象
     *
     * @param json
     * @param clzz
     * @param <T>
     * @return
     */
    public static <T> T parseJson(String json, Class<T> clzz) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clzz);
        } catch (Exception e) {
            throw new RuntimeException("将 Json 转换为对象时异常,数据是:" + json, e);
        }
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.toJson(obj);
        } catch (Exception e) {
            throw new RuntimeException("将对象转成Json时出错,数据是:" + obj, e);
        }
    }
}
