package com.abcs.haiwaigou.model;

import java.io.Serializable;

/**
 * Created by zjz on 2016/4/1.
 */
public class Refund implements Serializable{

    /**
     * refund_id : 22
     * order_id : 277
     * order_sn : 8000000000028202
     * refund_sn : 867103160331193822
     * store_id : 3
     * store_name : 海岛小王子
     * buyer_id : 15
     * buyer_name : zhouzhi123
     * goods_id : 0
     * order_goods_id : 0
     * goods_name : 订单商品全部退款
     * goods_num : 1
     * refund_amount : 0.01
     * goods_image : null
     * order_goods_type : 1
     * refund_type : 1
     * seller_state : 1
     * refund_state : 1
     * return_type : 1
     * order_lock : 2
     * goods_state : 1
     * add_time : 1459424302
     * seller_time : 0
     * admin_time : 0
     * reason_id : 0
     * reason_info : 取消订单，全部退款
     * pic_info : a:1:{s:5:"buyer";a:1:{i:1;s:21:"05127683025492190.png";}}
     * buyer_message : 不好
     * seller_message : null
     * admin_message : null
     * express_id : 0
     * invoice_no : null
     * ship_time : 0
     * delay_time : 0
     * receive_time : 0
     * receive_message : null
     * commis_rate : 0
     */

    private Integer id;
    private String refund_id;
    private String order_id;
    private String order_sn;
    private String refund_sn;
    private String store_id;
    private String store_name;
    private String buyer_id;
    private String buyer_name;
    private String goods_id;
    private String order_goods_id;
    private String goods_name;
    private String goods_num;
    private String refund_amount;
    private String goods_image;
    private String order_goods_type;
    private String refund_type;
    private String seller_state;
    private String refund_state;
    private String return_type;
    private String order_lock;
    private String goods_state;
    private Long add_time;
    private String seller_time;
    private Long admin_time;
    private String reason_id;
    private String reason_info;
    private String pic_info;
    private String buyer_message;
    private String seller_message;
    private String admin_message;
    private String express_id;
    private String invoice_no;
    private String ship_time;
    private String delay_time;
    private String receive_time;
    private String receive_message;
    private String commis_rate;
    private String return_e_name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getRefund_sn() {
        return refund_sn;
    }

    public void setRefund_sn(String refund_sn) {
        this.refund_sn = refund_sn;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getOrder_goods_id() {
        return order_goods_id;
    }

    public void setOrder_goods_id(String order_goods_id) {
        this.order_goods_id = order_goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    public String getOrder_goods_type() {
        return order_goods_type;
    }

    public void setOrder_goods_type(String order_goods_type) {
        this.order_goods_type = order_goods_type;
    }

    public String getRefund_type() {
        return refund_type;
    }

    public void setRefund_type(String refund_type) {
        this.refund_type = refund_type;
    }

    public String getSeller_state() {
        return seller_state;
    }

    public void setSeller_state(String seller_state) {
        this.seller_state = seller_state;
    }

    public String getRefund_state() {
        return refund_state;
    }

    public void setRefund_state(String refund_state) {
        this.refund_state = refund_state;
    }

    public String getReturn_type() {
        return return_type;
    }

    public void setReturn_type(String return_type) {
        this.return_type = return_type;
    }

    public String getOrder_lock() {
        return order_lock;
    }

    public void setOrder_lock(String order_lock) {
        this.order_lock = order_lock;
    }

    public String getGoods_state() {
        return goods_state;
    }

    public void setGoods_state(String goods_state) {
        this.goods_state = goods_state;
    }

    public Long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Long add_time) {
        this.add_time = add_time;
    }

    public String getSeller_time() {
        return seller_time;
    }

    public void setSeller_time(String seller_time) {
        this.seller_time = seller_time;
    }

    public Long getAdmin_time() {
        return admin_time;
    }

    public void setAdmin_time(Long admin_time) {
        this.admin_time = admin_time;
    }

    public String getReason_id() {
        return reason_id;
    }

    public void setReason_id(String reason_id) {
        this.reason_id = reason_id;
    }

    public String getReason_info() {
        return reason_info;
    }

    public void setReason_info(String reason_info) {
        this.reason_info = reason_info;
    }

    public String getPic_info() {
        return pic_info;
    }

    public void setPic_info(String pic_info) {
        this.pic_info = pic_info;
    }

    public String getBuyer_message() {
        return buyer_message;
    }

    public void setBuyer_message(String buyer_message) {
        this.buyer_message = buyer_message;
    }

    public String getSeller_message() {
        return seller_message;
    }

    public void setSeller_message(String seller_message) {
        this.seller_message = seller_message;
    }

    public String getAdmin_message() {
        return admin_message;
    }

    public void setAdmin_message(String admin_message) {
        this.admin_message = admin_message;
    }

    public String getExpress_id() {
        return express_id;
    }

    public void setExpress_id(String express_id) {
        this.express_id = express_id;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getShip_time() {
        return ship_time;
    }

    public void setShip_time(String ship_time) {
        this.ship_time = ship_time;
    }

    public String getDelay_time() {
        return delay_time;
    }

    public void setDelay_time(String delay_time) {
        this.delay_time = delay_time;
    }

    public String getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(String receive_time) {
        this.receive_time = receive_time;
    }

    public String getReceive_message() {
        return receive_message;
    }

    public void setReceive_message(String receive_message) {
        this.receive_message = receive_message;
    }

    public String getCommis_rate() {
        return commis_rate;
    }

    public void setCommis_rate(String commis_rate) {
        this.commis_rate = commis_rate;
    }

    public String getReturn_e_name() {
        return return_e_name;
    }

    public void setReturn_e_name(String return_e_name) {
        this.return_e_name = return_e_name;
    }
}
