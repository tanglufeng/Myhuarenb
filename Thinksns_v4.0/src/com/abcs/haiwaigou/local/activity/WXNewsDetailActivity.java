package com.abcs.haiwaigou.local.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.abcs.huaqiaobang.tljr.news.ShowWebImageActivity;
import com.abcs.huaqiaobang.tljr.news.bean.News;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
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

public class WXNewsDetailActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.t_title_name)
    TextView tTitleName;
    @InjectView(R.id.relative_top)
    RelativeLayout relativeTop;
    @InjectView(R.id.progress_bar)
    ProgressBar progressBar;
    //    @InjectView(R.id.news_web)
//    WebView newsWeb;
    String special;
    String id;
    long time;
    public News news = new News();
    @InjectView(R.id.t_title)
    TextView tTitle;
    @InjectView(R.id.t_news_sources)
    TextView tNewsSources;
    @InjectView(R.id.t_news_date)
    TextView tNewsDate;
    @InjectView(R.id.relative_head)
    RelativeLayout relativeHead;
    @InjectView(R.id.tv_tro)
    TextView tvTro;
    @InjectView(R.id.t_last)
    TextView tLast;
    @InjectView(R.id.t_next)
    TextView tNext;
    @InjectView(R.id.l_bottom)
    LinearLayout lBottom;
    @InjectView(R.id.linear_root)
    LinearLayout linearRoot;
    private Handler handler = new Handler();
    private ArrayList<String> uList = new ArrayList<String>();
    public ArrayList<com.abcs.haiwaigou.local.beans.News> newsArrayList = new ArrayList<com.abcs.haiwaigou.local.beans.News>();
    int curPosition;
    public static boolean isMore;
    private ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_wxnews_detail);
        ButterKnife.inject(this);
        aCache = ACache.get(this);
        tTitleName.setText("新闻详情");

        special = getIntent().getStringExtra("special");
        id = getIntent().getStringExtra("id");
        time = getIntent().getLongExtra("time", 0);
        newsArrayList = getIntent().getParcelableArrayListExtra("datas");

        Log.i("zjz", "newsArrayList=" + newsArrayList.size());
        for (int i = 0; i < newsArrayList.size(); i++) {
            if (id.equals(newsArrayList.get(i).getId())) {
                curPosition = i;
            }
        }

        init(false);
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getNewsDetail(special, id, time, new Complete() {
            @Override
            public void complete() {
                ChangeNews();
            }
        });

        tLast.setOnClickListener(this);
        tNext.setOnClickListener(this);

    }

    WebView newsWeb;

    public void init(boolean remove) {
        if (remove)
            if (linearRoot.getChildCount() > 1) {
                linearRoot.removeViews(1, linearRoot.getChildCount() - 2);
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
                // loadView(view, url);
                return true;
            }
        });
        newsWeb.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");
        newsWeb.setWebViewClient(new MyWebViewClient());
        linearRoot.addView(newsWeb, 1);
    }

    private void getNewsDetail(String special, final String id, long time, final Complete complete) {
        JSONObject mainObj = aCache.getAsJSONObject(TLUrl.getInstance().LOCAL_MSG_DETAIL + id);
        if (mainObj != null) {
            try {
                Log.i("zjz", "news_detail=" + mainObj);
                initJsonObject(mainObj);
                complete.complete();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            ProgressDlgUtil.showProgressDlg("Loading...", this);
            String url = TLUrl.getInstance().URL_new + "api/detail";
            String params = "species=" + special + "&id=" + id + "&time=" + time;
            LogUtil.i("zjz", url + "?" + params);
            HttpRequest.sendPost(url, params, new HttpRevMsg() {

                @Override
                public void revMsg(final String msg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject cmdInfo = new JSONObject(msg);
                                LogUtil.i("zjz", "single news:" + msg);
                                aCache.remove(TLUrl.getInstance().LOCAL_MSG_DETAIL + id);
                                if (aCache.getAsJSONObject(TLUrl.getInstance().LOCAL_MSG_DETAIL + id) == null) {
                                    aCache.put(TLUrl.getInstance().LOCAL_MSG_DETAIL + id, cmdInfo, 7 * 24 * 60 * 60);
                                }
                                initJsonObject(cmdInfo);
                                complete.complete();
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(WXNewsDetailActivity.this, "无法连接服务器请稍后再试", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } finally {
                                ProgressDlgUtil.stopProgressDlg();
                            }
                        }
                    });


                }
            });
        }

    }

    private void initJsonObject(JSONObject mainObj) throws JSONException {
        int status = mainObj.optInt("status", 0);
        String message = mainObj.optString("msg", "网络连接错误，请稍后重试");
        if (status == 0) {
            Toast.makeText(WXNewsDetailActivity.this, message, Toast.LENGTH_LONG).show();
            return;
        }
        JSONObject newsObject = mainObj.getJSONObject("joData");
        news.setTitle(newsObject.optString("title"));
        news.setDate(newsObject.optString("time"));
        news.setId(newsObject.getString("id"));
        news.setContent(newsObject.getString("content"));
        news.setSource(newsObject.getString("source"));

        news.setZan(newsObject.optString("allPraise", "0"));
        news.setCai(newsObject.optString("allOppose", "0"));
        news.setCollect(newsObject.optString("allCollect", "0"));
        news.setSurl(newsObject.optString("surl"));

        news.setHaveZan(newsObject.optBoolean("hasPraise", false));
        news.setHaveCai(newsObject.optBoolean("hasOppose", false));
        news.setHaveCollect(newsObject.optBoolean("hasCollect", false));
        //news.setHaveSee(newsObject.optBoolean("read", false));
        news.setLoadDetails(true);
    }


    private void ChangeNews() {
        /*
         * 头部 新闻 -标题 日期 来源
		 */
        if (news != null && news.getTitle() == null && !Util.isEmptyAndSpace(news.getTitle())) {
            return;
        }
        relativeHead.setVisibility(View.VISIBLE);
        String title = Util.ToDBC(Util.myTrim(news.getTitle()));
        title = title.length() > 40 ? title.substring(0, 40) + "..." : title;
        tTitle.setText(title);
        tNewsDate.setText(news.getDate());
        tNewsSources.setText(news.getSource());
        tvTro.setVisibility(View.VISIBLE);
        lBottom.setVisibility(View.VISIBLE);
        /*
         * webview 内容切换
		 */
        // linear_ly.setVisibility(View.VISIBLE);
        updateUI();

    }

    public void updateUI() {

        String k = news.getContent() == null ? news.getSummary() : news.getContent();

        String htmlCode = delATag(k.replaceAll("href", ""));

//        Log.i("zjz", "htmlCode=" + htmlCode);
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
        newsWeb.setVisibility(View.VISIBLE);
        newsWeb.loadDataWithBaseURL(null, htmlCode, "text/html", "utf-8", null);
        newsWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });



		/*w
		 * 新闻内图片,遍历获取链接地址 用于 ShowWebImageActivity
		 */
        Document doc_Dis = Jsoup.parse(htmlCode);
        Elements ele_Img = doc_Dis.getElementsByTag("img");
        if (ele_Img.size() != 0) {
            uList.clear();
            for (Element e_Img : ele_Img) {
                String img = e_Img.attr("src");
                uList.add(img);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.t_last:
                if (curPosition == 0) {
                    showToast("已经是第一条信息");
                    return;
                }
                init(true);
                hideView();
                curPosition -= 1;
                getNewsDetail(newsArrayList.get(curPosition).getSpecies(), newsArrayList.get(curPosition).getId(), newsArrayList.get(curPosition).getTime(), new Complete() {
                    @Override
                    public void complete() {
                        ChangeNews();
                    }
                });
                break;
            case R.id.t_next:
                if (curPosition == newsArrayList.size() - 1) {
                    showToast("已经是最后一条信息");
                    return;
                }
                if (isMore && curPosition == newsArrayList.size() - 2) {
                    showToast("正在加载更多信息...");
                    if (WXNewsMoreActivity.wxMoreHashMap.get(MyString.WXNEWSMORE) instanceof WXNewsMoreActivity) {
                        final WXNewsMoreActivity wxNewsMoreActivity = (WXNewsMoreActivity) WXNewsMoreActivity.wxMoreHashMap.get(MyString.WXNEWSMORE);
                        wxNewsMoreActivity.initMoreDates();
                        wxNewsMoreActivity.initInterfacer(new WXNewsMoreActivity.InitMoreInterfacer() {
                            @Override
                            public void notifyDataSetChanged() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        newsArrayList = wxNewsMoreActivity.newsModel.getNewsList();
                                        Log.i("zjz", "more_size=" + newsArrayList.size());
                                        showToast("加载完成");
                                    }
                                });

                            }
                        });
                    }
                }
                init(true);
                hideView();
                curPosition += 1;
                getNewsDetail(newsArrayList.get(curPosition).getSpecies(), newsArrayList.get(curPosition).getId(), newsArrayList.get(curPosition).getTime(), new Complete() {
                    @Override
                    public void complete() {
                        ChangeNews();
                    }
                });
                break;
        }
    }

    private void hideView() {
        newsWeb.setVisibility(View.GONE);
        lBottom.setVisibility(View.GONE);
        tvTro.setVisibility(View.GONE);
        relativeHead.setVisibility(View.GONE);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            computeHeight();
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();
//            Log.i("zjz","onPageFinished_getContentHeight="+view.getContentHeight());
//            fetchHeight( view.getContentHeight());
//            computeHeight();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
//            Log.i("zjz","onPageStarted_getContentHeight="+view.getContentHeight());
//            fetchHeight( view.getContentHeight());
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            addImageClickListner();
//            Log.i("zjz","onReceivedSslError_getContentHeight="+view.getContentHeight());
//            fetchHeight( view.getContentHeight());
        }
