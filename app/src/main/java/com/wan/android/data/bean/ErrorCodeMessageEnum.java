package com.wan.android.data.bean;

/**
 * @author wzc
 * @date 2018/3/28
 */
public enum ErrorCodeMessageEnum {

    NULL_RESPONSE(-1001, "response cannot be null"),
    NULL_BODY(-1002, "body cannot be null"),
    NULL_DATA(-1003, "no data");

    private int mErrorCode;
    private String mErrorMsg;

    ErrorCodeMessageEnum(int errorCode, String errorMsg) {
        mErrorCode = errorCode;
        mErrorMsg = errorMsg;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int errorCode) {
        mErrorCode = errorCode;
    }

    public String getErrorMsg() {
        return mErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        mErrorMsg = errorMsg;
    }
}
