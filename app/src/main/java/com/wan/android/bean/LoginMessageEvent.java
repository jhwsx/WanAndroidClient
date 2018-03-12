package com.wan.android.bean;

/**
 * 登录信息事件
 *
 * @author wzc
 * @date 2018/2/24
 */
public class LoginMessageEvent {

    public final String username;

    public LoginMessageEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
