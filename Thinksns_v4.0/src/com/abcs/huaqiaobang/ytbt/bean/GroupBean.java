package com.abcs.huaqiaobang.ytbt.bean;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Id;

public class GroupBean implements Serializable {
  @Id String groupId;
	String groupOwner,groupAvatar,groupName,groupType,groupPermission,groupDeclared,dateCreate;
	String memberInGroup;
    
	public String getMemberInGroup() {
		return memberInGroup;
	}

	public void setMemberInGroup(String string) {
		this.memberInGroup = string;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupOwner() {
		return groupOwner;
	}

	public void setGroupOwner(String groupOwner) {
		this.groupOwner = groupOwner;
	}

	public String getGroupAvatar() {
		return groupAvatar;
	}

	public void setGroupAvatar(String groupAvatar) {
		this.groupAvatar = groupAvatar;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getGroupPermission() {
		return groupPermission;
	}

	public void setGroupPermission(String groupPermission) {
		this.groupPermission = groupPermission;
	}

	public String getGroupDeclared() {
		return groupDeclared;
	}

	public void setGroupDeclared(String groupDeclared) {
		this.groupDeclared = groupDeclared;
	}

	public String getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(String dateCreate) {
		this.dateCreate = dateCreate;
	}

	
	
}
