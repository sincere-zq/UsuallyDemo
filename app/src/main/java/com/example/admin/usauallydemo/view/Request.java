package com.example.admin.usauallydemo.view;

import java.util.Map;

/**
 * Created by zengqiang on 2016/7/13 0013.
 * Description:请求对象
 */
public class Request {

    private String url;//请求地址

    /* 设置超时时间 */
    private int REQUEST_TIME_OUT = 10000;

    private Method method;//get,post

    private Map<String,String> params;//传入参数

    public Request(String url, int timeout, Method method, Map<String,String> map) {
        this.url = url;
        this.REQUEST_TIME_OUT = timeout;
        this.method = method;
        this.params = map;
    }

    public String getUrl() {
        return url;
    }

    public int getRequestTimeOut() {
        return REQUEST_TIME_OUT;
    }

    public Method getMethod() {
        return method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    /**
     * 请求方式get，post(get 接口读取数据，post可以插入数据，更新数据，删除数据)
     * 枚举：1.书写规范 2，限定了传入类型，节省了编译过程
     */
    public enum Method {

        GET("GET"), POST("POST");

        private String methed;

        Method(String methed) {
            this.methed = methed;
        }

        public String getMethed() {
            return methed;
        }
    }
}
