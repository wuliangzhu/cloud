package com.wuliangzhu.shiro.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;


public class BaseJsonResult implements Serializable {
    public static final SerializeConfig serializeConfig = new SerializeConfig();
    static {
//        serializeConfig.put(Long.class, new LongCurrencySerializer());
    }
    /**
     * 是否成功,默认成功
     */
    protected boolean success = true;

    /**
     * 消息
     */
    protected String msg = "";

    /**
     * API状态码
     */
    protected ApiCode apiCode;

    public BaseJsonResult() {
    }

    public BaseJsonResult(boolean success) {
        this.success = success;
    }

    public BaseJsonResult(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
        if (success) {
            this.apiCode = ApiCode.OK;
        } else {
            if (this.apiCode == null) {
                this.apiCode = ApiCode.ERROR_500;
            }
        }
    }

    public BaseJsonResult(boolean success, ApiCode apiCode) {
        this.success = success;
        this.msg = apiCode.getMessage();
        this.apiCode = apiCode;
    }

    public BaseJsonResult(boolean success, String msg, ApiCode apiCode) {
        this.success = success;
        this.msg = msg;
        this.apiCode = apiCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public int getApiCode() {
        return apiCode.getValue();
    }

    public static String formatMsg(String msg, Object[] arguments) {
        return MessageFormat.format(msg, arguments);
    }

    public String toJson() {
        if (serializeConfig == null) {
            return JSON.toJSONString(this,
                    SerializerFeature.WriteDateUseDateFormat,
                    SerializerFeature.WriteEnumUsingToString);
        } else {
            return JSON.toJSONString(this, serializeConfig,
                    SerializerFeature.WriteDateUseDateFormat,
                    SerializerFeature.WriteEnumUsingToString);
        }

        /*return JSONObject.toJSONString(this,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteEnumUsingToString);*/
    }

    public void toJson(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(toJson());
    }

    public void toGsonJson(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(toGsonJson());
    }

    public String toGsonJson() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(this);
    }

    public void setApiCode(ApiCode apiCode) {
        this.apiCode = apiCode;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return toJson();
    }

}
