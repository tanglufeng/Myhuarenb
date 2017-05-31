package com.abcs.haiwaigou.model;

import java.util.List;

/**
 * Created by zjz on 2016/3/25.
 */
public class CheckOrder {

    private List<StoreIdBean> store_list;
    private List<ManSongBean> mansong_list;

    String store_id;

    public List<ManSongBean> getMansong_list() {
        return mansong_list;
    }

    public void setMansong_list(List<ManSongBean> mansong_list) {
        this.mansong_list = mansong_list;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public List<StoreIdBean> getStore_list() {
        return store_list;
    }

    public void setStore_list(List<StoreIdBean> store_list) {
        this.store_list = store_list;
    }


    public static class ManSongBean{
        private String rule_id;
        private String mansong_id;
        private Double price;
        private Double discount;
        private String mansong_goods_name;
        private String goods_id;
        private String mansong_name;
        private String start_time;
        private String end_time;
        private String desc;


        public String getRule_id() {
            return rule_id;
        }

        public void setRule_id(String rule_id) {
            this.rule_id = rule_id;
        }

        public String getMansong_id() {
            return mansong_id;
        }

        public void setMansong_id(String mansong_id) {
            this.mansong_id = mansong_id;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Double getDiscount() {
            return discount;
        }

        public void setDiscount(Double discount) {
            this.discount = discount;
        }

        public String getMansong_goods_name() {
            return mansong_goods_name;
        }

        public void setMansong_goods_name(String mansong_goods_name) {
            this.mansong_goods_name = mansong_goods_name;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getMansong_name() {
            return mansong_name;
        }

        public void setMansong_name(String mansong_name) {
            this.mansong_name = mansong_name;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static class StoreIdBean {
        private String cart_id;
        private String buyer_id;
        private String store_id;
        private String store_name;
        private String goods_id;
        private String goods_name;
        private Double goods_price;
        private String goods_num;
        private String goods_image;
        private String bl_id;
        private boolean state;
        private boolean storage_state;
        private String goods_commonid;
        private String gc_id;
        private String transport_id;
        private Double goods_freight;
        private String goods_vat;
        private String goods_storage;
        private String goods_storage_alarm;
        private String is_fcode;
        private String have_gift;
        private GroupbuyInfoBean groupbuy_info;
        private Object xianshi_info;
        private String upper_limit;
        private String groupbuy_id;
        private String promotions_id;
        private boolean ifgroupbuy;
        private Double goods_total;
        private String goods_image_url;
        private String mansong;

        public String goods_en_name;
        public String goods_serial;
        public String tax_rate;



        public String getMansong() {
            return mansong;
        }

        public void setMansong(String mansong) {
            this.mansong = mansong;
        }

        private boolean isManSong;


        public boolean isManSong() {
            return isManSong;
        }

        public void setIsManSong(boolean isManSong) {
            this.isManSong = isManSong;
        }
        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }

        public String getBuyer_id() {
            return buyer_id;
        }

        public void setBuyer_id(String buyer_id) {
            this.buyer_id = buyer_id;
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

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public Double getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(Double goods_price) {
            this.goods_price = goods_price;
        }

        public String getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(String goods_num) {
            this.goods_num = goods_num;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getBl_id() {
            return bl_id;
        }

        public void setBl_id(String bl_id) {
            this.bl_id = bl_id;
        }

        public boolean isState() {
            return state;
        }

        public void setState(boolean state) {
            this.state = state;
        }

        public boolean isStorage_state() {
            return storage_state;
        }

        public void setStorage_state(boolean storage_state) {
            this.storage_state = storage_state;
        }

        public String getGoods_commonid() {
            return goods_commonid;
        }

        public void setGoods_commonid(String goods_commonid) {
            this.goods_commonid = goods_commonid;
        }

        public String getGc_id() {
            return gc_id;
        }

        public void setGc_id(String gc_id) {
            this.gc_id = gc_id;
        }

        public String getTransport_id() {
            return transport_id;
        }

        public void setTransport_id(String transport_id) {
            this.transport_id = transport_id;
        }

        public Double getGoods_freight() {
            return goods_freight;
        }

        public void setGoods_freight(Double goods_freight) {
            this.goods_freight = goods_freight;
        }

        public String getGoods_vat() {
            return goods_vat;
        }

        public void setGoods_vat(String goods_vat) {
            this.goods_vat = goods_vat;
        }

        public String getGoods_storage() {
            return goods_storage;
        }

        public void setGoods_storage(String goods_storage) {
            this.goods_storage = goods_storage;
        }

        public String getGoods_storage_alarm() {
            return goods_storage_alarm;
        }

        public void setGoods_storage_alarm(String goods_storage_alarm) {
            this.goods_storage_alarm = goods_storage_alarm;
        }

        public String getIs_fcode() {
            return is_fcode;
        }

        public void setIs_fcode(String is_fcode) {
            this.is_fcode = is_fcode;
        }

        public String getHave_gift() {
            return have_gift;
        }

        public void setHave_gift(String have_gift) {
            this.have_gift = have_gift;
        }

        public GroupbuyInfoBean getGroupbuy_info() {
            return groupbuy_info;
        }

        public void setGroupbuy_info(GroupbuyInfoBean groupbuy_info) {
            this.groupbuy_info = groupbuy_info;
        }

        public Object getXianshi_info() {
            return xianshi_info;
        }

        public void setXianshi_info(Object xianshi_info) {
            this.xianshi_info = xianshi_info;
        }

        public String getUpper_limit() {
            return upper_limit;
        }

        public void setUpper_limit(String upper_limit) {
            this.upper_limit = upper_limit;
        }

        public String getGroupbuy_id() {
            return groupbuy_id;
        }

        public void setGroupbuy_id(String groupbuy_id) {
            this.groupbuy_id = groupbuy_id;
        }

        public String getPromotions_id() {
            return promotions_id;
        }

        public void setPromotions_id(String promotions_id) {
            this.promotions_id = promotions_id;
        }

        public boolean isIfgroupbuy() {
            return ifgroupbuy;
        }

        public void setIfgroupbuy(boolean ifgroupbuy) {
            this.ifgroupbuy = ifgroupbuy;
        }

        public Double getGoods_total() {
            return goods_total;
        }

        public void setGoods_total(Double goods_total) {
            this.goods_total = goods_total;
        }

        public String getGoods_image_url() {
            return goods_image_url;
        }

        public void setGoods_image_url(String goods_image_url) {
            this.goods_image_url = goods_image_url;
        }

        public static class GroupbuyInfoBean {
            private String groupbuy_id;
            private String groupbuy_name;
            private String start_time;
            private String end_time;
            private String goods_id;
            private String goods_commonid;
            private String goods_name;
            private String store_id;
            private String store_name;
            private String goods_price;
            private String groupbuy_price;
            private String groupbuy_rebate;
            private String virtual_quantity;
            private String upper_limit;
            private String buyer_count;
            private String buy_quantity;
            private String groupbuy_intro;
            private String state;
            private String recommended;
            private String views;
            private String class_id;
            private String s_class_id;
            private String groupbuy_image;
            private String groupbuy_image1;
            private String remark;
            private String is_vr;
            private Object vr_city_id;
            private Object vr_area_id;
            private Object vr_mall_id;
            private Object vr_class_id;
            private Object vr_s_class_id;
            private String groupbuy_url;
            private String goods_url;
            private String start_time_text;
            private String end_time_text;
            private String groupbuy_state_text;
            private int reviewable;
            private int cancelable;
            private String state_flag;
            private String button_text;
            private String count_down_text;
            private int count_down;

            public String getGroupbuy_id() {
                return groupbuy_id;
            }

            public void setGroupbuy_id(String groupbuy_id) {
                this.groupbuy_id = groupbuy_id;
            }

            public String getGroupbuy_name() {
                return groupbuy_name;
            }

            public void setGroupbuy_name(String groupbuy_name) {
                this.groupbuy_name = groupbuy_name;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getGoods_commonid() {
                return goods_commonid;
            }

            public void setGoods_commonid(String goods_commonid) {
                this.goods_commonid = goods_commonid;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
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

            public String getGoods_price() {
                return goods_price;
            }

            public void setGoods_price(String goods_price) {
                this.goods_price = goods_price;
            }

            public String getGroupbuy_price() {
                return groupbuy_price;
            }

            public void setGroupbuy_price(String groupbuy_price) {
                this.groupbuy_price = groupbuy_price;
            }

            public String getGroupbuy_rebate() {
                return groupbuy_rebate;
            }

            public void setGroupbuy_rebate(String groupbuy_rebate) {
                this.groupbuy_rebate = groupbuy_rebate;
            }

            public String getVirtual_quantity() {
                return virtual_quantity;
            }

            public void setVirtual_quantity(String virtual_quantity) {
                this.virtual_quantity = virtual_quantity;
            }

            public String getUpper_limit() {
                return upper_limit;
            }

            public void setUpper_limit(String upper_limit) {
                this.upper_limit = upper_limit;
            }

            public String getBuyer_count() {
                return buyer_count;
            }

            public void setBuyer_count(String buyer_count) {
                this.buyer_count = buyer_count;
            }

            public String getBuy_quantity() {
                return buy_quantity;
            }

            public void setBuy_quantity(String buy_quantity) {
                this.buy_quantity = buy_quantity;
            }

            public String getGroupbuy_intro() {
                return groupbuy_intro;
            }

            public void setGroupbuy_intro(String groupbuy_intro) {
                this.groupbuy_intro = groupbuy_intro;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getRecommended() {
                return recommended;
            }

            public void setRecommended(String recommended) {
                this.recommended = recommended;
            }

            public String getViews() {
                return views;
            }

            public void setViews(String views) {
                this.views = views;
            }

            public String getClass_id() {
                return class_id;
            }

            public void setClass_id(String class_id) {
                this.class_id = class_id;
            }

            public String getS_class_id() {
                return s_class_id;
            }

            public void setS_class_id(String s_class_id) {
                this.s_class_id = s_class_id;
            }

            public String getGroupbuy_image() {
                return groupbuy_image;
            }

            public void setGroupbuy_image(String groupbuy_image) {
                this.groupbuy_image = groupbuy_image;
            }

            public String getGroupbuy_image1() {
                return groupbuy_image1;
            }

            public void setGroupbuy_image1(String groupbuy_image1) {
                this.groupbuy_image1 = groupbuy_image1;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getIs_vr() {
                return is_vr;
            }

            public void setIs_vr(String is_vr) {
                this.is_vr = is_vr;
            }

            public Object getVr_city_id() {
                return vr_city_id;
            }

            public void setVr_city_id(Object vr_city_id) {
                this.vr_city_id = vr_city_id;
            }

            public Object getVr_area_id() {
                return vr_area_id;
            }

            public void setVr_area_id(Object vr_area_id) {
                this.vr_area_id = vr_area_id;
            }

            public Object getVr_mall_id() {
                return vr_mall_id;
            }

            public void setVr_mall_id(Object vr_mall_id) {
                this.vr_mall_id = vr_mall_id;
            }

            public Object getVr_class_id() {
                return vr_class_id;
            }

            public void setVr_class_id(Object vr_class_id) {
                this.vr_class_id = vr_class_id;
            }

            public Object getVr_s_class_id() {
                return vr_s_class_id;
            }

            public void setVr_s_class_id(Object vr_s_class_id) {
                this.vr_s_class_id = vr_s_class_id;
            }

            public String getGroupbuy_url() {
                return groupbuy_url;
            }

            public void setGroupbuy_url(String groupbuy_url) {
                this.groupbuy_url = groupbuy_url;
            }

            public String getGoods_url() {
                return goods_url;
            }

            public void setGoods_url(String goods_url) {
                this.goods_url = goods_url;
            }

            public String getStart_time_text() {
                return start_time_text;
            }

            public void setStart_time_text(String start_time_text) {
                this.start_time_text = start_time_text;
            }

            public String getEnd_time_text() {
                return end_time_text;
            }

            public void setEnd_time_text(String end_time_text) {
                this.end_time_text = end_time_text;
            }

            public String getGroupbuy_state_text() {
                return groupbuy_state_text;
            }

            public void setGroupbuy_state_text(String groupbuy_state_text) {
                this.groupbuy_state_text = groupbuy_state_text;
            }

            public int getReviewable() {
                return reviewable;
            }

            public void setReviewable(int reviewable) {
                this.reviewable = reviewable;
            }

            public int getCancelable() {
                return cancelable;
            }

            public void setCancelable(int cancelable) {
                this.cancelable = cancelable;
            }

            public String getState_flag() {
                return state_flag;
            }

            public void setState_flag(String state_flag) {
                this.state_flag = state_flag;
            }

            public String getButton_text() {
                return button_text;
            }

            public void setButton_text(String button_text) {
                this.button_text = button_text;
            }

            public String getCount_down_text() {
                return count_down_text;
            }

            public void setCount_down_text(String count_down_text) {
                this.count_down_text = count_down_text;
            }

            public int getCount_down() {
                return count_down;
            }

            public void setCount_down(int count_down) {
                this.count_down = count_down;
            }
        }
    }
}
