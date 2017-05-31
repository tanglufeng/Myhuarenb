package com.abcs.huaqiaobang.ytbt.chats;

import android.net.Uri;
import android.os.Bundle;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.ytbt.view.photoview.PhotoView;

import java.io.File;

public class PhotoActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		String path = getIntent().getStringExtra("path");
		if (path == null) {
			finish();
		}
		PhotoView iv = (PhotoView) findViewById(R.id.iv);
		iv.setImageURI(Uri.fromFile(new File(path)));
	}
}
