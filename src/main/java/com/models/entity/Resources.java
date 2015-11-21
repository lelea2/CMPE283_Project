package com.models.entity;

/**
 * Created by kdao on 11/21/15.
 */
public class Resources {
    private String resourceid;
    private String serviceid;
    private String resourcetype;
    private String status;
    private String datecreated;
    private String json;

    public String getResourceid() {
        return resourceid;
    }
    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }
    public String getServiceid() {
        return serviceid;
    }
    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }
    public String getResourcetype() {
        return resourcetype;
    }
    public void setResourcetype(String resourcetype) {
        this.resourcetype = resourcetype;
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
}
