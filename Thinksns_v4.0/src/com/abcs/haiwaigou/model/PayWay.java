package com.abcs.haiwaigou.model;

import java.io.Serializable;

/**
 * Created by zjz on 2016/3/3.
 */
public class PayWay implements Serializable {
    String payment_code;
    String payment_name;

    public String getPayment_name() {
        return payment_name;
    }

    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }

    public String getPayment_code() {
        return payment_code;
    }

    public void setPayment_code(String payment_code) {
        this.payment_code = payment_code;
    }

    @Override
    public String toString() {
        return "PayWay{" +
                "payment_code='" + payment_code + '\'' +
                ", payment_name='" + payment_name + '\'' +
                '}';
    }
}
