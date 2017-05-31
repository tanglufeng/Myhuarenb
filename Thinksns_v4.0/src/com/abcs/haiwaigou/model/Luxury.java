package com.abcs.haiwaigou.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zjz on 2016/9/13.
 */
public class Luxury implements Serializable{




    private ImgBean imgBean;
    private List<DatasBean> datas;

    private String id;
    private Object brand_id;
    private String luxury_id;
    private String type;
    private String context;
    private Object sort;
    private String imgUrl;
    private String flag;
    private String desc;
    private String advert;
    private String title;

    public String getAdvert() {
        return advert;
    }

    public void setAdvert(String advert) {
        this.advert = advert;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ImgBean getImgBean() {
        return imgBean;
    }

    public void setImgBean(ImgBean img) {
        this.imgBean = img;
    }

    public List<DatasBean> getDatasBean() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Object brand_id) {
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

    public Object getSort() {
        return sort;
    }

    public void setSort(Object sort) {
        this.sort = sort;
    }

    public static class ImgBean {
        private String id;
        private String luxury_id;
        private String img;
        private String desc;
        private String flag;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLuxury_id() {
            return luxury_id;
        }

        public void setLuxury_id(String luxury_id) {
            this.luxury_id = luxury_id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }

    public static class DatasBean {
        private String id;
        private String brand_id;
        private String  luxury_id;
        private String type;
        private String img;
        private String context;
        private String sort;
        private String  desc;
        private String flag;
        private String title;
        private String advert;

        public String getAdvert() {
            return advert;
        }

        public void setAdvert(String advert) {
            this.advert = advert;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

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

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }


    }
}
