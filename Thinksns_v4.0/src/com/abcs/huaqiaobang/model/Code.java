package com.abcs.huaqiaobang.model;

public class Code {

	/**
	 * 成功
	 */
	public static final int SUCCESS = 1;
	/**
	 * 系统错误 
	 */
	public static final int SYSERR = -1000;
	/**
	 * 用户未实名认证
	 */
	public static final int USERNOTFIND = -1001;
	/**
	 * 未找到绑定的银行卡
	 */
	public static final int BANKNOTFIND = -1002;
	
	/**
	 * 输入金额有误
	 */
	public static final int AMOUMTNOTNUM = -1003;
	/**
	 * 用户已认证
	 */
	public static final int USERHASBIND = -1004;
	/**
	 * 重复操作
	 */
	public static final int REPEATOPERA  = -1005;
	
	/**
	 * 余额不足
	 */
	public static final int BALANCENOTENOUTH  = -1006;
	
	/**
	 * 用户未设置密码
	 */
	public static final int USERNOTPAYPWD  = -1007;
	
	/**
	 * 支付密码错误
	 */
	public static final int PAYPWDERR  = -1008;
	
	/**
	 * 身份证号码错误
	 */
	public static final int IDERROR  = -1009;
	
	/**
	 * 商城版本一样
	 */
	public static final int SHOP_VERSION = 1000;
	
	/**
	 * 错误 
	 */
	public static final int ERROR = -1;
	
	/**
	 * 余额不足
	 */
	public static final int USERCASHISNOTENOUTH = 1000;
	
}
