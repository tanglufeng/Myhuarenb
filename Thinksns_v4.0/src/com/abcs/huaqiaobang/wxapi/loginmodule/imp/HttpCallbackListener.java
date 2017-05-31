package com.abcs.huaqiaobang.wxapi.loginmodule.imp;

public abstract class HttpCallbackListener {
	public abstract void onFinish(String response);
	public void onError(Exception exception) {
	};
	public void onError(String exception) {
	};
	public void onError(Object exception) {
	};

	public void onCancel() {
	};
}
