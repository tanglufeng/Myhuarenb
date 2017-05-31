package com.abcs.haiwaigou.local.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by zjz on 2016/9/20.
 */
public class LNews implements Parcelable{

    private String content;
    private String id;
    private String title;
    private long auditTime;
    private String image;
    private String from;
    private int ids;

    public LNews() {
    }

    protected LNews(Parcel in) {
        content = in.readString();
        id = in.readString();
        title = in.readString();
        auditTime = in.readLong();
        image = in.readString();
        from = in.readString();
        ids = in.readInt();
    }

    public static final Creator<LNews> CREATOR = new Creator<LNews>() {
        @Override
        public LNews createFromParcel(Parcel in) {
            return new LNews(in);
        }

        @Override
        public LNews[] newArray(int size) {
            return new LNews[size];
        }
    };

    public int getIds() {
        return ids;
    }

    public void setIds(int ids) {
        this.ids = ids;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(long auditTime) {
        this.auditTime = auditTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeLong(auditTime);
        dest.writeString(image);
        dest.writeString(from);
        dest.writeInt(ids);
    }
}
