package com.abcs.haiwaigou.model;

import java.io.Serializable;

/**
 * Created by zjz on 2016/1/18.
 */
public class Brand implements Serializable {
    private int brandid;
    private String bname;
    private String bimg;
    private long time;
    public int getBrandid() {
        return brandid;
    }
    public void setBrandid(int brandid) {
        this.brandid = brandid;
    }
    public String getBname() {
        return bname;
    }
    public void setBname(String bname) {
        this.bname = bname;
    }
    public String getBimg() {
        return bimg;
    }
    public void setBimg(String bimg) {
        this.bimg = bimg;
    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    @Override
    public String toString() {
        return "Brand [brandid=" + brandid + ", bname=" + bname + ", bimg="
                + bimg + ", time=" + time + "]";
    }
}
