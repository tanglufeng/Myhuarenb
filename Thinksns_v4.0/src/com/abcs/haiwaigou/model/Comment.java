package com.abcs.haiwaigou.model;

import java.io.Serializable;

/**
 * Created by zjz on 2016/3/22.
 */
public class Comment implements Serializable{
    Integer id;
    String geval_id;
    String geval_orderid;
    String geval_orderno;
    String geval_ordergoodsid;
    String geval_goodsid;
    String geval_goodsname;
    String geval_goodsimage;
    double geval_goodsprice;
    int geval_scores;
    String geval_content;
    boolean geval_isanonymous;
    Long geval_addtime;
    String geval_storeid;
    String geval_storename;
    String geval_frommemberid;
    String geval_frommembername;
    String geval_remark;
    String geval_explain;
    String geval_image;





    public String getGeval_id() {
        return geval_id;
    }

    public void setGeval_id(String geval_id) {
        this.geval_id = geval_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGeval_orderid() {
        return geval_orderid;
    }

    public void setGeval_orderid(String geval_orderid) {
        this.geval_orderid = geval_orderid;
    }

    public String getGeval_goodsimage() {
        return geval_goodsimage;
    }

    public void setGeval_goodsimage(String geval_goodsimage) {
        this.geval_goodsimage = geval_goodsimage;
    }

    public String getGeval_orderno() {
        return geval_orderno;
    }

    public void setGeval_orderno(String geval_orderno) {
        this.geval_orderno = geval_orderno;
    }

    public String getGeval_ordergoodsid() {
        return geval_ordergoodsid;
    }

    public void setGeval_ordergoodsid(String geval_ordergoodsid) {
        this.geval_ordergoodsid = geval_ordergoodsid;
    }

    public String getGeval_goodsid() {
        return geval_goodsid;
    }

    public void setGeval_goodsid(String geval_goodsid) {
        this.geval_goodsid = geval_goodsid;
    }

    public String getGeval_goodsname() {
        return geval_goodsname;
    }

    public void setGeval_goodsname(String geval_goodsname) {
        this.geval_goodsname = geval_goodsname;
    }

    public double getGeval_goodsprice() {
        return geval_goodsprice;
    }

    public void setGeval_goodsprice(double geval_goodsprice) {
        this.geval_goodsprice = geval_goodsprice;
    }

    public int getGeval_scores() {
        return geval_scores;
    }

    public void setGeval_scores(int geval_scores) {
        this.geval_scores = geval_scores;
    }

    public String getGeval_content() {
        return geval_content;
    }

    public void setGeval_content(String geval_content) {
        this.geval_content = geval_content;
    }

    public boolean isGeval_isanonymous() {
        return geval_isanonymous;
    }

    public void setGeval_isanonymous(boolean geval_isanonymous) {
        this.geval_isanonymous = geval_isanonymous;
    }

    public Long getGeval_addtime() {
        return geval_addtime;
    }

    public void setGeval_addtime(Long geval_addtime) {
        this.geval_addtime = geval_addtime;
    }

    public String getGeval_storeid() {
        return geval_storeid;
    }

    public void setGeval_storeid(String geval_storeid) {
        this.geval_storeid = geval_storeid;
    }

    public String getGeval_storename() {
        return geval_storename;
    }

    public void setGeval_storename(String geval_storename) {
        this.geval_storename = geval_storename;
    }

    public String getGeval_frommemberid() {
        return geval_frommemberid;
    }

    public void setGeval_frommemberid(String geval_frommemberid) {
        this.geval_frommemberid = geval_frommemberid;
    }

    public String getGeval_frommembername() {
        return geval_frommembername;
    }

    public void setGeval_frommembername(String geval_frommembername) {
        this.geval_frommembername = geval_frommembername;
    }

    public String getGeval_remark() {
        return geval_remark;
    }

    public void setGeval_remark(String geval_remark) {
        this.geval_remark = geval_remark;
    }

    public String getGeval_explain() {
        return geval_explain;
    }

    public void setGeval_explain(String geval_explain) {
        this.geval_explain = geval_explain;
    }

    public String getGeval_image() {
        return geval_image;
    }

    public void setGeval_image(String geval_image) {
        this.geval_image = geval_image;
    }
}
