package com.abcs.haiwaigou.local.beans;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by zjz on 2016/8/31.
 */
public class LocalBean  {


    private List<MenuBean> menu;

    private List<NewestInfoBean> newestInfo;

    public List<MenuBean> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuBean> menu) {
        this.menu = menu;
    }

    public List<NewestInfoBean> getNewestInfo() {
        return newestInfo;
    }

    public void setNewestInfo(List<NewestInfoBean> newestInfo) {
        this.newestInfo = newestInfo;
    }

    public static class MenuBean {
        private String imgUrl;
        private int menuParentId;
        private int menuId;
        private String menuName;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getMenuParentId() {
            return menuParentId;
        }

        public void setMenuParentId(int menuParentId) {
            this.menuParentId = menuParentId;
        }

        public int getMenuId() {
            return menuId;
        }

        public void setMenuId(int menuId) {
            this.menuId = menuId;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }
    }

    public static class NewestInfoBean {
        private int id;
        private long createTime;
        private String title;
        private String cityName;
        private String country;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }
}
