package com.abcs.haiwaigou.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 项目名称：com.abcs.haiwaigou.model
 * 作者：zds
 * 时间：2017/3/21 13:48
 */

public class BannersBean implements Parcelable {

    public BannersBean() {
    }

    public BannersBean(Parcel in) {
          cityId=in.readInt();
        is_junp=in.readInt();
          id=in.readInt();
          newsUrl=in.readString();
          createTime=in.readLong();
          img=in.readString();
          orderId=in.readInt();
          type=in.readInt();
    }

    public static final Creator<BannersBean> CREATOR = new Creator<BannersBean>() {
        @Override
        public BannersBean createFromParcel(Parcel in) {
            return new BannersBean(in);
        }

        @Override
        public BannersBean[] newArray(int size) {
            return new BannersBean[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(cityId);
        dest.writeInt(is_junp);
        dest.writeInt(id);
        dest.writeString(newsUrl);
        dest.writeLong(createTime);
        dest.writeString(img);
        dest.writeInt(orderId);
        dest.writeInt(type);
    }

    /**
     * city_id : 41
     * id : 1
     * news_url : www.huaqiaobang.com/mobile/index.php?act=hqb_admin&op=find_news&id=12
     * create_time : 1482918253000
     * img : http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_270507b2dbb10d13c5b5bdc42a41d58e.jpg
     * order_id : 0
     * type : 2
     */

    public int cityId;
    public int is_junp;
    public int id;
    public String newsUrl;
    public long createTime;
    public String img;
    public int orderId;
    public int type;
}
