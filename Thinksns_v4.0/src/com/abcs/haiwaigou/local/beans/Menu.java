package com.abcs.haiwaigou.local.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zjz on 2016/8/31.
 */
public class Menu implements Parcelable{
/*    "imgUrl": "http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb_abc_img%2F%E6%9C%AC%E5%9C%B0%E9%85%8D%E9%80%81%402x1484217268589.png",
            "menuParentId": -1,
            "is_show": 1,
            "menuId": 90,
            "is_click": 0,
            "menuName": "货行订货"*/

    private String imgUrl;
    private int menuParentId;
    private String  menuId;
    private String menuName;
    private int is_show;
    private int is_click;

    public int getIs_click() {
        return is_click;
    }

    public void setIs_click(int is_click) {
        this.is_click = is_click;
    }

    public int getIs_show() {
        return is_show;
    }

    public void setIs_show(int is_show) {
        this.is_show = is_show;
    }

    public Menu() {
    }

    protected Menu(Parcel in) {
        imgUrl = in.readString();
        menuParentId = in.readInt();
        menuId = in.readString();
        menuName = in.readString();
        is_show = in.readInt();
        is_click = in.readInt();
    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };

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

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
        dest.writeInt(menuParentId);
        dest.writeString(menuId);
        dest.writeString(menuName);
        dest.writeInt(is_show);
        dest.writeInt(is_click);
    }
}
