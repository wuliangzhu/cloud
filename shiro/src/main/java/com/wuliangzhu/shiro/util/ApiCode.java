package com.wuliangzhu.shiro.util;

/**
 * <p/>
 * Api状态码
 * <p/>
 * 1xxx	通用状态码
 * 2xxx	类目&商品&搜索状态码
 * 3xxx	预留功能状态码
 * 4xxx	订单相关状态码
 * 5xxx	购物车&收藏夹状态码
 * 6xxx	预留功能状态码
 * 7xxx	优惠FAQ等相关状态码
 * 8xxx	账户相关状态码
 * 9xxx 商品资讯状态码
 * 11xxx 退款退货
 */
public enum ApiCode {
    NONE_CODE(0) {
        public String getMessage() {
                    return "";
                }
    },
    OK(1) {
      public String getMessage() {
          return "ok";
      }
    },
    SUCCESS(2) {
        public String getMessage() {
            return "success";
        }
    },
    ERROR_500(3) {
        public String getMessage() {
            return "insteral error";
        }
    };

    ////////////////////////////华丽的分割线/////////////////////////////
    private final int value;

    private ApiCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public abstract String getMessage();
}
