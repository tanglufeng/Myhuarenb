package com.abcs.haiwaigou.local.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zjz on 2016/9/6.
 */
public class HireFind implements Parcelable{


    private long createTime;
    private String title;
    private String views;
    private long pubTime;
    private int isTop;
    private String  issueUser;
    private int tid;
    private String  listTypeId;
    private String contact;
    private String contactMan;
    private String countryEnName;
    private String cityId;
    private String  id;
    private String icon;
    private String isIssue;
    private String parentId;
    private Integer ids;
    private boolean cheeck;

    public boolean isCheeck() {
        return cheeck;
    }

    public void setCheeck(boolean cheeck) {
        this.cheeck = cheeck;
    }

    public HireFind() {
    }

    protected HireFind(Parcel in) {
        createTime = in.readLong();
        title = in.readString();
        views = in.readString();
        pubTime = in.readLong();
        isTop = in.readInt();
        issueUser = in.readString();
        tid = in.readInt();
        listTypeId = in.readString();
        contact = in.readString();
        contactMan = in.readString();
        countryEnName = in.readString();
        cityId = in.readString();
        id = in.readString();
        icon = in.readString();
        isIssue = in.readString();
        parentId = in.readString();
    }

    public static final Creator<HireFind> CREATOR = new Creator<HireFind>() {
        @Override
        public HireFind createFromParcel(Parcel in) {
            return new HireFind(in);
        }

        @Override
        public HireFind[] newArray(int size) {
            return new HireFind[size];
        }
    };

    public Integer getIds() {
        return ids;
    }

    public void setIds(Integer ids) {
        this.ids = ids;
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

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public long getPubTime() {
        return pubTime;
    }

    public void setPubTime(long pubTime) {
        this.pubTime = pubTime;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public String getIssueUser() {
        return issueUser;
    }

    public void setIssueUser(String  issueUser) {
        this.issueUser = issueUser;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getListTypeId() {
        return listTypeId;
    }

    public void setListTypeId(String listTypeId) {
        this.listTypeId = listTypeId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactMan() {
        return contactMan;
    }

    public void setContactMan(String contactMan) {
        this.contactMan = contactMan;
    }

    public String getCountryEnName() {
        return countryEnName;
    }

    public void setCountryEnName(String countryEnName) {
        this.countryEnName = countryEnName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIsIssue() {
        return isIssue;
    }

    public void setIsIssue(String isIssue) {
        this.isIssue = isIssue;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(createTime);
        dest.writeString(title);
        dest.writeString(views);
        dest.writeLong(pubTime);
        dest.writeInt(isTop);
        dest.writeString(issueUser);
        dest.writeInt(tid);
        dest.writeString(listTypeId);
        dest.writeString(contact);
        dest.writeString(contactMan);
        dest.writeString(countryEnName);
        dest.writeString(cityId);
        dest.writeString(id);
        dest.writeString(icon);
        dest.writeString(isIssue);
        dest.writeString(parentId);
    }
}
