package com.abcs.huaqiaobang.ytbt.bean;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Id;

public class LabelBean implements Serializable {
	String LabelName;
	String users;
	@Id String id;
	public String getLabelName() {
		return LabelName;
	}
	public void setLabelName(String labelName) {
		LabelName = labelName;
	}
	public String getUsers() {
		return users;
	}
	public void setUsers(String users) {
		this.users = users;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
