package com.wan.android.bean;

public class UncollectRepsonse {

   private String data;
   private int errorCode;
   private String errorMsg;


    public void setData(String data) {
        this.data = data;
    }
    public String getData() {
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