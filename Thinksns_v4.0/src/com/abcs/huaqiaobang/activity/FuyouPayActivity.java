package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;

public class FuyouPayActivity extends BaseActivity {

    private WebView web_fuyoupay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_fuyoupay);

        web_fuyoupay = (WebView) findViewById(R.id.web_fuyoupay);
        WebSettings settings = web_fuyoupay.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        //设置支持缩放
        settings.setBuiltInZoomControls(true);
        final Intent intent = getIntent();
        web_fuyoupay.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边 http://tuling.me

                if (url.indexOf("http://tuling.me/success") == 0) {
                    Intent intent1 = new Intent();
                    intent1.putExtra("id", intent.getStringExtra("id"));
                    setResult(1, intent1);
                    MyUpdateUI.sendUpdateCollection(FuyouPayActivity.this, MyUpdateUI.ORDER);
                    MyUpdateUI.sendUpdateCollection(FuyouPayActivity.this,MyUpdateUI.MYORDERNUM);
                    finish();
                    return true;
                }
                if (url.indexOf("http://tuling.me/") == 0) {
                    finish();
                }
                if(url.indexOf("http://www.huaqiaobang.com/mobile/api/payment/alipay/call_back_url.php")==0){
                    MyUpdateUI.sendUpdateCollection(FuyouPayActivity.this, MyUpdateUI.ORDER);
                    MyUpdateUI.sendUpdateCollection(FuyouPayActivity.this,MyUpdateUI.MYORDERNUM);
                    Log.i("zjz","支付成功！");
                    finish();
                }
                view.loadUrl(url);
                return true;
            }
        });
        if (intent != null) {
            String url = intent.getStringExtra("url");
            web_fuyoupay.loadUrl(url);
        }
    }
}
