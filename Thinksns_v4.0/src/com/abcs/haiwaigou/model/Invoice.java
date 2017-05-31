package com.abcs.haiwaigou.model;

import java.io.Serializable;

/**
 * Created by zjz on 2016/3/25.
 */
public class Invoice implements Serializable {
    String inv_id;
    String inv_title;
    String inv_content;

    public String getInv_id() {
        return inv_id;
    }

    public void setInv_id(String inv_id) {
        this.inv_id = inv_id;
    }

    public String getInv_title() {
        return inv_title;
    }

    public void setInv_title(String inv_title) {
        this.inv_title = inv_title;
    }

    public String getInv_content() {
        return inv_content;
    }

    public void setInv_content(String inv_content) {
        this.inv_content = inv_content;
    }
}
