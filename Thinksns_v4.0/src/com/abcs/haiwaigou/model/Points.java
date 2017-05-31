package com.abcs.haiwaigou.model;

import java.io.Serializable;

/**
 * Created by zjz on 2016/4/27.
 */
public class Points implements Serializable {

    private int id;
    private String pl_id;
    private String pl_memberid;
    private String pl_membername;
    private String pl_adminid;
    private String pl_adminname;
    private String pl_points;
    private Long pl_addtime;
    private String pl_desc;
    private String pl_stage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPl_id() {
        return pl_id;
    }

    public void setPl_id(String pl_id) {
        this.pl_id = pl_id;
    }

    public String getPl_memberid() {
        return pl_memberid;
    }

    public void setPl_memberid(String pl_memberid) {
        this.pl_memberid = pl_memberid;
    }

    public String getPl_membername() {
        return pl_membername;
    }

    public void setPl_membername(String pl_membername) {
        this.pl_membername = pl_membername;
    }

    public String getPl_adminid() {
        return pl_adminid;
    }

    public void setPl_adminid(String pl_adminid) {
        this.pl_adminid = pl_adminid;
    }

    public String getPl_adminname() {
        return pl_adminname;
    }

    public void setPl_adminname(String pl_adminname) {
        this.pl_adminname = pl_adminname;
    }

    public String getPl_points() {
        return pl_points;
    }

    public void setPl_points(String pl_points) {
        this.pl_points = pl_points;
    }

    public Long getPl_addtime() {
        return pl_addtime;
    }

    public void setPl_addtime(Long pl_addtime) {
        this.pl_addtime = pl_addtime;
    }

    public String getPl_desc() {
        return pl_desc;
    }

    public void setPl_desc(String pl_desc) {
        this.pl_desc = pl_desc;
    }

    public String getPl_stage() {
        return pl_stage;
    }

    public void setPl_stage(String pl_stage) {
        this.pl_stage = pl_stage;
    }
}
