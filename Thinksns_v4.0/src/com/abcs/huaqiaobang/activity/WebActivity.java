package com.abcs.huaqiaobang.activity;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


@SuppressLint("HandlerLeak")
public class WebActivity extends BaseActivity {

	public static final int PROGRESS = 0;
	public static final int WEBVIEW = 1;
	private WebView mWebView;
	private TextView txt_web_name;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.occft_activity_web);
		init();
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		txt_web_name.setText(intent.getStringExtra("name"));
		if (url == null || url.length() == 0)
			url = "http://www.baidu.com/";

		loadView(mWebView, url);

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case PROGRESS:
					/* 显示进度条 */
//					ProgressDlgUtil.showProgressDlg("", WebActivity.this);
					break;
				case WEBVIEW:
//					ProgressDlgUtil.stopProgressDlg();
					break;
				default:
					break;
				}
			}
			super.handleMessage(msg);
		}
	};

	private boolean isfirst = true;

	public final class Back {// 这个Java 对象是绑定在另一个线程里的，
		public void back() {// js中对象,调用的方法
			if (isfirst) {
				isfirst = false;
				return;
			}
			mHandler.post(new Runnable() {
				public void run() {
					WebActivity.this.finish();
				}
			});
		}
	}

	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	private void init() {
		txt_web_name = (TextView) findViewById(R.id.tljr_txt_web_name);
		findViewById(R.id.tljr_img_web_fanhui).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (mWebView.canGoBack()) {
							mWebView.goBack();
						} else {
							WebActivity.this.finish();
						}
					}
				});
		mWebView = (WebView) findViewById(R.id.tljr_webview);
		mWebView.addJavascriptInterface(new Back(), "Back");//
		// 后面字符串与js中调用的对象名对应
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.requestFocus();
		mWebView.setScrollBarStyle(0);

		mWebView.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				// loadView(view, url);
				return false;
			}
		});
		mWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int newProgress) {
				if (100 == newProgress) {
					mHandler.sendEmptyMessage(WEBVIEW);
				}
				super.onProgressChanged(view, newProgress);
			}
		});
	}

	private void loadView(final WebView webView, final String url) {
		// new Thread() {
		// public void run() {
		// mHandler.sendEmptyMessage(PROGRESS);

//		ProgressDlgUtil.showProgressDlg("", WebActivity.this);
		// String data =
		// "<meta name="+"\"applicable-device"+"\""+"content="+"\"mobile"+"\">";
		// webView.loadDataWithBaseURL(url, data, "text/html", "utf-8", null);
		Log.i("HQ", "final url:" + url);
		webView.loadUrl(url);
		//
		// super.run();
		// }

		// }.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event != null
				&& event.getRepeatCount() == 0) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			} else {
				WebActivity.this.finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		ProgressDlgUtil.stopProgressDlg();
	}
}