package com.abcs.huaqiaobang.ytbt.voicemeeting;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Id;

public class ContactEntity implements Serializable{
	@Id private String name;
	private String num;
	private long time;
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	

}
