package com.abcs.haiwaigou.model;

import java.io.Serializable;

/**
 * Created by zjz on 2016/4/19.
 */
public class Voucher implements Serializable{


    /**
     * voucher_t_id : 2
     * voucher_t_title : 限时抢购华侨帮代金券
     * voucher_t_desc : 消费满5000即可用
     * voucher_t_start_date : 1457452842
     * voucher_t_end_date : 1483372432
     * voucher_t_price : 100
     * voucher_t_limit : 5000
     * voucher_t_store_id : 1
     * voucher_t_storename : 华侨帮官方店
     * voucher_t_sc_id : 0
     * voucher_t_creator_id : 1
     * voucher_t_state : 1
     * voucher_t_total : 100
     * voucher_t_giveout : 0
     * voucher_t_used : 0
     * voucher_t_add_date : 1457452842
     * voucher_t_quotaid : 2
     * voucher_t_points : 10000
     * voucher_t_eachlimit : 2
     * voucher_t_styleimg : null
     * voucher_t_customimg : http://www.huaqiaobang.com/data/upload/shop/voucher/1/05107968426002469.jpg
     * voucher_t_recommend : 1
     * voucher_t_sc_name : null
     */

    private int id;
    private String voucher_t_id;
    private String voucher_code;
    private String voucher_t_title;
    private String voucher_t_desc;
    private Long voucher_t_start_date;
    private Long voucher_t_end_date;
    private String voucher_t_price;
    private int voucher_t_limit;
    private String voucher_t_store_id;
    private String voucher_t_storename;
    private String voucher_t_sc_id;
    private String voucher_t_creator_id;
    private String voucher_t_state;
    private String voucher_state_text;
    private String voucher_t_total;
    private String voucher_t_giveout;
    private String voucher_t_used;
    private Long voucher_t_add_date;
    private String voucher_t_quotaid;
    private String voucher_t_points;
    private String voucher_t_eachlimit;
    private Object voucher_t_styleimg;
    private String voucher_t_customimg;
    private String voucher_t_recommend;
    private Object voucher_t_sc_name;

    public String getVoucher_state_text() {
        return voucher_state_text;
    }

    public void setVoucher_state_text(String voucher_state_text) {
        this.voucher_state_text = voucher_state_text;
    }

    public String getVoucher_code() {
        return voucher_code;
    }

    public void setVoucher_code(String voucher_code) {
        this.voucher_code = voucher_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoucher_t_id() {
        return voucher_t_id;
    }

    public void setVoucher_t_id(String voucher_t_id) {
        this.voucher_t_id = voucher_t_id;
    }

    public String getVoucher_t_title() {
        return voucher_t_title;
    }

    public void setVoucher_t_title(String voucher_t_title) {
        this.voucher_t_title = voucher_t_title;
    }

    public String getVoucher_t_desc() {
        return voucher_t_desc;
    }

    public void setVoucher_t_desc(String voucher_t_desc) {
        this.voucher_t_desc = voucher_t_desc;
    }

    public Long getVoucher_t_start_date() {
        return voucher_t_start_date;
    }

    public void setVoucher_t_start_date(Long voucher_t_start_date) {
        this.voucher_t_start_date = voucher_t_start_date;
    }

    public Long getVoucher_t_end_date() {
        return voucher_t_end_date;
    }

    public void setVoucher_t_end_date(Long voucher_t_end_date) {
        this.voucher_t_end_date = voucher_t_end_date;
    }

    public String getVoucher_t_price() {
        return voucher_t_price;
    }

    public void setVoucher_t_price(String voucher_t_price) {
        this.voucher_t_price = voucher_t_price;
    }

    public int getVoucher_t_limit() {
        return voucher_t_limit;
    }

    public void setVoucher_t_limit(int voucher_t_limit) {
        this.voucher_t_limit = voucher_t_limit;
    }

    public String getVoucher_t_store_id() {
        return voucher_t_store_id;
    }

    public void setVoucher_t_store_id(String voucher_t_store_id) {
        this.voucher_t_store_id = voucher_t_store_id;
    }

    public String getVoucher_t_storename() {
        return voucher_t_storename;
    }

    public void setVoucher_t_storename(String voucher_t_storename) {
        this.voucher_t_storename = voucher_t_storename;
    }

    public String getVoucher_t_sc_id() {
        return voucher_t_sc_id;
    }

    public void setVoucher_t_sc_id(String voucher_t_sc_id) {
        this.voucher_t_sc_id = voucher_t_sc_id;
    }

    public String getVoucher_t_creator_id() {
        return voucher_t_creator_id;
    }

    public void setVoucher_t_creator_id(String voucher_t_creator_id) {
        this.voucher_t_creator_id = voucher_t_creator_id;
    }

    public String getVoucher_t_state() {
        return voucher_t_state;
    }

    public void setVoucher_t_state(String voucher_t_state) {
        this.voucher_t_state = voucher_t_state;
    }

    public String getVoucher_t_total() {
        return voucher_t_total;
    }

    public void setVoucher_t_total(String voucher_t_total) {
        this.voucher_t_total = voucher_t_total;
    }

    public String getVoucher_t_giveout() {
        return voucher_t_giveout;
    }

    public void setVoucher_t_giveout(String voucher_t_giveout) {
        this.voucher_t_giveout = voucher_t_giveout;
    }

    public String getVoucher_t_used() {
        return voucher_t_used;
    }

    public void setVoucher_t_used(String voucher_t_used) {
        this.voucher_t_used = voucher_t_used;
    }

    public Long getVoucher_t_add_date() {
        return voucher_t_add_date;
    }

    public void setVoucher_t_add_date(Long voucher_t_add_date) {
        this.voucher_t_add_date = voucher_t_add_date;
    }

    public String getVoucher_t_quotaid() {
        return voucher_t_quotaid;
    }

    public void setVoucher_t_quotaid(String voucher_t_quotaid) {
        this.voucher_t_quotaid = voucher_t_quotaid;
    }

    public String getVoucher_t_points() {
        return voucher_t_points;
    }

    public void setVoucher_t_points(String voucher_t_points) {
        this.voucher_t_points = voucher_t_points;
    }

    public String getVoucher_t_eachlimit() {
        return voucher_t_eachlimit;
    }

    public void setVoucher_t_eachlimit(String voucher_t_eachlimit) {
        this.voucher_t_eachlimit = voucher_t_eachlimit;
    }

    public Object getVoucher_t_styleimg() {
        return voucher_t_styleimg;
    }

    public void setVoucher_t_styleimg(Object voucher_t_styleimg) {
        this.voucher_t_styleimg = voucher_t_styleimg;
    }

    public String getVoucher_t_customimg() {
        return voucher_t_customimg;
    }

    public void setVoucher_t_customimg(String voucher_t_customimg) {
        this.voucher_t_customimg = voucher_t_customimg;
    }

    public String getVoucher_t_recommend() {
        return voucher_t_recommend;
    }

    public void setVoucher_t_recommend(String voucher_t_recommend) {
        this.voucher_t_recommend = voucher_t_recommend;
    }

    public Object getVoucher_t_sc_name() {
        return voucher_t_sc_name;
    }

    public void setVoucher_t_sc_name(Object voucher_t_sc_name) {
        this.voucher_t_sc_name = voucher_t_sc_name;
    }
}
