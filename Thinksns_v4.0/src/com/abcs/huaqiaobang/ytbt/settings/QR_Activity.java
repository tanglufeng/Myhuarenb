package com.abcs.huaqiaobang.ytbt.settings;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.ytbt.emotion.DisplayUtils;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

public class QR_Activity extends BaseActivity {
	private ImageView iv;
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr);
		iv = (ImageView) findViewById(R.id.qr_code);
		final String filePath = getFileRoot(this) + File.separator + "qr_"
				+ System.currentTimeMillis() + ".jpg";
		String path = getFileRoot(this) + File.separator + "avator_"
				+ System.currentTimeMillis() + ".jpg";
		new HttpUtils().download(MyApplication.getInstance().getAvater(), path, new RequestCallBack<File>() {
			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				bitmap = BitmapFactory
						.decodeFile(arg0.result.getAbsolutePath());
				new Thread(new Runnable() {
					int width =  DisplayUtils.getScreenWidthPixels(QR_Activity.this);
					@Override
					public void run() {
						boolean success = QRCodeUtil.createQRImage("{\"name\":\""+ MyApplication.getInstance().getOwnernickname()+"\",\"uid\":\""
					+MyApplication.getInstance().getUid()+"\",\"avator\":\""+MyApplication.getInstance().getAvater()+"\"}",
					width/3*2, width/3*2, bitmap, filePath);
						if (success) {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									iv.setImageBitmap(BitmapFactory
											.decodeFile(filePath));
								}
							});
						}
					}
				}).start();
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Tool.showInfo(QR_Activity.this, "网络异常");
			}
		});
		
	}

}
