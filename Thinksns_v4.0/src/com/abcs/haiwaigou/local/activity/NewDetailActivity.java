package com.abcs.haiwaigou.local.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class NewDetailActivity extends BaseActivity {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_title_name)
    TextView tTitleName;
    @InjectView(R.id.news_sources)
    TextView tljr_tv_news_source;
    @InjectView(R.id.news_date)
    TextView tljr_tv_date;
    @InjectView(R.id.progress_bar)
    ProgressBar progressBar;
    @InjectView(R.id.tv_tro)
    TextView tvTro;
    @InjectView(R.id.linear_root)
    LinearLayout linearRoot;
    private Handler handler = new Handler();

    private String id;
    private String time;
    private String title;
    private String webUrl;

    private ArrayList<String> uList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_new_detail);
        ButterKnife.inject(this);
        id = getIntent().getStringExtra("id");
        time = getIntent().getStringExtra("time");
//        title = getIntent().getStringExtra("title");
        initData();

        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        if (!TextUtils.isEmpty(title)){
//            tTitleName.setText(title);
//        }
    }


    private void initData() {
        String pid = "0";// 默认无
        if (MyApplication.getInstance().self != null) {
            pid = MyApplication.getInstance().self.getId();
        }
        String url = TLUrl.getInstance().URL_new + "api/detail";
        String params = "species=hq_b" + "&id=" + id + "&time=" + time + "&uid=" + pid;

        LogUtil.i("xuke118", url + "?" + params);
        HttpRequest.sendPost(url, params, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject cmdInfo = new JSONObject(msg);
                            LogUtil.i("xuke118", " news:" + msg);

                            int status = cmdInfo.optInt("status", 0);
                            String message = cmdInfo.optString("msg", "网络连接错误，请稍后重试");
                            if (status == 0) {
                                Toast.makeText(NewDetailActivity.this, message, Toast.LENGTH_LONG).show();
                                return;
                            }
                            JSONObject newsObject = cmdInfo.getJSONObject("joData");

                            tTitleName.setText(newsObject.optString("title"));
                            tljr_tv_news_source.setText(newsObject.optString("source"));
                            tljr_tv_date.setText(newsObject.optString("time"));

                            webUrl = newsObject.optString("content");

                            Log.i("xuke118", "webUrl:" + webUrl);

                            if (NetworkUtils.isNetAvailable(NewDetailActivity.this))
                                initWeb(false, webUrl);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    WebView newsWeb;

    private void initWeb(boolean remove, String webUrl) {
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

        String htmlCode = delATag(webUrl.replaceAll("href", ""));
        // 加载网页
        newsWeb.loadDataWithBaseURL(null, htmlCode, "text/html", "utf-8", null);
//        newsWeb.loadUrl(url);
        /*
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


    public String delATag(String content) {
        Pattern p = Pattern.compile("</?a[^>]*>");
        Matcher m = p.matcher(content);
        content = m.replaceAll("");
        return content;
    }


}
