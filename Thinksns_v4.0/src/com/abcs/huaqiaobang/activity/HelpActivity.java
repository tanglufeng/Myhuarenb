package com.abcs.huaqiaobang.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.abcs.sociax.android.R;

public class HelpActivity extends Activity {
	
	private View helpview;
	private WebView webview_help;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		helpview = getLayoutInflater().inflate(R.layout.main_activity_help, null);
		setContentView(helpview);

		webview_help = (WebView) findViewById(R.id.webview_help);
		WebSettings settings = webview_help.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setAllowFileAccess(true);
		//设置支持缩放
		settings.setBuiltInZoomControls(true);
		Intent intent = getIntent();
		webview_help.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{ //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});

		if(intent!=null){
			String url = intent.getStringExtra("url");
			webview_help.loadUrl(url);

		}





//		String url="https://m.baidu.com";
//		loadWebView(url);
		
	}

	private void loadWebView(String url) {
		// TODO Auto-generated method stub
		webview_help.loadUrl(url);
	}

}
