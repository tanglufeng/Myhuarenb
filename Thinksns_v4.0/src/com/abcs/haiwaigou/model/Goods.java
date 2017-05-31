package com.abcs.haiwaigou.model;

import java.io.Serializable;

/**
 * Created by zjz on 2016/1/12.
 */
public class Goods implements Serializable{
    private Integer id;
    private Integer sid;
    private Integer cateid;
    private Integer brandid;
    private String title;
    private String subhead;
    private String good_en_name;
    private String goods_serial;

    public String getGoods_serial() {
        return goods_serial;
    }

    public void setGoods_serial(String goods_serial) {
        this.goods_serial = goods_serial;
    }

    public String getGood_en_name() {
        return good_en_name;
    }

    public void setGood_en_name(String good_en_name) {
        this.good_en_name = good_en_name;
    }

    private double money;  /****促销价*****/
    private double goods_markeprice; /****市场价*****/
    private double promote_money; /****原价*****/


    private double dismoney;
    private String picarr;
    private String manyId;
    private String otype;

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    private String content;//商品详情链接
    private Integer pos;
    private Integer renqi;
    private long time;
    private String cid;
    private Integer store;
    private Integer sy_store;
    private Integer quota;
    private Integer quota_num;
    private String shopser;
    private String hint;
    private String shoppar;//商品参数链接
    private Integer shopid;
    private Integer recommend;

    private int goods_num;
    private String goods_id;
    private String cart_id;
    private String gc_id;
    private String goods_salenum;
    private String fav_id;
    private String store_id;
    private String store_name;
    private String store_goods_total;

    private String pay_amount;
    private Long order_time;
    private String pay_sn;
    private String order_id;
    private String order_sn;
    private String buyer_id;
    private String buyer_name;
    private String buyer_email;
    private String state_desc;
    private String goods_url;
    private Integer goods_storage;


    private boolean if_cancel;
    private boolean if_recerive;
    private boolean if_lock;
    private boolean if_deliver;

    private String deliver_code;
    private String express_name;
    private String goods_state;
    private String goods_bl_state;
    private String type;
    private String rule_id;

    ////////////
    private double jk_price;
    private double bjk_price;
    private double hk_price;

    private String goods_promotion_price;
    private String goods_promotion_type;


    ////////////////


    public double getGoods_markeprice() {
        return goods_markeprice;
    }

    public void setGoods_markeprice(double goods_markeprice) {
        this.goods_markeprice = goods_markeprice;
    }

    public String getManyId() {
        return manyId;
    }

    public void setManyId(String manyId) {
        this.manyId = manyId;
    }

    public String getGoods_promotion_price() {
        return goods_promotion_price;
    }

    public void setGoods_promotion_price(String goods_promotion_price) {
        this.goods_promotion_price = goods_promotion_price;
    }

    public String getGoods_promotion_type() {
        return goods_promotion_type;
    }

    public void setGoods_promotion_type(String goods_promotion_type) {
        this.goods_promotion_type = goods_promotion_type;
    }

    public double getJk_price() {
        return jk_price;
    }

    public void setJk_price(double jk_price) {
        this.jk_price = jk_price;
    }

    public double getBjk_price() {
        return bjk_price;
    }

    public void setBjk_price(double bjk_price) {
        this.bjk_price = bjk_price;
    }

    public double getHk_price() {
        return hk_price;
    }

    public void setHk_price(double hk_price) {
        this.hk_price = hk_price;
    }

    //一元云购
    private String title_style;
    private String title2;
    private String keywords;
    private String description;
    private double yunjiage;
    private int zongrenshu;
    private int canyurenshu;
    private int shenyurenshu;
    private int def_renshu;
    private int qishu;
    private short maxqishu;
    private String thumb;
    private String codes_table;
    private int xsjx_time;
    private int order;
    private String q_uid;
    private String q_user;
    private String q_user_code;
    private String q_content;
    private String q_counttime;
    private Long q_end_time;
    private Long q_showtime;
    private int ex_y;
    private int is_share;
    private int xiangou;
    private int LayoutType;
    private String total_money;
    private long q_num;


