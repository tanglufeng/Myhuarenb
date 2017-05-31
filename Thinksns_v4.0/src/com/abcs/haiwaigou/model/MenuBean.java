package com.abcs.haiwaigou.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：com.abcs.haiwaigou.model
 * 作者：zds
 * 时间：2017/3/20 19:41
 */

public class MenuBean implements Parcelable {
    /**
     * imgUrl : http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb_abc_img/%E6%9C%AA%E6%A0%87%E9%A2%98-111489741189730.png
     * menuParentId : -1
     * is_show : 1
     * menuId : 90
     * is_click : 0
     * type : 0
     * menuName : 货行
     */

    @SerializedName("imgUrl")
    public String imgUrl;
    @SerializedName("menuParentId")
    public int menuParentId;
    @SerializedName("is_show")
    public int isShow;
    @SerializedName("menuId")
    public int menuId;
    @SerializedName("is_click")
    public int isClick;
    @SerializedName("type")
    public int type;
    @SerializedName("menuName")
    public String menuName;

    public MenuBean() {
    }

    protected MenuBean(Parcel in) {
        imgUrl = in.readString();
        menuParentId = in.readInt();
        isShow = in.readInt();
        menuId = in.readInt();
        isClick = in.readInt();
        type = in.readInt();
        menuName = in.readString();
    }

    public static final Creator<MenuBean> CREATOR = new Creator<MenuBean>() {
        @Override
        public MenuBean createFromParcel(Parcel in) {
            return new MenuBean(in);
        }

        @Override
        public MenuBean[] newArray(int size) {
            return new MenuBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
        dest.writeInt(menuParentId);
        dest.writeInt(isShow);
        dest.writeInt(menuId);
        dest.writeInt(isClick);
        dest.writeInt(type);
        dest.writeString(menuName);
    }
}