//        @Override
//        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//
//            super.onReceivedError(view, errorCode, description, failingUrl);
//            addImageClickListner();
//        }
    }

    private void computeHeight() {
        newsWeb.loadUrl("javascript:fetchHeight(document.body.getBoundingClientRect().height)");
    }

    private Runnable runnable;

    public void fetchHeight(final float height) {
        final int newHeight = (int) (height * getResources().getDisplayMetrics().density);
        runnable = new Runnable() {
            @Override
            public void run() {
                if (newsWeb.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) newsWeb.getLayoutParams();
                    linearParams.height = newHeight;
                    newsWeb.setLayoutParams(linearParams);
                }
            }
        };
        if (null != handler) {
            handler.postDelayed(runnable, 50);
        }

    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        newsWeb.loadUrl("javascript:(function(){" + "var objs = document.getElementsByTagName(\"img\"); "
                + "for(var i=0;i<objs.length;i++)  " + "{" + "    objs[i].onclick=function()  " + "    {  "
                + "  window.imagelistner.openImage(this.src);  " + "    }  " + "}" + "})()");
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

    public static String delATag(String content) {
        Pattern p = Pattern.compile("</?a[^>]*>");
        Matcher m = p.matcher(content);
        content = m.replaceAll("");
        return content;
    }


}
