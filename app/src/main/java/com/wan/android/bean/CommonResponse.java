package com.wan.android.bean;

/**
 * 通用响应类
 *
 * @author wzc
 * @date 2018/3/12
 */
public class CommonResponse<T> {
    private T data;
    private int errorCode;
    private String errorMsg;


    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }


    public void setErrorcode(int errorcode) {
        this.errorCode = errorcode;
    }

    public int getErrorcode() {
        return errorCode;
    }


    public void setErrormsg(String errormsg) {
        this.errorMsg = errormsg;
    }

    public String getErrormsg() {
        return errorMsg;
    }
}
