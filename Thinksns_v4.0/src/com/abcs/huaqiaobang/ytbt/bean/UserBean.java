package com.abcs.huaqiaobang.ytbt.bean;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Id;

/**
 * Created by Administrator on 2015/12/25.
 */
public class UserBean implements Serializable {
	private String dateCreate, voipPwd;
	@Id
	private String voipAccount;

	public String getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(String dateCreate) {
		this.dateCreate = dateCreate;
	}

	public String getVoipAccount() {
		return voipAccount;
	}

	public void setVoipAccount(String voipAccount) {
		this.voipAccount = voipAccount;
	}

	public String getVoipPwd() {
		return voipPwd;
	}

	public void setVoipPwd(String voipPwd) {
		this.voipPwd = voipPwd;
	}
    
}
