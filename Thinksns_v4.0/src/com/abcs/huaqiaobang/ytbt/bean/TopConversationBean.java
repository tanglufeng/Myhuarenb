package com.abcs.huaqiaobang.ytbt.bean;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Id;

public class TopConversationBean implements Serializable {  
	@Id String  conversationpeople;
	 String msgfrom,msgto,msg;
	 Long msglasttime;
	 int unread;
	 boolean isgroup ;
	public boolean isIsgroup() {
		return isgroup;
	}
	public void setIsgroup(boolean isgroup) {
		this.isgroup = isgroup;
	}
	public String getConversationpeople() {
		return conversationpeople;
	}
	public void setConversationpeople(String conversationpeople) {
		this.conversationpeople = conversationpeople;
	}
	public String getMsgfrom() {
		return msgfrom;
	}
	public void setMsgfrom(String msgfrom) {
		this.msgfrom = msgfrom;
	}
	public String getMsgto() {
		return msgto;
	}
	public void setMsgto(String msgto) {
		this.msgto = msgto;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Long getMsglasttime() {
		return msglasttime;
	}
	public void setMsglasttime(Long msglasttime) {
		this.msglasttime = msglasttime;
	}
	public int getUnread() {
		return unread;
	}
	public void setUnread(int unread) {
		this.unread = unread;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((conversationpeople == null) ? 0 : conversationpeople
						.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TopConversationBean other = (TopConversationBean) obj;
		if (conversationpeople == null) {
			if (other.conversationpeople != null)
				return false;
		} else if (!conversationpeople.equals(other.conversationpeople))
			return false;
		return true;
	}
	
	 
}
