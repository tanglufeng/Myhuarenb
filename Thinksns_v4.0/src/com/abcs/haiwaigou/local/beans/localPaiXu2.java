package com.abcs.haiwaigou.local.beans;

import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.local.beans
 * 作者：zds
 * 时间：2017/3/22 18:07
 */

public class localPaiXu2 {

    public int state;
    public List<DatasEntry> datas;
    public List<TagArr> tagArr;

    public List<TagArr> getTagArr() {
        return tagArr;
    }

    public void setTagArr(List<TagArr> tagArr) {
        this.tagArr = tagArr;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<DatasEntry> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasEntry> datas) {
        this.datas = datas;
    }

    public static class DatasEntry {

        public String goodsLetter;
        public List<HuoHangItem> goodsList;

        public String getGoodsLetter() {
            return goodsLetter;
        }

        public void setGoodsLetter(String goodsLetter) {
            this.goodsLetter = goodsLetter;
        }

        public List<HuoHangItem> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<HuoHangItem> goodsList) {
            this.goodsList = goodsList;
        }
    }
}
