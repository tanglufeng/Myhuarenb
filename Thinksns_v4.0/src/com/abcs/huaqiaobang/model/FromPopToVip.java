package com.abcs.huaqiaobang.model;/**
 * Created by Administrator on 2016/12/15.
 */

import com.google.gson.annotations.SerializedName;

/**
 * User: zds
 * Date: 2016-12-15
 * Time: 16:48
 * FIXME
 */
public class FromPopToVip {

    /**
     * state : 1
     * vip_yyg : {"id":"1","level_id":"2","level_name":"精英金卡","old_money":"500.00","new_money":"1.00","sum":8,"start_time":"1481904000","end_time":"1483113600","type":"1","yyg_id":"0","member_id":"0","states":"0","times":"0","descs":"正值年末，为回馈顾客，华侨帮特推出一元抢购限量会员活动，商品均享受比某宝、某东更优惠30%-50%的折扣价。抢购时间为：2016-12-17-00：00：00号到2016-12-31-00：00：00，每天十个名额，等你来哟！"}
     * vip_card : {"id":"2","level_id":"2","level_name":"精英金卡","money":"500.00","continuous_time":"31536000","rebate_percent":"2.00","description":"精英人士最佳选择","dudao":"0","introduce":"1、购买会员后立即赠送500购物金，500点会员升级信用点\r\n2、消费享受金卡会员折扣价\r\n3、年尾按年度消费金额2%返还购物金\r\n4、可购买会员特典商品及服务\r\n5、充值金额可累计计入会员升级信用","notice":"1、用于享受会员特权及商品优惠。\r\n2、限一卡一账户。\r\n3、余额不可转赠。","descs":"正值年末，为回馈顾客，华侨帮特推出一元抢购限量会员活动，商品均享受比某宝、某东更优惠30%-50%的折扣价。抢购时间为：2016-12-17-00：00：00号到2016-12-31-00：00：00，每天十个名额，等你来哟！"}
     * buy_state : 1
     * buy_msg :
     */

    @SerializedName("state")
    public int state;
    @SerializedName("vip_yyg")
    public VipYygBean vipYyg;
    @SerializedName("vip_card")
    public VipCardBean vipCard;
    @SerializedName("buy_state")
    public int buyState;
    @SerializedName("buy_msg")
    public String buyMsg;

    public static class VipYygBean {
        /**
         * id : 1
         * level_id : 2
         * level_name : 精英金卡
         * old_money : 500.00
         * new_money : 1.00
         * sum : 8
         * start_time : 1481904000
         * end_time : 1483113600
         * type : 1
         * yyg_id : 0
         * member_id : 0
         * states : 0
         * times : 0
         * descs : 正值年末，为回馈顾客，华侨帮特推出一元抢购限量会员活动，商品均享受比某宝、某东更优惠30%-50%的折扣价。抢购时间为：2016-12-17-00：00：00号到2016-12-31-00：00：00，每天十个名额，等你来哟！
         */

        @SerializedName("id")
        public String id;
        @SerializedName("level_id")
        public String levelId;
        @SerializedName("level_name")
        public String levelName;
        @SerializedName("old_money")
        public String oldMoney;
        @SerializedName("new_money")
        public String newMoney;
        @SerializedName("sum")
        public int sum;
        @SerializedName("start_time")
        public String startTime;
        @SerializedName("end_time")
        public String endTime;
        @SerializedName("type")
        public String type;
        @SerializedName("yyg_id")
        public String yygId;
        @SerializedName("member_id")
        public String memberId;
        @SerializedName("states")
        public String states;
        @SerializedName("times")
        public String times;
        @SerializedName("descs")
        public String descs;
    }

    public static class VipCardBean {
        /**
         * id : 2
         * level_id : 2
         * level_name : 精英金卡
         * money : 500.00
         * continuous_time : 31536000
         * rebate_percent : 2.00
         * description : 精英人士最佳选择
         * dudao : 0
         * introduce : 1、购买会员后立即赠送500购物金，500点会员升级信用点
         2、消费享受金卡会员折扣价
         3、年尾按年度消费金额2%返还购物金
         4、可购买会员特典商品及服务
         5、充值金额可累计计入会员升级信用
         * notice : 1、用于享受会员特权及商品优惠。
         2、限一卡一账户。
         3、余额不可转赠。
         * descs : 正值年末，为回馈顾客，华侨帮特推出一元抢购限量会员活动，商品均享受比某宝、某东更优惠30%-50%的折扣价。抢购时间为：2016-12-17-00：00：00号到2016-12-31-00：00：00，每天十个名额，等你来哟！
         */

        @SerializedName("id")
        public String id;
        @SerializedName("level_id")
        public String levelId;
        @SerializedName("level_name")
        public String levelName;
        @SerializedName("money")
        public String money;
        @SerializedName("continuous_time")
        public String continuousTime;
        @SerializedName("rebate_percent")
        public String rebatePercent;
        @SerializedName("description")
        public String description;
        @SerializedName("dudao")
        public String dudao;
        @SerializedName("introduce")
        public String introduce;
        @SerializedName("notice")
        public String notice;
        @SerializedName("descs")
        public String descs;
    }
}
