package com.abcs.huaqiaobang.model;

import java.io.Serializable;


/**
 * @author xbw
 * @version 创建时间：2015年10月22日 下午5:03:30
 */
public class Product implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 产品id
     */
    private String id;
    /**
     * 产品名字
     */
    private String name;
    /**
     * 产品收益率
     */
    private double earnings;
    /**
     * 产品说明
     */
    private String desc;
    /**
     * 产品购买期限
     */
    private int numberOfDays;
    /**
     * 产品更新日期
     */
    private long updateDate;
    /**
     * 产品售出数量
     */
    private long soldMoney;
    /**
     * 产品购买人数
     */
    private int buyNum;
    /**
     * 产品图标远程地址
     */
    private String iconUrl;
    /**
     * 起购金额
     */
    private int buyMoney;
    /**
     * 申购费率
     */
    private double buyRate;
    /**
     * 申购节制日期
     */
    private long endBuyDate;
    /**
     * 申购确认日期
     */
    private long confirmBuyDate;
    /**
     * 排序用的
     */
    private int order;
    /**
     * 类型
     */
    private int productType;
    private boolean vip;//是否为vip
    private double overlayEarnings;//vip额外收益率
    private String items;
    private String payway;//支付方式
    private String outway;//转出方式

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getOutway() {
        return outway;
    }

    public void setOutway(String outway) {
        this.outway = outway;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getEarnings() {
        return earnings;
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public long getSoldMoney() {
        return soldMoney;
    }

    public void setSoldMoney(long soldMoney) {
        this.soldMoney = soldMoney;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getBuyMoney() {
        return buyMoney;
    }

    public void setBuyMoney(int buyMoney) {
        this.buyMoney = buyMoney;
    }

    public double getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(double buyRate) {
        this.buyRate = buyRate;
    }

    public long getEndBuyDate() {
        return endBuyDate;
    }

    public void setEndBuyDate(long endBuyDate) {
        this.endBuyDate = endBuyDate;
    }

    public long getConfirmBuyDate() {
        return confirmBuyDate;
    }

    public void setConfirmBuyDate(long confirmBuyDate) {
        this.confirmBuyDate = confirmBuyDate;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public double getOverlayEarnings() {
        return overlayEarnings;
    }

    public void setOverlayEarnings(double overlayEarnings) {
        this.overlayEarnings = overlayEarnings;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

}
