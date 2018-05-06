package com.wan.android.data.bean;

import com.wan.android.annotation.NotProguard;

import java.util.List;

/**
 * 账户数据类
 * @author wzc
 * @date 2018/3/12
 */
@NotProguard
public class AccountData {

        private List<Integer> collectIds;
        private String email;
        private String icon;
        private int id;
        private String password;
        private int type;
        private String username;


        public void setCollectids(List<Integer> collectids) {
            this.collectIds = collectids;
        }
        public List<Integer> getCollectids() {
            return collectIds;
        }


        public void setEmail(String email) {
            this.email = email;
        }
        public String getEmail() {
            return email;
        }


        public void setIcon(String icon) {
            this.icon = icon;
        }
        public String getIcon() {
            return icon;
        }


        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }


        public void setPassword(String password) {
            this.password = password;
        }
        public String getPassword() {
            return password;
        }


        public void setType(int type) {
            this.type = type;
        }
        public int getType() {
            return type;
        }


        public void setUsername(String username) {
            this.username = username;
        }
        public String getUsername() {
            return username;
        }

    }