package com.wuliangzhu.shiro.controller;

import com.wuliangzhu.shiro.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BaseController {
    protected static Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected JsonResult getResult(boolean success, String msg) {
        if(success) {
            return JsonResult.success();
        }else {
            return JsonResult.fail(msg);
        }
    }

    protected JsonResult getResult(boolean success,String successMsg,String failMsg) {
        if(success) {
            return JsonResult.success(successMsg);
        }else {
            return JsonResult.fail(failMsg);
        }
    }

    protected JsonResult successResult(Map map){
        JsonResult jsonResult = JsonResult.success();
        jsonResult.setResult(map);
        return jsonResult;
    }

}
