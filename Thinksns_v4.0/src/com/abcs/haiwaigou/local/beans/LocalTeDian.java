package com.abcs.haiwaigou.local.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 */
public class LocalTeDian implements Parcelable{
/*
    {
        "id": "7",
            "td_id": "7",
            "city_id": "55",
            "img": "http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_1624a9c3c9ea68c37c3f161a25bcbb14.png",
            "td_name": "私人飞机曼谷",
            "intro": "飞，向不一样的人生",
            "td_cn_name": "私人飞机",
            "td_en_name": "",
            "td_desc": "",
            "td_desc_img": "http://www.huaqiaobang.com/data/upload/mobile/special/s8/s8_df1aca5e339eda429b77344349bc7a9e.jpg",
            "jump_id": "93",
            "goods_id": "0",
            "state": "1",
            "type": "1.2",
            "sort": "1"
    }
*/

    public String id;
    public String td_id;
    public String  city_id;
    public String img;
    public String td_name;
    public String intro;
    public String td_cn_name;
    public String td_en_name;
    public String td_desc;
    public String td_desc_img;
    public String jump_id;
    public String goods_id;
    public String state;
    public String type;
    public String sort;


    public LocalTeDian() {
    }

    protected LocalTeDian(Parcel in) {
          id= in.readString();
          td_id= in.readString();
           city_id= in.readString();
          img= in.readString();
          td_name= in.readString();
          intro= in.readString();
          td_cn_name= in.readString();
          td_en_name= in.readString();
          td_desc= in.readString();
          td_desc_img= in.readString();
          jump_id= in.readString();
          goods_id= in.readString();
          state= in.readString();
          type= in.readString();
          sort= in.readString();
    }

    public static final Creator<LocalTeDian> CREATOR = new Creator<LocalTeDian>() {
        @Override
        public LocalTeDian createFromParcel(Parcel in) {
            return new LocalTeDian(in);
        }

        @Override
        public LocalTeDian[] newArray(int size) {
            return new LocalTeDian[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(td_id);
        dest.writeString(city_id);
        dest.writeString(img);
        dest.writeString(td_name);
        dest.writeString(intro);
        dest.writeString(td_cn_name);
        dest.writeString(td_en_name);
        dest.writeString(td_desc);
        dest.writeString(td_desc_img);
        dest.writeString(jump_id);
        dest.writeString(goods_id);
        dest.writeString(state);
        dest.writeString(type);
        dest.writeString(sort);

    }
}
