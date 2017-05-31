package com.abcs.huaqiaobang.model;

public class PayWay {
	
	
	private String name;//支付方式名称
	private String sub_title;//支付描述
	private String img_url;//支付logo
	private int type;//支付logo
	
	public PayWay(String name, String sub_title, String img_url) {
		
		this.name = name;
		this.sub_title = sub_title;
		this.img_url = img_url;
	}
	
	public PayWay() {
		
	}

	public String getName() {
		return name;
	}
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getSub_title() {
		return sub_title;
	}
	public void setSub_title(String sub_title) {
		this.sub_title = sub_title;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	

}
