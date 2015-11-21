package com.models.entity;

/**
 * Created by kdao on 11/21/15.
 */
public class Services {
    private String serviceid;
    private String servicename;
    private String uid;
    private String servicetype;
    private String status;
    private String datecreated;
    private String json;

    public String getServiceid() {
        return serviceid;
    }
    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getServicetype() {
        return servicetype;
    }
    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDatecreated() {
        return datecreated;
    }
    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }
    public String getJson() {
        return json;
    }
    public void setJson(String json) {
        this.json = json;
    }
    public String getServicename() {
        return servicename;
    }
    public void setServicename(String servicename) {
        this.servicename = servicename;
    }
}
