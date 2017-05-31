package com.abcs.haiwaigou.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/3.
 */
public class Orders implements Serializable {
    /**
     * order_list : [{"order_id":"170","order_sn":"8000000000017101","pay_sn":"580510315533518013","store_id":"6","store_name":"文馨礼品店","buyer_id":"13","buyer_name":"zhongjingzhong","buyer_email":"541163600@qq.com","add_time":"1456971533","payment_code":"online","payment_time":"0","finnshed_time":"0","goods_amount":"100.00","order_amount":"100.00","rcb_amount":"0.00","pd_amount":"0.00","shipping_fee":"0.00","evaluation_state":"0","order_state":"10","refund_state":"0","lock_state":"0","delete_state":"0","refund_amount":"0.00","delay_time":"0","order_from":"2","shipping_code":"","state_desc":"待付款","payment_name":"在线付款","extend_order_goods":[{"rec_id":"175","order_id":"170","goods_id":"100155","goods_name":"六胜肽抗皱霜","goods_price":"100.00","goods_num":"1","goods_image":"6_05102447203797537.png","goods_pay_price":"100.00","store_id":"6","buyer_id":"13","goods_type":"1","promotions_id":"0","commis_rate":"0","gc_id":"470","goods_image_url":"http://www.huaqiaobang.com/data/upload/shop/store/goods/6/6_05102447203797537_240.png"}],"if_cancel":true,"if_receive":false,"if_lock":false,"if_deliver":false}]
     * pay_amount : 100
     * add_time : 1456971533
     * pay_sn : 580510315533518013
     */

    private Integer id;
    private String  pay_amount;
    private Long add_time;
    private String pay_sn;

    private boolean isUseVouvher;
    private String order_from;
    private String card_state;

    public String getCard_state() {
        return card_state;
    }

    public void setCard_state(String card_state) {
        this.card_state = card_state;
    }
    public String getOrder_from() {
        return order_from;
    }

    public void setOrder_from(String order_from) {
        this.order_from = order_from;
    }

    public boolean isUseVouvher() {
        return isUseVouvher;
    }

