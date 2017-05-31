package com.abcs.haiwaigou.local.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zjz on 2016/9/28.
 */

public class News implements Parcelable{

    /**
     * purl : http://qhcdn.oss-cn-hangzhou.aliyuncs.com/icon/tuling/wxh/hqb/80_1474452106861.png
     * source : 侨外英国
     * hasCollect : false
     * type : hq_b_my_news
     * title : 跟着世界大咖投资英国，金钱身份双丰收
     * id : 273803
     * summary :
     * tagPos : []
     * read : false
     * tagIDs : []
     * species : hq_b
     * time : 1474451401000
     */
    private int ids;
    private String purl;
    private String source;
    private boolean hasCollect;
    private String type;
    private String title;
    private String id;
    private String summary;
    private boolean read;
    private String species;
    private long time;
    private List<?> tagPos;
    private List<?> tagIDs;

    public News() {
    }

    protected News(Parcel in) {
        ids = in.readInt();
        purl = in.readString();
        source = in.readString();
        hasCollect = in.readByte() != 0;
        type = in.readString();
        title = in.readString();
        id = in.readString();
        summary = in.readString();
        read = in.readByte() != 0;
        species = in.readString();
        time = in.readLong();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    public int getIds() {
        return ids;
    }

    public void setIds(int ids) {
        this.ids = ids;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<?> getTagPos() {
        return tagPos;
    }

    public void setTagPos(List<?> tagPos) {
        this.tagPos = tagPos;
    }

    public List<?> getTagIDs() {
        return tagIDs;
    }

    public void setTagIDs(List<?> tagIDs) {
        this.tagIDs = tagIDs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ids);
        dest.writeString(purl);
        dest.writeString(source);
        dest.writeByte((byte) (hasCollect ? 1 : 0));
        dest.writeString(type);
        dest.writeString(title);
        dest.writeString(id);
        dest.writeString(summary);
        dest.writeByte((byte) (read ? 1 : 0));
        dest.writeString(species);
        dest.writeLong(time);
    }
}
