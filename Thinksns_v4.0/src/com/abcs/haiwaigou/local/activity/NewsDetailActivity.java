package com.abcs.haiwaigou.local.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.LNews;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.abcs.huaqiaobang.tljr.news.ShowWebImageActivity;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.sociax.android.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewsDetailActivity extends BaseActivity implements View.OnClickListener{

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_title_name)
    TextView tTitleName;
//    @InjectView(R.id.news_web)
//    WebView newsWeb;

    @InjectView(R.id.progress_bar)
    ProgressBar progressBar;
    @InjectView(R.id.relative_top)
    RelativeLayout relativeTop;
    @InjectView(R.id.tv_tro)
    TextView tvTro;
    @InjectView(R.id.linear_root)
    LinearLayout linearRoot;
    @InjectView(R.id.t_last)
    TextView tLast;
    @InjectView(R.id.t_next)
    TextView tNext;
    @InjectView(R.id.l_bottom)
    LinearLayout lBottom;
    private ArrayList<String> uList = new ArrayList<String>();
    String webUrl;
    public ArrayList<LNews> lNewsArrayList = new ArrayList<LNews>();
    int curPosition;
    public static boolean isMore;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_news_detail);
        ButterKnife.inject(this);
        webUrl = getIntent().getStringExtra("content");
        lNewsArrayList = getIntent().getParcelableArrayListExtra("datas");

        Log.i("zjz", "newsArrayList=" + lNewsArrayList.size());
        for (int i = 0; i < lNewsArrayList.size(); i++) {
            if (webUrl.equals(lNewsArrayList.get(i).getContent())) {
                curPosition = i;
            }
        }

        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tLast.setOnClickListener(this);
        tNext.setOnClickListener(this);
        tTitleName.setText("新闻详情");
        if (NetworkUtils.isNetAvailable(this))
            initWeb(false,webUrl);
    }

    WebView newsWeb;
    private void initWeb(boolean remove,String url) {
        if (remove)
            if (linearRoot.getChildCount() > 1) {
                linearRoot.removeViews(0, linearRoot.getChildCount() - 2);
                linearRoot.invalidate();
            }
        newsWeb = new WebView(this);
        newsWeb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        newsWeb.setFocusable(false);
        newsWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 图片自适应
        newsWeb.getSettings().setUseWideViewPort(false);
        newsWeb.getSettings().setLoadWithOverviewMode(true);
        newsWeb.getSettings().setBlockNetworkImage(Constent.noPictureMode); // 无图模式
        newsWeb.getSettings().setBlockNetworkImage(Constent.netType.equals("未知"));

        newsWeb.getSettings().setJavaScriptEnabled(true);
        newsWeb.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { // 屏蔽超链接
                if (url != null)
                    view.loadUrl(url);
                return true;
            }
        });
        newsWeb.setVisibility(View.VISIBLE);
        lBottom.setVisibility(View.VISIBLE);
        newsWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    tvTro.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                    tvTro.setVisibility(View.GONE);
                }
            }
        });

//        newsWeb.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");
//        newsWeb.setWebViewClient(new MyWebViewClient());


		/*
         * 调整行间距，字间距，字体大小，改由服务器更改
		 */
        // int a = 18;
        // String strUrl = "<html> \n" + "<head> \n" +
        // "<style type=\"text/css\"> \n"
        // + "body {text-align:justify; font-size: " + a + "px; line-height: " +
        // (a + 6)
        // + "px;letter-spacing:2px}\n" + "</style> \n" + "</head> \n" +
        // "<body>" + htmlCode
        // + "</body> \n </html>";

        // 加载网页
//        newsWeb.loadDataWithBaseURL(null, webUrl, "text/html", "utf-8", null);
        newsWeb.loadUrl(url);
        /*w
         * 新闻内图片,遍历获取链接地址 用于 ShowWebImageActivity
		 */
        Document doc_Dis = Jsoup.parse(webUrl);
        Elements ele_Img = doc_Dis.getElementsByTag("img");
        if (ele_Img.size() != 0) {
            for (Element e_Img : ele_Img) {
                String img = e_Img.attr("src");
                uList.add(img);
            }
        }
        linearRoot.addView(newsWeb, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.t_last:
                if (curPosition == 0) {
                    showToast("已经是第一条信息");
                    return;
                }
                hideView();
                curPosition -= 1;

                initWeb(true,lNewsArrayList.get(curPosition).getContent());
                break;
            case R.id.t_next:
                if (curPosition == lNewsArrayList.size() - 1) {
                    showToast("已经是最后一条信息");
                    return;
                }
                if (isMore && curPosition == lNewsArrayList.size() - 2) {
                    showToast("正在加载更多信息...");
                    if (NewsMoreActivity.newsMoreHashMap.get(MyString.NEWSMORE) instanceof NewsMoreActivity) {
                        final NewsMoreActivity newsMoreActivity = (NewsMoreActivity) NewsMoreActivity.newsMoreHashMap.get(MyString.NEWSMORE);
                        newsMoreActivity.initMoreDates();
                        newsMoreActivity.initInterfacer(new NewsMoreActivity.InitMoreInterfacer() {
                            @Override
                            public void notifyDataSetChanged() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        lNewsArrayList = newsMoreActivity.hireAndFindModel.getNewsList();
                                        Log.i("zjz", "more_size=" + lNewsArrayList.size());
                                        showToast("加载完成");
                                    }
                                });

                            }
                        });
                    }
                }
                hideView();
                curPosition += 1;
                initWeb(true,lNewsArrayList.get(curPosition).getContent());
                break;
        }
    }

    private void hideView() {
        newsWeb.setVisibility(View.GONE);
        lBottom.setVisibility(View.GONE);
        tvTro.setVisibility(View.GONE);
    }

    // js通信接口

    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            // System.out.println(img);
            Intent intent = new Intent();
            intent.putExtra("image", img);
            intent.putExtra("ulist", uList);
            intent.setClass(context, ShowWebImageActivity.class);
            context.startActivity(intent);

        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
//            addImageClickListner();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);
//            addImageClickListner();
        }
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        newsWeb.loadUrl("javascript:(function(){" + "var objs = document.getElementsByTagName(\"img\"); "
                + "for(var i=0;i<objs.length;i++)  " + "{" + "    objs[i].onclick=function()  " + "    {  "
                + "  window.imagelistner.openImage(this.src);  " + "    }  " + "}" + "})()");
    }
}
