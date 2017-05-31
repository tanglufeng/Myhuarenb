package com.abcs.huaqiaobang.ytbt.bean;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Id;

public class GroupMemberBean implements Serializable {
  
	@Id String groupid;
	  String members;
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getMembers() {
		return members;
	}
	public void setMembers(String members) {
		this.members = members;
	}
	
}
