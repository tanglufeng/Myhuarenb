package com.abcs.huaqiaobang.ytbt.bean;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Id;

public class AddFriendRequestBean implements Serializable {
	String id, nickname,avadar,voip;
	public String getVoip() {
		return voip;
	}
	public void setVoip(String voip) {
		this.voip = voip;
	}
	@Id String uid;
	int state;
	long time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getNickname() {
		return nickname;
	}
	public String getAvadar() {
		return avadar;
	}
	public void setAvadar(String avadar) {
		this.avadar = avadar;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((nickname == null) ? 0 : nickname.hashCode());
		result = prime * result + state;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AddFriendRequestBean) {
			AddFriendRequestBean u = (AddFriendRequestBean) obj;
			return this.uid.equals(u.uid);
		}
		return super.equals(obj);
	}
	
	
}
