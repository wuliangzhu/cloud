package com.wuliangzhu.shiro.util;

import com.alibaba.fastjson.annotation.JSONType;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@JSONType(orders = {"apiCode", "success", "msg", "result"})
public class JsonResult extends BaseJsonResult {

    private Map<String, Object> result = new HashMap<>();

    public JsonResult() {
    }

    public JsonResult(boolean success) {
        super(success);
    }

    public JsonResult(boolean success, String msg) {
        super(success, msg);
    }

    public JsonResult(boolean success, ApiCode apiCode) {
        super(success, apiCode);
    }

    public JsonResult(boolean success, String msg, ApiCode apiCode) {
        super(success, msg, apiCode);
    }

    public JsonResult addResult(String name, Object value) {
        result.put(name, value);
        return this;
    }

    public JsonResult(Map<String, Object> data){
        super(true, ApiCode.OK);
        this.result = data;
    }

    public static JsonResult successJsonResult(Map<String, Object> data){
        return new JsonResult(data);
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> data) {
        this.result = data;
    }

    public static JsonResult success() {
        return new JsonResult(true, ApiCode.OK);
    }

    public static JsonResult success(String msg) {
        return new JsonResult(true, msg);
    }

    public static JsonResult success(ApiCode apiCode) {
        return new JsonResult(true, apiCode);
    }

    public static JsonResult success(String msg, ApiCode apiCode) {
        return new JsonResult(true, msg, apiCode);
    }

    public static JsonResult fail() {
        return new JsonResult(false);
    }

    public static JsonResult fail(String msg) {
        return new JsonResult(false, msg);
    }

    public static JsonResult fail(ApiCode apiCode) {
        JsonResult ret = new JsonResult(false, apiCode);
        if (isBlank(ret.getMsg())) {
            ret.setMsg(apiCode.getMessage());
        }
        return ret;
    }

    public static boolean isBlank(String message) {
        return message == null || message.trim().length() == 0;
    }
    public static JsonResult fail(String msg, ApiCode apiCode) {
        return new JsonResult(false, msg, apiCode);
    }

    public void autoConvertJSONP(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isLogin = false; //SessionHelper.isLogin(request);
        this.addResult("__isLogin_", isLogin);
        String callback = request.getParameter("callback");
        if(isBlank(callback)) {
            callback = request.getParameter("jsonp");
        }

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();

        if(isBlank(callback)) {
            outputStream.write(toJson().getBytes("UTF-8"));
        } else if(callbackIsSafe(callback)) {
            outputStream.write(callback.getBytes("UTF-8"));
            outputStream.write(0x28);  //(
            outputStream.write(toJson().getBytes("UTF-8"));
            outputStream.write(0x29);  //)
        }
    }


    public void autoConvertJSONP(boolean isLogin, HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.addResult("__isLogin_", isLogin);
        String callback = request.getParameter("callback");
        if(isBlank(callback)) {
            callback = request.getParameter("jsonp");
        }

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();

        if(isBlank(callback)) {
            outputStream.write(toJson().getBytes("UTF-8"));
        } else if(callbackIsSafe(callback)) {
            outputStream.write(callback.getBytes("UTF-8"));
            outputStream.write(0x28);  //(
            outputStream.write(toJson().getBytes("UTF-8"));
            outputStream.write(0x29);  //)
        }
    }

    //只允许包含大小写英文字母，美元符号$，下划线_，数字0-9（不可作为开头），点号.（不可作为开头与结尾），由调用方保证callback不为blank
    private boolean callbackIsSafe(String callback) {
        char c0 = callback.charAt(0);
        if(!__callbackIsSafe(c0)) return false;

        int endIndex = callback.length() - 1;
        char ce = callback.charAt(endIndex);
        if(!_callbackIsSafe(ce)) return false;

        for(int i = 1, j = endIndex; i < j; i++) {
            char c = callback.charAt(i);
            if(!(_callbackIsSafe(c) || (c == '.' && __callbackIsSafe(callback.charAt(i + 1))))) {
                return false;
            }
        }
        return true;
    }

    private boolean _callbackIsSafe(char c) {
        return __callbackIsSafe(c) || '0' <= c && c <= '9';
    }

    private boolean __callbackIsSafe(char c) {
        return ('a' <= c && c <= 'z' || 'A' <= c && c <= 'Z' || c == '$' || c == '_');
    }
}
