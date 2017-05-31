package com.abcs.huaqiaobang.tljr.news.channel.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/** 
 * ITEM的对应可序化队列属性
 *  */
public class ChannelItem implements Serializable, Parcelable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	/** 
	 * 栏目对应ID
	 *  */
	public Integer id;
	/** 
	 * 栏目对应NAME
	 *  */
	public String name;
	/** 
	 * 栏目在整体中的排序顺序  rank
	 *  */
	public Integer orderId;
	/** 
	 * 栏目是否选中
	 *  */
	public Integer selected;
	
	/*
	 * 频道类型
	 */
	public String species ;
	 
	
	
	/* 0代表HqssFragment
	 *  1代表 NewsMarkFragment
	 *   2 代表 其他频道的 ComNewsFragment 普通直播新闻
	 */
	public Integer otherChannel = 0 ;		
	
	
	public String otherChannelType;
	
	
	
	
	public String contentPictureUrl ;

	public int channelType ; // 0重要频道 1推荐频道 2其他频道

	public boolean hasCheck ;
	
	
  
	public boolean isHasCheck()
	{
		return hasCheck;
	}

	public void setHasCheck(boolean hasCheck)
	{
		this.hasCheck = hasCheck;
	}

	public int getChannelType() {
		return channelType;
	}

	public void setChannelType(int channelType) {
		this.channelType = channelType;
	}

	public String getContentPictureUrl() {
		return contentPictureUrl;
	}

	public void setContentPictureUrl(String contentPictureUrl) {
		this.contentPictureUrl = contentPictureUrl;
	}
 

	public ChannelItem() {
	}

	public ChannelItem(int id, String name, int orderId,int selected,String speical) {
		this.id = Integer.valueOf(id);
		this.name = name;
		this.orderId = Integer.valueOf(orderId);
		this.selected = Integer.valueOf(selected);
		this.species = speical;
	}

	
	
	
	 
 

 

	public String getOtherChannelType() {
		return otherChannelType;
	}

	public void setOtherChannelType(String otherChannelType) {
		this.otherChannelType = otherChannelType;
	}

	public Integer getOtherChannel() {
		return otherChannel;
	}

	public void setOtherChannel(Integer otherChannel) {
		this.otherChannel = otherChannel;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public int getId() {
		return this.id.intValue();
	}

	public String getName() {
		return this.name;
	}

	public int getOrderId() {
		return this.orderId.intValue();
	}

	public Integer getSelected() {
		return this.selected;
	}

	public void setId(int paramInt) {
		this.id = Integer.valueOf(paramInt);
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setOrderId(int paramInt) {
		this.orderId = Integer.valueOf(paramInt);
	}

	public void setSelected(Integer paramInteger) {
		this.selected = paramInteger;
	}

	public String toString() {
		return "ChannelItem [id=" + this.id + ", name=" + this.name
				+ ", selected=" + this.selected + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.id);
		dest.writeString(this.name);
		dest.writeValue(this.orderId);
		dest.writeValue(this.selected);
		dest.writeValue(this.species);
		dest.writeValue(this.otherChannel);
		dest.writeString(this.otherChannelType);
		dest.writeString(this.contentPictureUrl);
		dest.writeInt(this.channelType);
		dest.writeByte(hasCheck ? (byte) 1 : (byte) 0);
	}

	protected ChannelItem(Parcel in) {
		this.id = (Integer) in.readValue(Integer.class.getClassLoader());
		this.name = in.readString();
		this.orderId = (Integer) in.readValue(Integer.class.getClassLoader());
		this.selected = (Integer) in.readValue(Integer.class.getClassLoader());
		this.species = in.readString();
		this.otherChannel = (Integer) in.readValue(Integer.class.getClassLoader());
		this.otherChannelType = in.readString();
		this.contentPictureUrl = in.readString();
		this.channelType = in.readInt();
		this.hasCheck = in.readByte() != 0;
	}

	public static final Creator<ChannelItem> CREATOR = new Creator<ChannelItem>() {
		public ChannelItem createFromParcel(Parcel source) {
			return new ChannelItem(source);
		}

		public ChannelItem[] newArray(int size) {
			return new ChannelItem[size];
		}
	};
}
