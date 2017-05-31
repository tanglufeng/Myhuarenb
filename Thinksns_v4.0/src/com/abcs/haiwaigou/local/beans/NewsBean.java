package com.abcs.haiwaigou.local.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xuke on 2017/2/10.
 */
public class NewsBean implements Parcelable {
    /**
     * summary :
     * read : false
     * species : country_thailand
     * purl : http://bbs.taiguo.com/data/attachment/portal/201702/09/173205qbvgdcejpppiu5ip.jpg.thumb.jpg
     * id : 2258394
     * time : 1486632720000
     * source : 泰国网
     * hasCollect : false
     * title : 让我死，我就死给你看......
     * type : taiguonet_my_news
     */

    public String summary;
    public boolean read;
    public String species;
    public String purl;
    public String id;
    public String time;
    public String source;
    public boolean hasCollect;
    public String title;
    public String type;

//    /**
//     * id : 2076542
//     * time : 1484487838000
//     * title : 泰国回应人权报告缺乏公正∣移交77国集团主席职位
//     * purl : http://qhcdn.oss-cn-hangzhou.aliyuncs.com/icon/tuling/wxh/hqb/505_1484489717536.png
//     */
//
//    private String id;
//    private String time;
//    private String title;
//    private String purl;
//    private int ids;
//
    public static final Creator<NewsBean> CREATOR = new Creator<NewsBean>() {
        @Override
        public NewsBean createFromParcel(Parcel in) {
            return new NewsBean(in);
        }

        @Override
        public NewsBean[] newArray(int size) {
            return new NewsBean[size];
        }
    };


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isHasCollect() {
        return hasCollect;
    }

    public void setHasCollect(boolean hasCollect) {
        this.hasCollect = hasCollect;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NewsBean() {
    }

    protected NewsBean(Parcel in) {
        summary=in.readString();
        species=in.readString();
        purl=in.readString();
        id=in.readString();
        time=in.readString();
        source=in.readString();
        title=in.readString();
        type=in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(summary);
        dest.writeString(species);
        dest.writeString(purl);
        dest.writeString(id);
        dest.writeString(time);
        dest.writeString(source);
        dest.writeString(title);
        dest.writeString(type);
    }
}
