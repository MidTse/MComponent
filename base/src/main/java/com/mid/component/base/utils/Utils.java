package com.mid.component.base.utils;

import android.net.ParseException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * <pre>
 *     @author : mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2019/02/17
 *     desc    : 工具类
 *     version : 0.1.0
 * </pre>
 */

public class Utils {

    private Utils(){
        throw new IllegalStateException("you can't instantiate me!");
    }


    /**
     * 对HttpException返回的异常码进行解析并返回
     * @param httpException
     * @return
     */
    public static String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() == 500) {
            msg = "服务器发生错误";
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }


    /**
     * 将异常转换成相应文字提示
     * @param t
     * @return
     */
    public static String converException(Throwable t) {
        String msg = t.getMessage();
        if (t instanceof UnknownHostException) {
            msg = "网络不可用";
        } else if (t instanceof SocketTimeoutException) {
            msg = "请求网络超时";
        } else if (t instanceof ConnectException) {
            msg = "请求连接异常";
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            msg = convertStatusCode(httpException);
        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "数据解析错误";
        }
        return msg;
    }
}
