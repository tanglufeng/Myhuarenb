package com.abcs.haiwaigou.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/3 0003.
 */
public class Prediposit implements Serializable {

    /**
     * pdc_id : 3
     * pdc_sn : 770513788215735015
     * pdc_member_id : 15
     * pdc_member_name : zhouzhi123
     * pdc_amount : 9.90
     * pdc_bank_name : 支付宝
     * pdc_bank_no : 13823143261
     * pdc_bank_user : 程磊
     * pdc_add_time : 1460444215
     * pdc_payment_time : 1464835863
     * pdc_payment_state : 1
     * pdc_payment_admin : huaqiaobang
     * pdc_bank_phone : 0
     */

    private Integer id;
    private String pdc_id;
    private String pdc_sn;
    private String pdc_member_id;
    private String pdc_member_name;
    private Double pdc_amount;
    private String pdc_bank_name;
    private String pdc_bank_no;
    private String pdc_bank_user;
    private Long pdc_add_time;
    private Long pdc_payment_time;
    private String pdc_payment_state;
    private String pdc_payment_admin;
    private String pdc_bank_phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPdc_id() {
        return pdc_id;
    }

    public void setPdc_id(String pdc_id) {
        this.pdc_id = pdc_id;
    }

    public String getPdc_sn() {
        return pdc_sn;
    }

    public void setPdc_sn(String pdc_sn) {
        this.pdc_sn = pdc_sn;
    }

    public String getPdc_member_id() {
        return pdc_member_id;
    }

    public void setPdc_member_id(String pdc_member_id) {
        this.pdc_member_id = pdc_member_id;
    }

    public String getPdc_member_name() {
        return pdc_member_name;
    }

    public void setPdc_member_name(String pdc_member_name) {
        this.pdc_member_name = pdc_member_name;
    }

    public Double getPdc_amount() {
        return pdc_amount;
    }

    public void setPdc_amount(Double pdc_amount) {
        this.pdc_amount = pdc_amount;
    }

    public String getPdc_bank_name() {
        return pdc_bank_name;
    }

    public void setPdc_bank_name(String pdc_bank_name) {
        this.pdc_bank_name = pdc_bank_name;
    }

    public String getPdc_bank_no() {
        return pdc_bank_no;
    }

    public void setPdc_bank_no(String pdc_bank_no) {
        this.pdc_bank_no = pdc_bank_no;
    }

    public String getPdc_bank_user() {
        return pdc_bank_user;
    }

    public void setPdc_bank_user(String pdc_bank_user) {
        this.pdc_bank_user = pdc_bank_user;
    }

    public Long getPdc_add_time() {
        return pdc_add_time;
    }

    public void setPdc_add_time(Long pdc_add_time) {
        this.pdc_add_time = pdc_add_time;
    }

    public Long getPdc_payment_time() {
        return pdc_payment_time;
    }

    public void setPdc_payment_time(Long pdc_payment_time) {
        this.pdc_payment_time = pdc_payment_time;
    }

    public String getPdc_payment_state() {
        return pdc_payment_state;
    }

    public void setPdc_payment_state(String pdc_payment_state) {
        this.pdc_payment_state = pdc_payment_state;
    }

    public String getPdc_payment_admin() {
        return pdc_payment_admin;
    }

    public void setPdc_payment_admin(String pdc_payment_admin) {
        this.pdc_payment_admin = pdc_payment_admin;
    }

    public String getPdc_bank_phone() {
        return pdc_bank_phone;
    }

    public void setPdc_bank_phone(String pdc_bank_phone) {
        this.pdc_bank_phone = pdc_bank_phone;
    }
}
