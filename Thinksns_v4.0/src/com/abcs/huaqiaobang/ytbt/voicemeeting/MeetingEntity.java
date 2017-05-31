package com.abcs.huaqiaobang.ytbt.voicemeeting;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Id;

public class MeetingEntity implements Serializable{
	@Id private String meetingNo;
	private String users;
	public String getUsers() {
		return users;
	}
	public void setUsers(String users) {
		this.users = users;
	}
	public String getMeetingNo() {
		return meetingNo;
	}
	public void setMeetingNo(String meetingNo) {
		this.meetingNo = meetingNo;
	}
}
