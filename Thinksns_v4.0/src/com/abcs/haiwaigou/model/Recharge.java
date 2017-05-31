package com.abcs.haiwaigou.model;

import java.io.Serializable;

/**
 * Created by zjz on 2016/6/3 0003.
 */
public class Recharge implements Serializable{

    /**
     * id : 302
     * member_id : 15
     * member_name : zhouzhi123
     * type : refund
     * add_time : 1464337635
     * available_amount : 7.90
     * freeze_amount : 0.00
     * description : 确认退款，订单号: 8000000000068401
     */

    private Integer id;
    private String recharge_id;
    private String member_id;
    private String member_name;
    private String type;
    private Long add_time;
    private Double available_amount;
    private Double freeze_amount;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecharge_id() {
        return recharge_id;
    }

    public void setRecharge_id(String recharge_id) {
        this.recharge_id = recharge_id;
    }


    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Long add_time) {
        this.add_time = add_time;
    }

    public Double getAvailable_amount() {
        return available_amount;
    }

    public void setAvailable_amount(Double available_amount) {
        this.available_amount = available_amount;
    }

    public Double getFreeze_amount() {
        return freeze_amount;
    }

    public void setFreeze_amount(Double freeze_amount) {
        this.freeze_amount = freeze_amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