    public String getRule_id() {
        return rule_id;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGoods_state() {
        return goods_state;
    }

    public void setGoods_state(String goods_state) {
        this.goods_state = goods_state;
    }

    public String getGoods_bl_state() {
        return goods_bl_state;
    }

    public void setGoods_bl_state(String goods_bl_state) {
        this.goods_bl_state = goods_bl_state;
    }

    public Integer getGoods_storage() {
        return goods_storage;
    }

    public void setGoods_storage(Integer goods_storage) {
        this.goods_storage = goods_storage;
    }

    public String getDeliver_code() {
        return deliver_code;
    }

    public void setDeliver_code(String deliver_code) {
        this.deliver_code = deliver_code;
    }

    public String getExpress_name() {
        return express_name;
    }

    public void setExpress_name(String express_name) {
        this.express_name = express_name;
    }

    public String getGoods_url() {
        return goods_url;
    }

    public void setGoods_url(String goods_url) {
        this.goods_url = goods_url;
    }

    public double getPromote_money() {
        return promote_money;
    }

    public void setPromote_money(double promote_money) {
        this.promote_money = promote_money;
    }

    public boolean isIf_cancel() {
        return if_cancel;
    }

    public void setIf_cancel(boolean if_cancel) {
        this.if_cancel = if_cancel;
    }

    public boolean isIf_recerive() {
        return if_recerive;
    }

    public void setIf_recerive(boolean if_recerive) {
        this.if_recerive = if_recerive;
    }

    public boolean isIf_lock() {
        return if_lock;
    }

    public void setIf_lock(boolean if_lock) {
        this.if_lock = if_lock;
    }

    public boolean isIf_deliver() {
        return if_deliver;
    }

    public void setIf_deliver(boolean if_deliver) {
        this.if_deliver = if_deliver;
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

    public String getBuyer_email() {
        return buyer_email;
    }

    public void setBuyer_email(String buyer_email) {
        this.buyer_email = buyer_email;
    }

    public String getState_desc() {
        return state_desc;
    }

    public void setState_desc(String state_desc) {
        this.state_desc = state_desc;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public Long getOrder_time() {
        return order_time;
    }

    public void setOrder_time(Long order_time) {
        this.order_time = order_time;
    }

    public String getPay_sn() {
        return pay_sn;
    }

    public void setPay_sn(String pay_sn) {
        this.pay_sn = pay_sn;
    }

    public String getStore_goods_total() {
        return store_goods_total;
    }

    public void setStore_goods_total(String store_goods_total) {
        this.store_goods_total = store_goods_total;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getFav_id() {
        return fav_id;
    }

    public void setFav_id(String fav_id) {
        this.fav_id = fav_id;
    }

    public String getTitle_style() {
        return title_style;
    }

    public void setTitle_style(String title_style) {
        this.title_style = title_style;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getYunjiage() {
        return yunjiage;
    }

    public void setYunjiage(double yunjiage) {
        this.yunjiage = yunjiage;
    }

    public int getZongrenshu() {
        return zongrenshu;
    }

    public void setZongrenshu(int zongrenshu) {
        this.zongrenshu = zongrenshu;
    }

    public int getCanyurenshu() {
        return canyurenshu;
    }

    public void setCanyurenshu(int canyurenshu) {
        this.canyurenshu = canyurenshu;
    }

    public int getShenyurenshu() {
        return shenyurenshu;
    }

    public void setShenyurenshu(int shenyurenshu) {
        this.shenyurenshu = shenyurenshu;
    }

    public int getDef_renshu() {
        return def_renshu;
    }

    public void setDef_renshu(int def_renshu) {
        this.def_renshu = def_renshu;
    }

    public int getQishu() {
        return qishu;
    }

    public void setQishu(int qishu) {
        this.qishu = qishu;
    }

    public short getMaxqishu() {
        return maxqishu;
    }

    public void setMaxqishu(short maxqishu) {
        this.maxqishu = maxqishu;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getCodes_table() {
        return codes_table;
    }

    public void setCodes_table(String codes_table) {
        this.codes_table = codes_table;
    }

    public int getXsjx_time() {
        return xsjx_time;
    }

    public void setXsjx_time(int xsjx_time) {
        this.xsjx_time = xsjx_time;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getQ_uid() {
        return q_uid;
    }

    public void setQ_uid(String q_uid) {
        this.q_uid = q_uid;
    }

    public String getQ_user() {
        return q_user;
    }

    public void setQ_user(String q_user) {
        this.q_user = q_user;
    }

    public String getQ_user_code() {
        return q_user_code;
    }

    public void setQ_user_code(String q_user_code) {
        this.q_user_code = q_user_code;
    }

    public String getQ_content() {
        return q_content;
    }

    public void setQ_content(String q_content) {
        this.q_content = q_content;
    }

    public String getQ_counttime() {
        return q_counttime;
    }

    public void setQ_counttime(String q_counttime) {
        this.q_counttime = q_counttime;
    }

    public Long getQ_end_time() {
        return q_end_time;
    }

    public void setQ_end_time(Long q_end_time) {
        this.q_end_time = q_end_time;
    }

    public Long getQ_showtime() {
        return q_showtime;
    }

    public void setQ_showtime(Long q_showtime) {
        this.q_showtime = q_showtime;
    }

    public int getEx_y() {
        return ex_y;
    }

    public void setEx_y(int ex_y) {
        this.ex_y = ex_y;
    }

    public int getIs_share() {
        return is_share;
    }

    public void setIs_share(int is_share) {
        this.is_share = is_share;
    }

    public int getXiangou() {
        return xiangou;
    }

    public void setXiangou(int xiangou) {
        this.xiangou = xiangou;
    }

    public int getLayoutType() {
        return LayoutType;
    }

    public void setLayoutType(int layoutType) {
        LayoutType = layoutType;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public long getQ_num() {
        return q_num;
    }

    public void setQ_num(long q_num) {
        this.q_num = q_num;
    }

    public String getGoods_salenum() {
        return goods_salenum;
    }

    public void setGoods_salenum(String goods_salenum) {
        this.goods_salenum = goods_salenum;
    }

    public String getGc_id() {
        return gc_id;
    }

    public void setGc_id(String gc_id) {
        this.gc_id = gc_id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public Integer getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getSid() {
        return sid;
    }
    public void setSid(Integer sid) {
        this.sid = sid;
    }
    public Integer getCateid() {
        return cateid;
    }
    public void setCateid(Integer cateid) {
        this.cateid = cateid;
    }
    public Integer getBrandid() {
        return brandid;
    }
    public void setBrandid(Integer brandid) {
        this.brandid = brandid;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubhead() {
        return subhead;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }

    public double getMoney() {
        return money;
    }
    public void setMoney(double money) {
        this.money = money;
    }
    public double getDismoney() {
        return dismoney;
    }
    public void setDismoney(double dismoney) {
        this.dismoney = dismoney;
    }
    public String getPicarr() {
        return picarr;
    }
    public void setPicarr(String picarr) {
        this.picarr = picarr;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Integer getPos() {
        return pos;
    }
    public void setPos(Integer pos) {
        this.pos = pos;
    }
    public Integer getRenqi() {
        return renqi;
    }
    public void setRenqi(Integer renqi) {
        this.renqi = renqi;
    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getCid() {
        return cid;
    }
    public void setCid(String cid) {
        this.cid = cid;
    }
    public Integer getStore() {
        return store;
    }
    public void setStore(Integer store) {
        this.store = store;
    }
    public Integer getSy_store() {
        return sy_store;
    }
    public void setSy_store(Integer sy_store) {
        this.sy_store = sy_store;
    }
    public Integer getQuota() {
        return quota;
    }
    public void setQuota(Integer quota) {
        this.quota = quota;
    }
    public Integer getQuota_num() {
        return quota_num;
    }
    public void setQuota_num(Integer quota_num) {
        this.quota_num = quota_num;
    }
    public String getShopser() {
        return shopser;
    }
    public void setShopser(String shopser) {
        this.shopser = shopser;
    }
    public String getHint() {
        return hint;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }
    public String getShoppar() {
        return shoppar;
    }
    public void setShoppar(String shoppar) {
        this.shoppar = shoppar;
    }
    public Integer getShopid() {
        return shopid;
    }
    public void setShopid(Integer shopid) {
        this.shopid = shopid;
    }
    public Integer getRecommend() {
        return recommend;
    }
    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", sid=" + sid +
                ", cateid=" + cateid +
                ", brandid=" + brandid +
                ", title='" + title + '\'' +
                ", subhead='" + subhead + '\'' +
                ", money=" + money +
                ", dismoney=" + dismoney +
                ", picarr='" + picarr + '\'' +
                ", content='" + content + '\'' +
                ", pos=" + pos +
                ", renqi=" + renqi +
                ", time=" + time +
                ", cid='" + cid + '\'' +
                ", store=" + store +
                ", sy_store=" + sy_store +
                ", quota=" + quota +
                ", quota_num=" + quota_num +
                ", shopser='" + shopser + '\'' +
                ", hint='" + hint + '\'' +
                ", shoppar='" + shoppar + '\'' +
                ", shopid=" + shopid +
                ", recommend=" + recommend +
                ", goods_num=" + goods_num +
                ", goods_id='" + goods_id + '\'' +
                ", cart_id='" + cart_id + '\'' +
                ", gc_id='" + gc_id + '\'' +
                ", goods_salenum='" + goods_salenum + '\'' +
                ", fav_id='" + fav_id + '\'' +
                ", store_id='" + store_id + '\'' +
                ", store_name='" + store_name + '\'' +
                ", store_goods_total='" + store_goods_total + '\'' +
                ", pay_amount='" + pay_amount + '\'' +
                ", order_time='" + order_time + '\'' +
                ", pay_sn='" + pay_sn + '\'' +
                ", order_id='" + order_id + '\'' +
                ", order_sn='" + order_sn + '\'' +
                ", buyer_id='" + buyer_id + '\'' +
                ", buyer_name='" + buyer_name + '\'' +
                ", buyer_email='" + buyer_email + '\'' +
                ", state_desc='" + state_desc + '\'' +
                ", title_style='" + title_style + '\'' +
                ", title2='" + title2 + '\'' +
                ", keywords='" + keywords + '\'' +
                ", description='" + description + '\'' +
                ", yunjiage=" + yunjiage +
                ", zongrenshu=" + zongrenshu +
                ", canyurenshu=" + canyurenshu +
                ", shenyurenshu=" + shenyurenshu +
                ", def_renshu=" + def_renshu +
                ", qishu=" + qishu +
                ", maxqishu=" + maxqishu +
                ", thumb='" + thumb + '\'' +
                ", codes_table='" + codes_table + '\'' +
                ", xsjx_time=" + xsjx_time +
                ", order=" + order +
                ", q_uid='" + q_uid + '\'' +
                ", q_user='" + q_user + '\'' +
                ", q_user_code='" + q_user_code + '\'' +
                ", q_content='" + q_content + '\'' +
                ", q_counttime='" + q_counttime + '\'' +
                ", q_end_time=" + q_end_time +
                ", q_showtime=" + q_showtime +
                ", ex_y=" + ex_y +
                ", is_share=" + is_share +
                ", xiangou=" + xiangou +
                ", LayoutType=" + LayoutType +
                ", total_money='" + total_money + '\'' +
                ", q_num=" + q_num +
                '}';
    }
}
