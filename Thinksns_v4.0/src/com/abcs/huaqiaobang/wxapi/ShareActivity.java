package com.abcs.huaqiaobang.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.wxapi.official.share.ShareQQPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.ShareWeiXinPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.ShareWeiboPlatform;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

public class ShareActivity extends BaseActivity implements IWeiboHandler.Response {
	private ShareWeiXinPlatform shareWeiXinPlatform;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShareQQPlatform.getInstance().registerShare(this);
		ShareWeiboPlatform.getInstanse().regiesetShare(this, savedInstanceState, this);
		shareWeiXinPlatform = new ShareWeiXinPlatform(this);
		setContentView(R.layout.tljr_activity_share);
		findViewById(R.id.tljr_per_btn_lfanhui).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		ShareWeiboPlatform.getInstanse().callOnNewIntent(intent, this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ShareQQPlatform.getInstance().callOnActivityResult(requestCode, resultCode, data);
	}

	public void wx(View view) {
		shareWeiXinPlatform.wechatShare(0);
	//	finish();
	}
	public void pyq(View view) {
		shareWeiXinPlatform.wechatShare(1);
	//	finish();
	}
	public void qq(View view) {
		ShareQQPlatform.getInstance().share(this);
	}
	public void wb(View view) {
		ShareWeiboPlatform.getInstanse().share(this);
	}
	public void back(View view) {
		finish();
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
			case WBConstants.ErrorCode.ERR_OK :
				showToast("分享成功");
				break;
			case WBConstants.ErrorCode.ERR_CANCEL :
				showToast("取消分享");
				break;
			case WBConstants.ErrorCode.ERR_FAIL :
				showToast("分享失败，Error Message: " + baseResp.errMsg);
				break;
		}
	}
}
