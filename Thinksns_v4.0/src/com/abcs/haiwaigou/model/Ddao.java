package com.abcs.haiwaigou.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuke on 2017/19/13.
 */
public class Ddao implements Serializable{

    /**
     * cn_title : 独道之旅
     * en_title : Private Trips
     * datas : [{"id":"80","brand_id":"1","luxury_id":"0","type":"3","context":"null","sort":"1","img":"s8_5b3a2887a56cdbf2a14149ec6cee02c2.jpg","flag":"4","descs":"62","title":"私人高尔夫之旅","advert":"null","belong":"1"},{"id":"81","brand_id":"1","luxury_id":null,"type":"3","context":null,"sort":"2","img":"s8_07ee91666ad26646b03c132cf3616f04.jpg","flag":"3","descs":"101667","title":null,"advert":null,"belong":"1"},{"id":"82","brand_id":"1","luxury_id":null,"type":"3","context":null,"sort":"2","img":"s8_675ce5969c08b34f9d2e505a8746b2b2.jpg","flag":"3","descs":"101666","title":null,"advert":null,"belong":"1"},{"id":"83","brand_id":"1","luxury_id":"0","type":"3","context":"null","sort":"2","img":"s8_33580a61ee446c6f6e35d16345aea183.jpg","flag":"3","descs":"101363","title":"null","advert":"null","belong":"1"}]
     */

    private String cn_title;
    private String en_title;
    private List<DatasBean> datas;

    public String getCn_title() {
        return cn_title;
    }

    public void setCn_title(String cn_title) {
        this.cn_title = cn_title;
    }

    public String getEn_title() {
        return en_title;
    }

    public void setEn_title(String en_title) {
        this.en_title = en_title;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * id : 80
         * brand_id : 1
         * luxury_id : 0
         * type : 3
         * context : null
         * sort : 1
         * img : s8_5b3a2887a56cdbf2a14149ec6cee02c2.jpg
         * flag : 4
         * descs : 62
         * title : 私人高尔夫之旅
         * advert : null
         * belong : 1
         */

        private String id;
        private String brand_id;
        private String luxury_id;
        private String type;
        private String context;
        private String sort;
        private String img;
        private String flag;
        private String descs;
        private String title;
        private String advert;
        private String belong;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public String getLuxury_id() {
            return luxury_id;
        }

        public void setLuxury_id(String luxury_id) {
            this.luxury_id = luxury_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getDescs() {
            return descs;
        }

        public void setDescs(String descs) {
            this.descs = descs;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAdvert() {
            return advert;
        }

        public void setAdvert(String advert) {
            this.advert = advert;
        }

        public String getBelong() {
            return belong;
        }

        public void setBelong(String belong) {
            this.belong = belong;
        }
    }
}
