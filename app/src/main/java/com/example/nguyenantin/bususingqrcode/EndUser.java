package com.example.nguyenantin.bususingqrcode;

public class EndUser {

    private String username;
    private String password;
    private String usertype;
    private String email;
    private String qrcode;
    private float money;

    public EndUser() {
    }

    public EndUser(String username, String password, String usertype, String email, String qrcode, float money) {
        this.username = username;
        this.password = password;
        this.usertype = usertype;
        this.email = email;
        this.qrcode = qrcode;
        this.money = money;
    }

    public EndUser(String username, String usertype, String email, String qrcode, float money) {
        this.username = username;
        this.usertype = usertype;
        this.email = email;
        this.qrcode = qrcode;
        this.money = money;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}
