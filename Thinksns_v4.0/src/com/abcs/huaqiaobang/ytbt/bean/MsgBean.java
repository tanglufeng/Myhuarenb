package com.abcs.huaqiaobang.ytbt.bean;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Id;

/**
 * Created by Administrator on 2015/12/26.
 */
public class MsgBean  implements Serializable {
 @Id private int id;
    private String type;
    private String msg;
    private long msgtime;
    private String img;
    private String voicepath;
    private String mgsTo,msgfrom;
    private int flag;
    
    
    public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getMsgfrom() {
		return msgfrom;
	}

	public void setMsgfrom(String msgfrom) {
		this.msgfrom = msgfrom;
	}

	public String getMgsTo() {
		return mgsTo;
	}

	public void setMgsTo(String mgsTo) {
		this.mgsTo = mgsTo;
	}

//	public String getNickname() {
//		return nickname;
//	}
//
//	public void setNickname(String nickname) {
//		this.nickname = nickname;
//	}

	public String getVoicepath() {
		return voicepath;
	}

	public void setVoicepath(String voicepath) {
		this.voicepath = voicepath;
	}

	

    public MsgBean() {
    }

    public MsgBean(String type, String msg, Long msgtime,String name) {
        this.type = type;
        this.msg = msg;
        this.msgtime = msgtime;
      
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getMsgtime() {
        return msgtime;
    }

    public void setMsgtime(Long msgtime) {
        this.msgtime = msgtime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
