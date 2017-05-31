package com.abcs.huaqiaobang.model;/**
 * Created by Administrator on 2016/11/30.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: zds
 * Date: 2016-11-30
 * Time: 11:37
 * FIXME
 */
public class VIP_CENTER {

    /**
     * code : 200
     * datas : {"id":"1311","member_id":"1333","member_name":"18333615191","total_vip_money":"582.90","level_id":"2","level_name":"精英金卡会员","credit":"0.00","add_time":"1481120348","end_time":"1512656348","money":"582.90","progress":"697.1","introduce":"1、购买会员后立即赠送500购物金，500点会员升级信用点\r\n2、消费享受金卡会员折扣价\r\n3、年尾按年度消费金额2%返还购物金\r\n4、可购买会员特典商品及服务\r\n5、充值金额可累计计入会员升级信用"}
     * introduce_arr : [{"name":"白金卡会员权益","introduce":"1、购买会员后立即赠送1280购物金，1280点会员升级信用点\r\n2、消费享受铂金卡会员折扣价\r\n3、年尾按年度消费金额3%返还购物金\r\n4、可购买会员特典商品及服务\r\n5、充值金额可累计计入会员升级信用\r\n"},{"name":"黑卡会员权益","introduce":"1、购买会员后立即赠送12680购物金，12680点会员升级信用点\r\n2、消费享受黑卡会员折扣价\r\n3、年尾按年度消费金额5%返还购物金\r\n4、可购买会员特典商品及服务\r\n5、充值金额可累计计入会员升级信用\r\n"}]
     */

    @SerializedName("code")
    public int code;
    @SerializedName("datas")
    public DatasBean datas;
    @SerializedName("introduce_arr")
    public List<IntroduceArrBean> introduceArr;

    public static class DatasBean {
        /**
         * id : 1311
         * member_id : 1333
         * member_name : 18333615191
         * total_vip_money : 582.90
         * level_id : 2
         * level_name : 精英金卡会员
         * credit : 0.00
         * add_time : 1481120348
         * end_time : 1512656348
         * money : 582.90
         * progress : 697.1
         * introduce : 1、购买会员后立即赠送500购物金，500点会员升级信用点
         2、消费享受金卡会员折扣价
         3、年尾按年度消费金额2%返还购物金
         4、可购买会员特典商品及服务
         5、充值金额可累计计入会员升级信用
         */

        @SerializedName("id")
        public String id;
        @SerializedName("member_id")
        public String memberId;
        @SerializedName("member_name")
        public String memberName;
        @SerializedName("total_vip_money")
        public String totalVipMoney;
        @SerializedName("level_id")
        public String levelId;
        @SerializedName("level_name")
        public String levelName;
        @SerializedName("credit")
        public String credit;
        @SerializedName("add_time")
        public String addTime;
        @SerializedName("end_time")
        public String endTime;
        @SerializedName("money")
        public String money;
        @SerializedName("progress")
        public String progress;
        @SerializedName("introduce")
        public String introduce;
    }

    public static class IntroduceArrBean {
        /**
         * name : 白金卡会员权益
         * introduce : 1、购买会员后立即赠送1280购物金，1280点会员升级信用点
         2、消费享受铂金卡会员折扣价
         3、年尾按年度消费金额3%返还购物金
         4、可购买会员特典商品及服务
         5、充值金额可累计计入会员升级信用

         */

        @SerializedName("name")
        public String name;
        @SerializedName("introduce")
        public String introduce;
    }
}
