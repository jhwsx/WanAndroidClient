package com.wan.android.data.network.model;

/**
 * @author wzc
 * @date 2018/3/27
 */
public class CommonException {
    private int mErrorCode;
    private String mErrorMessage;

    public CommonException(int errorCode, String errorMessage) {
        mErrorCode = errorCode;
        mErrorMessage = errorMessage;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int errorCode) {
        mErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "CommonException{" +
                "mErrorCode=" + mErrorCode +
                ", mErrorMessage='" + mErrorMessage + '\'' +
                '}';
    }

    public static CommonException convert(ErrorCodeMessageEnum errorCodeMessage) {
        return new CommonException(errorCodeMessage.getErrorCode(), errorCodeMessage.getErrorMsg());
    }
}
