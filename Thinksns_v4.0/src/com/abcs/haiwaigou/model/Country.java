package com.abcs.haiwaigou.model;

import java.io.Serializable;

/**
 * Created by zjz on 2016/1/14.
 */
public class Country implements Serializable{
    private String picarr;
    private String name;
    private String cid;
    private int id;

    public String getPicarr() {
        return picarr;
    }

    public void setPicarr(String picarr) {
        this.picarr = picarr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Country{" +
                "picarr='" + picarr + '\'' +
                ", name='" + name + '\'' +
                ", cid='" + cid + '\'' +
                ", id=" + id +
                '}';
    }
}
