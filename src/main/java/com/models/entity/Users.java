package com.models.entity;

/**
 * Created by kdao on 11/21/15.
 */
public class Users {

    private Integer uid;
    private String username;
    private String password;

    /**
     * Constructor
     */
    public Users(String username, String password) {
        this.username = username;
        this.password = password;
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
    public Integer getUid() {
        return uid;
    }
    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
