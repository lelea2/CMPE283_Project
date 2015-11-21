package com.models.entity;

/**
 * Created by kdao on 11/21/15.
 */
public class Resources_Usages {
    private String userid;
    private String serviceid;
    private String resourceid;
    private String starttime;
    private String endtime;
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getServiceid() {
        return serviceid;
    }
    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }
    public String getResourceid() {
        return resourceid;
    }
    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }
    public String getStarttime() {
        return starttime;
    }
    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
    public String getEndtime() {
        return endtime;
    }
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}