    public void setIsUseVouvher(boolean isUseVouvher) {
        this.isUseVouvher = isUseVouvher;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * order_id : 170
     * order_sn : 8000000000017101
     * pay_sn : 580510315533518013
     * store_id : 6
     * store_name : 文馨礼品店
     * buyer_id : 13
     * buyer_name : zhongjingzhong
     * buyer_email : 541163600@qq.com
     * add_time : 1456971533
     * payment_code : online
     * payment_time : 0
     * finnshed_time : 0
     * goods_amount : 100.00
     * order_amount : 100.00
     * rcb_amount : 0.00
     * pd_amount : 0.00
     * shipping_fee : 0.00
     * evaluation_state : 0
     * order_state : 10
     * refund_state : 0
     * lock_state : 0
     * delete_state : 0
     * refund_amount : 0.00
     * delay_time : 0
     * order_from : 2
     * shipping_code :
     * state_desc : 待付款
     * payment_name : 在线付款
     * extend_order_goods : [{"rec_id":"175","order_id":"170","goods_id":"100155","goods_name":"六胜肽抗皱霜","goods_price":"100.00","goods_num":"1","goods_image":"6_05102447203797537.png","goods_pay_price":"100.00","store_id":"6","buyer_id":"13","goods_type":"1","promotions_id":"0","commis_rate":"0","gc_id":"470","goods_image_url":"http://www.huaqiaobang.com/data/upload/shop/store/goods/6/6_05102447203797537_240.png"}]
     * if_cancel : true
     * if_receive : false
     * if_lock : false
     * if_deliver : false
     */

    private List<OrderListEntity> order_list;

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public void setAdd_time(Long add_time) {
        this.add_time = add_time;
    }

    public void setPay_sn(String pay_sn) {
        this.pay_sn = pay_sn;
    }

    public void setOrder_list(List<OrderListEntity> order_list) {
        this.order_list = order_list;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public Long getAdd_time() {
        return add_time;
    }

    public String getPay_sn() {
        return pay_sn;
    }

    public List<OrderListEntity> getOrder_list() {
        return order_list;
    }

    public static class OrderListEntity {
        private String order_id;
        private String order_sn;
        private String pay_sn;
        private String store_id;
        private String store_name;
        private String buyer_id;
        private String buyer_name;
        private String buyer_email;
        private String add_time;
        private String payment_code;
        private String payment_time;
        private long finnshed_time;
        private String goods_amount;
        private String order_amount;
        private String rcb_amount;
        private String pd_amount;
        private String shipping_fee;
        private String evaluation_state;
        private String order_state;
        private String refund_state;
        private String lock_state;
        private String delete_state;
        private String refund_amount;
        private String delay_time;
        private String order_from;
        private String shipping_code;
        private String state_desc;
        private String payment_name;
        private boolean if_cancel;
        private boolean if_receive;
        private boolean if_lock;
        private boolean if_deliver;
        private boolean if_refund_cancel;
        private boolean if_complain;
        private boolean if_evaluation;
        private boolean if_restore;
        private boolean if_drop;
        private boolean if_delete;
        /**
         * rec_id : 175
         * order_id : 170
         * goods_id : 100155
         * goods_name : 六胜肽抗皱霜
         * goods_price : 100.00
         * goods_num : 1
         * goods_image : 6_05102447203797537.png
         * goods_pay_price : 100.00
         * store_id : 6
         * buyer_id : 13
         * goods_type : 1
         * promotions_id : 0
         * commis_rate : 0
         * gc_id : 470
         * goods_image_url : http://www.huaqiaobang.com/data/upload/shop/store/goods/6/6_05102447203797537_240.png
         */


        private List<ExtendOrderGoodsEntity> extend_order_goods;

        public boolean isIf_refund_cancel() {
            return if_refund_cancel;
        }

        public void setIf_refund_cancel(boolean if_refund_cancel) {
            this.if_refund_cancel = if_refund_cancel;
        }

        public boolean isIf_complain() {
            return if_complain;
        }

        public void setIf_complain(boolean if_complain) {
            this.if_complain = if_complain;
        }

        public boolean isIf_evaluation() {
            return if_evaluation;
        }

        public void setIf_evaluation(boolean if_evaluation) {
            this.if_evaluation = if_evaluation;
        }

        public boolean isIf_restore() {
            return if_restore;
        }

        public void setIf_restore(boolean if_restore) {
            this.if_restore = if_restore;
        }

        public boolean isIf_drop() {
            return if_drop;
        }

        public void setIf_drop(boolean if_drop) {
            this.if_drop = if_drop;
        }

        public boolean isIf_delete() {
            return if_delete;
        }

        public void setIf_delete(boolean if_delete) {
            this.if_delete = if_delete;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public void setPay_sn(String pay_sn) {
            this.pay_sn = pay_sn;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public void setBuyer_id(String buyer_id) {
            this.buyer_id = buyer_id;
        }

        public void setBuyer_name(String buyer_name) {
            this.buyer_name = buyer_name;
        }

        public void setBuyer_email(String buyer_email) {
            this.buyer_email = buyer_email;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public void setPayment_code(String payment_code) {
            this.payment_code = payment_code;
        }

        public void setPayment_time(String payment_time) {
            this.payment_time = payment_time;
        }

        public void setFinnshed_time(long finnshed_time) {
            this.finnshed_time = finnshed_time;
        }

        public void setGoods_amount(String goods_amount) {
            this.goods_amount = goods_amount;
        }

        public void setOrder_amount(String order_amount) {
            this.order_amount = order_amount;
        }

        public void setRcb_amount(String rcb_amount) {
            this.rcb_amount = rcb_amount;
        }

        public void setPd_amount(String pd_amount) {
            this.pd_amount = pd_amount;
        }

        public void setShipping_fee(String shipping_fee) {
            this.shipping_fee = shipping_fee;
        }

        public void setEvaluation_state(String evaluation_state) {
            this.evaluation_state = evaluation_state;
        }

        public void setOrder_state(String order_state) {
            this.order_state = order_state;
        }

        public void setRefund_state(String refund_state) {
            this.refund_state = refund_state;
        }

        public void setLock_state(String lock_state) {
            this.lock_state = lock_state;
        }

        public void setDelete_state(String delete_state) {
            this.delete_state = delete_state;
        }

        public void setRefund_amount(String refund_amount) {
            this.refund_amount = refund_amount;
        }

        public void setDelay_time(String delay_time) {
            this.delay_time = delay_time;
        }

        public void setOrder_from(String order_from) {
            this.order_from = order_from;
        }

        public void setShipping_code(String shipping_code) {
            this.shipping_code = shipping_code;
        }

        public void setState_desc(String state_desc) {
            this.state_desc = state_desc;
        }

        public void setPayment_name(String payment_name) {
            this.payment_name = payment_name;
        }

        public void setIf_cancel(boolean if_cancel) {
            this.if_cancel = if_cancel;
        }

        public void setIf_receive(boolean if_receive) {
            this.if_receive = if_receive;
        }

        public void setIf_lock(boolean if_lock) {
            this.if_lock = if_lock;
        }

        public void setIf_deliver(boolean if_deliver) {
            this.if_deliver = if_deliver;
        }

        public void setExtend_order_goods(List<ExtendOrderGoodsEntity> extend_order_goods) {
            this.extend_order_goods = extend_order_goods;
        }

        public String getOrder_id() {
            return order_id;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public String getPay_sn() {
            return pay_sn;
        }

        public String getStore_id() {
            return store_id;
        }

        public String getStore_name() {
            return store_name;
        }

        public String getBuyer_id() {
            return buyer_id;
        }

        public String getBuyer_name() {
            return buyer_name;
        }

        public String getBuyer_email() {
            return buyer_email;
        }

        public String getAdd_time() {
            return add_time;
        }

        public String getPayment_code() {
            return payment_code;
        }

        public String getPayment_time() {
            return payment_time;
        }

        public long getFinnshed_time() {
            return finnshed_time;
        }

        public String getGoods_amount() {
            return goods_amount;
        }

        public String getOrder_amount() {
            return order_amount;
        }

        public String getRcb_amount() {
            return rcb_amount;
        }

        public String getPd_amount() {
            return pd_amount;
        }

        public String getShipping_fee() {
            return shipping_fee;
        }

        public String getEvaluation_state() {
            return evaluation_state;
        }

        public String getOrder_state() {
            return order_state;
        }

        public String getRefund_state() {
            return refund_state;
        }

        public String getLock_state() {
            return lock_state;
        }

        public String getDelete_state() {
            return delete_state;
        }

        public String getRefund_amount() {
            return refund_amount;
        }

        public String getDelay_time() {
            return delay_time;
        }

        public String getOrder_from() {
            return order_from;
        }

        public String getShipping_code() {
            return shipping_code;
        }

        public String getState_desc() {
            return state_desc;
        }

        public String getPayment_name() {
            return payment_name;
        }

        public boolean isIf_cancel() {
            return if_cancel;
        }

        public boolean isIf_receive() {
            return if_receive;
        }

        public boolean isIf_lock() {
            return if_lock;
        }

        public boolean isIf_deliver() {
            return if_deliver;
        }

        public List<ExtendOrderGoodsEntity> getExtend_order_goods() {
            return extend_order_goods;
        }

        public static class ExtendOrderGoodsEntity {
            private String orgin;
            private String rec_id;
            private String order_id;
            private String goods_id;
            private String goods_name;
            private String goods_price;
            private String goods_num;
            private String goods_image;
            private String goods_pay_price;
            private String store_id;
            private String buyer_id;
            private String goods_type;
            private String promotions_id;
            private String commis_rate;
            private String gc_id;
            private String goods_image_url;
            private String refund;

            public String getOrgin() {
                return orgin;
            }

            public void setOrgin(String orgin) {
                this.orgin = orgin;
            }

            public String getRefund() {
                return refund;
            }

            public void setRefund(String refund) {
                this.refund = refund;
            }

            public void setRec_id(String rec_id) {
                this.rec_id = rec_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public void setGoods_price(String goods_price) {
                this.goods_price = goods_price;
            }

            public void setGoods_num(String goods_num) {
                this.goods_num = goods_num;
            }

            public void setGoods_image(String goods_image) {
                this.goods_image = goods_image;
            }

            public void setGoods_pay_price(String goods_pay_price) {
                this.goods_pay_price = goods_pay_price;
            }

            public void setStore_id(String store_id) {
                this.store_id = store_id;
            }

            public void setBuyer_id(String buyer_id) {
                this.buyer_id = buyer_id;
            }

            public void setGoods_type(String goods_type) {
                this.goods_type = goods_type;
            }

            public void setPromotions_id(String promotions_id) {
                this.promotions_id = promotions_id;
            }

            public void setCommis_rate(String commis_rate) {
                this.commis_rate = commis_rate;
            }

            public void setGc_id(String gc_id) {
                this.gc_id = gc_id;
            }

            public void setGoods_image_url(String goods_image_url) {
                this.goods_image_url = goods_image_url;
            }

            public String getRec_id() {
                return rec_id;
            }

            public String getOrder_id() {
                return order_id;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public String getGoods_price() {
                return goods_price;
            }

            public String getGoods_num() {
                return goods_num;
            }

            public String getGoods_image() {
                return goods_image;
            }

            public String getGoods_pay_price() {
                return goods_pay_price;
            }

            public String getStore_id() {
                return store_id;
            }

            public String getBuyer_id() {
                return buyer_id;
            }

            public String getGoods_type() {
                return goods_type;
            }

            public String getPromotions_id() {
                return promotions_id;
            }

            public String getCommis_rate() {
                return commis_rate;
            }

            public String getGc_id() {
                return gc_id;
            }

            public String getGoods_image_url() {
                return goods_image_url;
            }
        }
    }
}
