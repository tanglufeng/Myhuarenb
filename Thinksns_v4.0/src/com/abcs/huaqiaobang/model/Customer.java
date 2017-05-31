package com.abcs.huaqiaobang.model;

/**
 * Created by Administrator on 2015/12/24.
 */
public class Customer {


    /**
     * _id : 5646a84e189db90541c409fa
     * country : 中国
     * directcount : 0
     * endTime : 1766306926991
     * indirectcount : 0
     * startTime : 1450946926991
     * uid : 10001
     * vipcode : 19881223
     * viplevel : 1
     */

    private String _id;
    private String country;
    private String directcount;
    private long endTime;
    private String indirectcount;
    private long startTime;
    private String uid;
    private String vipcode;
    private String viplevel;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDirectcount(String directcount) {
        this.directcount = directcount;
    }


    public void setIndirectcount(String indirectcount) {
        this.indirectcount = indirectcount;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setVipcode(String vipcode) {
        this.vipcode = vipcode;
    }

    public void setViplevel(String viplevel) {
        this.viplevel = viplevel;
    }

    public String get_id() {
        return _id;
    }

    public String getCountry() {
        return country;
    }

    public String getDirectcount() {
        return directcount;
    }


    public String getIndirectcount() {
        return indirectcount;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getUid() {
        return uid;
    }

    public String getVipcode() {
        return vipcode;
    }

    public String getViplevel() {
        return viplevel;
    }
}
