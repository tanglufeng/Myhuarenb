package com.abcs.huaqiaobang.ytbt.bean;

import com.lidroid.xutils.db.annotation.Id;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 9148049628584458016L;
	private String id, uname, fromUser, avatar, location, nickname,remark;
	@Id
	private String voipAccout;
	private int uid, gender;
	

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getVoipAccout() {
		return voipAccout;
	}

	public void setVoipAccout(String voipAccout) {
		this.voipAccout = voipAccout;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((voipAccout == null) ? 0 : voipAccout.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User u = (User) obj;
			return this.voipAccout.equals(u.voipAccout);
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return "{\"voipAccout\":\""+voipAccout+"\"," +
				"\"nickname\":\""+nickname+"\"," +
						"\"avatar\":\""+avatar+"\"," +
						"\"uid\":\""+uid+"\"}";
	}

	public void setSortLetters(String string) {
		this.location = string;
		// TODO Auto-generated method stub
		
	}

	public String getSortLetters() {
		// TODO Auto-generated method stub
		return location;
	}
	

}
