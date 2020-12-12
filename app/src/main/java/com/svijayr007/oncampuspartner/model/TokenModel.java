package com.svijayr007.oncampuspartner.model;

public class TokenModel {
    private String phone, token,uid;



    public TokenModel() {
    }

    public TokenModel(String phone, String token, String uid) {
        this.phone = phone;
        this.token = token;
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
