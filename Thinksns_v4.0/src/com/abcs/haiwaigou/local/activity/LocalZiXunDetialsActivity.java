package com.abcs.haiwaigou.local.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.StartActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.abcs.huaqiaobang.tljr.news.HuanQiuShiShi;
import com.abcs.huaqiaobang.tljr.news.LocalZiXunWebView;
import com.abcs.huaqiaobang.tljr.news.NewsCommentActivity;
import com.abcs.huaqiaobang.tljr.news.Options;
import com.abcs.huaqiaobang.tljr.news.bean.Comment;
import com.abcs.huaqiaobang.tljr.news.bean.News;
import com.abcs.huaqiaobang.tljr.news.bean.Reply;
import com.abcs.huaqiaobang.tljr.news.widget.CircularProgressView;
import com.abcs.huaqiaobang.tljr.news.widget.InputTools;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.official.share.ShareQQPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.ShareWeiXinPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.ShareWeiboPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.util.ShareContent;
import com.abcs.sociax.android.R;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LocalZiXunDetialsActivity extends BaseFragmentActivity implements View.OnClickListener, IWeiboHandler.Response {

    private LocalZiXunDetialsActivity activity;
    private TextView tljr_tv_title, tljr_tv_date, tljr_tv_news_source; // 新闻标题，内容,日期
    private ImageView img_piyue;
    private LocalZiXunWebView newsWebView; // webview 控件
    private WebView webview;
    public final String Tag = "LocalZiXunDetialsActivity";
    public static long PUBLISH_COMMENT_TIME;
    public long PUBLISH_ZAN_TIME = -1;
    public long PUBLISH_CAI_TIME = -1;
    public ShareWeiXinPlatform shareWeiXinPlatform;

    private Button tljr_btn_news_addCollect, tljr_btn_news_share, tljr_btn_news_AddComment;

    private RelativeLayout tljr_ly_btn_news_addCollect, tljr_ly_btn_news_share, tljr_ly_btn_news_AddComment;

    private View newest_comment;

    int position = 0;
    public News news;
    private ImageView iv_zan, iv_cai;
    private TextView tv_zan_num, tv_cai_num;
    private RelativeLayout ly_zan, ly_cai;
    public Button btn_more;
    public String default_avatar = "drawable://" + R.drawable.img_avatar;

    LinearLayout cmt_ly;
    LinearLayout linear_ly;
    private String id;
    private String time;

    public int red = Color.parseColor("#eb5244");

    public ScrollView scrollView;
    SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tljr_fragment_hqss_news_details);

        activity = this;
        shareWeiXinPlatform = new ShareWeiXinPlatform(this);
        ShareQQPlatform.getInstance().registerShare(this);
        ShareWeiboPlatform.getInstanse().regiesetShare(this, savedInstanceState, this);

        id = getIntent().getStringExtra("id");
        time = getIntent().getStringExtra("time");

        progressView = (CircularProgressView) findViewById(R.id.web_progress_view);
        progressView.setColor(0xffEB5041);
        progressView.setIndeterminate(true);
        loadingLayout = (FrameLayout) findViewById(R.id.loadinglayout);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);

        linear_ly = (LinearLayout)findViewById(R.id.linear_ly);

        cmt_ly = (LinearLayout) findViewById(R.id.cmt_ly);
        newest_comment = findViewById(R.id.cmt_tip);
        // 设置滑动到一半时加载评论
        scrollView = (ScrollView) findViewById(R.id.news_scroll);
        scrollView.setOnTouchListener(touchListener);
		/*
		 * bottom bar
		 */
        tljr_btn_news_addCollect = (Button) findViewById(R.id.tljr_btn_news_addCollect);
        tljr_btn_news_share = (Button) findViewById(R.id.tljr_btn_news_share);
        tljr_btn_news_AddComment = (Button) findViewById(R.id.tljr_btn_news_AddComment);
        tljr_ly_btn_news_addCollect = (RelativeLayout) findViewById(R.id.tljr_ly_btn_news_addCollect);
        tljr_ly_btn_news_share = (RelativeLayout) findViewById(R.id.tljr_ly_btn_news_share);
        tljr_ly_btn_news_AddComment = (RelativeLayout) findViewById(R.id.tljr_ly_btn_news_AddComment);
        tljr_ly_btn_news_addCollect.setOnClickListener(this);
        tljr_ly_btn_news_share.setOnClickListener(this);
        tljr_ly_btn_news_AddComment.setOnClickListener(this);
        tljr_btn_news_addCollect.setOnClickListener(this);
        tljr_btn_news_share.setOnClickListener(this);
        tljr_btn_news_AddComment.setOnClickListener(this);

        tljr_tv_title = (TextView) findViewById(R.id.title);
        tljr_tv_date = (TextView) findViewById(R.id.news_date);
        tljr_tv_news_source = (TextView) findViewById(R.id.news_sources);
        img_piyue = (ImageView) findViewById(R.id.img_piyue);

		/*
		 * webview 控件
		 */
        webview = (WebView) findViewById(R.id.webView1);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        iv_zan = (ImageView) findViewById(R.id.iv_zan);
        iv_cai = (ImageView) findViewById(R.id.iv_cai);
        iv_zan.setOnClickListener(this);
        iv_cai.setOnClickListener(this);
        tv_zan_num = (TextView) findViewById(R.id.tv_zan_num);
        tv_cai_num = (TextView) findViewById(R.id.tv_cai_num);
        tv_zan_num.setOnClickListener(this);
        tv_cai_num.setOnClickListener(this);

        ly_zan = (RelativeLayout) findViewById(R.id.ly_zan);
        ly_cai = (RelativeLayout) findViewById(R.id.ly_cai);
        ly_zan.setOnClickListener(this);
        ly_cai.setOnClickListener(this);

        btn_more = (Button) findViewById(R.id.btn_more);

        initData();

    }

    private String news_Id;
    private void initData() {
        String pid = "0";// 默认无
        if (MyApplication.getInstance().self != null) {
            pid = MyApplication.getInstance().self.getId();
        }
        String url = TLUrl.getInstance().URL_new + "api/detail";
        String params = "species=hq_b" + "&id=" + id + "&time=" + time + "&uid=" + pid;

        LogUtil.i("xuke118", url + "?" + params);
        HttpRequest.sendGet(url, params, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            LogUtil.i(Tag, "singel news:" + msg);

                            JSONObject cmdInfo = new JSONObject(msg);
                            int status = cmdInfo.optInt("status", 0);
                            String message = cmdInfo.optString("msg", "网络连接错误，请稍后重试");
                            if (status == 0) {
                                Toast.makeText(LocalZiXunDetialsActivity.this, message, Toast.LENGTH_LONG).show();
                                return;
                            }

                            JSONObject newsObject = cmdInfo.getJSONObject("joData");
                            news = new News();
                            news.setId(newsObject.optString("id"));
                            news_Id=newsObject.optString("id");
                            news.setDate(newsObject.optString("time"));
                            news.setContent(newsObject.optString("content"));
                            news.setSource(newsObject.optString("source"));
                            news.setSpecial(newsObject.optString("species"));
                            news.setTime(newsObject.optLong("time"));
                            news.setTitle(newsObject.optString("title"));
                            news.setSurl(newsObject.optString("surl"));
                            news.setpUrl(newsObject.optString("purl"));

                            news.setZan(newsObject.optString("allPraise", "0"));
                            news.setCai(newsObject.optString("allOppose", "0"));
                            news.setCollect(newsObject.optString("allCollect", "0"));
                            news.setSurl(newsObject.optString("surl"));

                            news.setHaveZan(newsObject.optBoolean("hasPraise", false));
                            news.setHaveCai(newsObject.optBoolean("hasOppose", false));
                            news.setHaveCollect(newsObject.optBoolean("hasCollect", false));
                            //news.setHaveSee(newsObject.optBoolean("read", false));
                            news.setLoadDetails(true);

                            newsWebView = new LocalZiXunWebView(webview, news, activity);

                            activity.handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    LogUtil.i(Tag, "要执行关掉进度圈");
                                    ChangeNews();
                                    Loading(false);
                                }
                            }, 300);

                            activity.handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    linear_ly.setVisibility(View.VISIBLE);
                                }
                            }, 1000);

                            init();

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    private void ChangeNews() {


        LogUtil.i(Tag, "ChangeNews");
		/*
		 * 头部 新闻 -标题 日期 来源
		 */
        if (news != null && news.getTitle() == null && !Util.isEmptyAndSpace(news.getTitle())) {
            return;
        }
        String title = Util.ToDBC(Util.myTrim(news.getTitle()));
        title = title.length() > 40 ? title.substring(0, 40) + "..." : title;
        tljr_tv_title.setText(title);
        tljr_tv_date.setText(news.getDate());
        tljr_tv_news_source.setText(news.getSource());

        tv_zan_num.setText(news.getZan());
        tv_cai_num.setText(news.getCai());

		/*
		 * webview 内容切换
		 */
        // linear_ly.setVisibility(View.VISIBLE);
        newsWebView.changNews();

		/*
		 * 底部 已收藏，已点赞踩 状态更新
		 */
        bottomBarStateUpdate();
        // /*
        // * 获取评论
        // */

    }

    @Override
    public void onPause() {
        LogUtil.i("read", Tag + ": onPause()");
        // TODO Auto-generated method stub
        super.onPause();
        LogUtil.i(Tag, "isPause");
//        HuanQiuShiShi.gotoDetailsNews = false;
        // scrollView.setFocusable(true);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // LocalZiXunDetialsActivity.handler.postDelayed(r, 5000);
    }

    boolean isScrollLoadComment;

    private FrameLayout loadingLayout;
    private CircularProgressView progressView;

    public void Loading(boolean load) {
        loadingLayout.setVisibility(load ? View.VISIBLE : View.INVISIBLE);
        LogUtil.i(Tag, "loadingLayout visiable:" + loadingLayout.getVisibility());
    }

    @SuppressWarnings("deprecation")
    private void init() {

        refreshLayout.setColorSchemeResources(android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                LogUtil.i(Tag, "------Refresh-------");

                activity.handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        getNewsComment();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout.setRefreshing(false);
                            }
                        },1000);
                    }
                });

            }
        });

        btn_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent it = new Intent(activity, NewsCommentActivity.class);
                it.putExtra("id", news.getId());
                it.putExtra("species", news.getSpecial());
                it.putExtra("time", news.getTime() + "");
                startActivity(it);

            }
        });

    }

    public void addCommentView(ArrayList<Comment> list) {
        cmt_ly.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            final Comment cmt = list.get(i);
            View v = activity.getLayoutInflater().inflate(R.layout.tljr_item_news_details_comment, null);

            LinearLayout layout_child = ((LinearLayout) v.findViewById(R.id.layout_child));
            layout_child.setVisibility(View.VISIBLE);

            if (cmt.getReply() != null && cmt.getReply().length > 0) {
                Reply[] replys = cmt.getReply();
                for (Reply reply : replys) {
                    TextView tv = new TextView(activity);
                    tv.setPadding(10, 10, 10, 10);
                    SpannableString ss = new SpannableString(reply.getNickname() + "： " + reply.getReply());
                    ss.setSpan(new ForegroundColorSpan(red), 0, reply.getNickname().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(ss);
                    layout_child.addView(tv);
                    ;
                }
            }

            final TextView tv_num = (TextView) v.findViewById(R.id.tljr_tx_comment_praise_num);

            if (!cmt.getAurl().equals("default")) {
                StartActivity.imageLoader.displayImage(cmt.getAurl(), ((ImageView) v.findViewById(R.id.img_avatar)), Options.getCircleListOptions());
            } else {
                StartActivity.imageLoader.displayImage(default_avatar, ((ImageView) v.findViewById(R.id.img_avatar)), Options.getCircleListOptions());
            }

            ((TextView) v.findViewById(R.id.tljr_comment_name)).setText(cmt.getName());

            ((TextView) v.findViewById(R.id.tljr_comment_contents)).setText(cmt.getContent());

            ((TextView) v.findViewById(R.id.tljr_comment_time)).setText(cmt.getTime());

            tv_num.setText(cmt.getPraise());

            ((ImageView) v.findViewById(R.id.add_child)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpeakDialog dialog = new SpeakDialog(activity);
                    dialog.setComment(cmt);
                    dialog.show();
                }
            });

            // 评论点赞
            ((RelativeLayout) v.findViewById(R.id.tljr_ly_news_comment_praise)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {

                    if (MyApplication.getInstance().self == null) {
                        activity.showToast("未登录或注册无法完成操作");
                        activity.login();
                        return;
                    } else {

                        ProgressDlgUtil.showProgressDlg("", activity);
                        String url = TLUrl.getInstance().URL_new + "api/uc/cadd";
                        String param = "oper=3&uid=" + MyApplication.getInstance().self.getId() + "&cid=" + cmt.getId() + "&id=" + news.getId();
                        LogUtil.i(Tag, url + "?" + param);
                        HttpRequest.sendPost(url, param, new HttpRevMsg() {

                            @Override
                            public void revMsg(final String msg) {

                                try {
                                    LogUtil.i(Tag, msg);
                                    JSONObject allJson = new JSONObject(msg);

                                    final String status = allJson.getString("status");
                                    final String message = allJson.getString("msg");

                                    activity.handler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (status.equals("1")) {
                                                tv_num.setText(Integer.valueOf(cmt.getPraise()) + 1 + "");
                                                ((ImageView) v.findViewById(R.id.tljr_btn_comment_praise)).setImageResource(R.drawable.img_zan_dianliang);
                                            }
                                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                            ProgressDlgUtil.stopProgressDlg();

                                        }
                                    });


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                    activity.handler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            // TODO Auto-generated method stub
                                            Toast.makeText(activity, "无法连接服务器请稍后再试", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }
                        });
                    }

                }
            });

            ImageView iv = (ImageView) v.findViewById(R.id.img_avatar);
            if (!cmt.getAurl().equals("default")) {
                StartActivity.imageLoader.displayImage(cmt.getAurl(), iv, Options.getCircleListOptions());
            } else {
                StartActivity.imageLoader.displayImage(default_avatar, iv, Options.getCircleListOptions());
            }
            cmt_ly.addView(v);
        }

    }

    public void addCai() {

        if (MyApplication.getInstance().self == null) {
            activity.showToast("未登录或注册无法完成操作");
            activity.login();
            return;
        }
        if (PUBLISH_CAI_TIME > 0) {
            activity.showToast("太快了，休息下吧");
            return;
        }
        PUBLISH_CAI_TIME = 5;
        activity.handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                PUBLISH_CAI_TIME = 0;
            }
        }, 5000);
        activity.handler.post(new Runnable() {

            @Override
            public void run() {

                String url = TLUrl.getInstance().URL_new + "api/uc/oppose";
                String params = "uid=" + MyApplication.getInstance().self.getId() + "&nid=" + news.getId() + "&species=" + news.getSpecial() + "&time=" + news.getTime();

                HttpRequest.sendPost(url, params, new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {

                        LogUtil.i(Tag, msg);
                        activity.handler.post(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    JSONObject allJson = new JSONObject(msg);

                                    String status = allJson.getString("status");
                                    String message = allJson.getString("msg");

                                    if (status.equals("1")) {
                                        JSONObject jodata = allJson.getJSONObject("joData");
                                        String num = jodata.optInt("len", 0) + "";
                                        news.setCai(num);
                                        news.setHaveCai(true);
                                        tv_cai_num.setText(num);
                                        tv_cai_num.setTextColor(getResources().getColor(R.color.redTitlebj));
                                        ImageView btn_cai = (ImageView) findViewById(R.id.iv_cai);
                                        btn_cai.setImageResource(R.drawable.img_news_cai2);
                                    } else {

                                    }
                                    activity.showToast(message);
                                } catch (Resources.NotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                    activity.handler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            // TODO Auto-generated method stub
                                            Toast.makeText(activity, "无法连接服务器请稍后再试", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }
                        });

                    }
                });
            }

        });

    }

    public void addCollect() {

        if (MyApplication.getInstance().self == null) {
            activity.showToast("未登录或注册无法完成操作");
            activity.login();
            return;
        }
        String url = TLUrl.getInstance().URL_new + "api/uc/collect";
        String params = "uid=" + MyApplication.getInstance().self.getId() + "&nid=" + news.getId() + "&species=" + news.getSpecial() + "&time=" + news.getTime();

        LogUtil.i(Tag, url + "?" + params);
        HttpRequest.sendPost(url, params, new HttpRevMsg() {

            @Override
            public void revMsg(final String msg) {

                try {
                    LogUtil.i(Tag, msg);

                    JSONObject allJson = new JSONObject(msg);
                    String status = allJson.getString("status");
                    String message = allJson.getString("msg");
                    if (status.equals("1")) {

                        if (!news.isHaveCollect()) {
                            tljr_btn_news_addCollect.setBackgroundResource(R.drawable.img_news_shoucang2);
                            news.setHaveCollect(true);
                        } else {
                            tljr_btn_news_addCollect.setBackgroundResource(R.drawable.img_news_shoucang1);
                            news.setHaveCollect(false);
                        }

                    } else {

                        // LocalZiXunDetialsActivity.showToast("删除收藏成功");
                        // news.setHaveCollect(false);
                        // tljr_btn_news_addCollect
                        // .setBackgroundResource(R.drawable.img_news_shoucang1);

                    }
                    activity.showToast(message);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    activity.handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Toast.makeText(activity, "无法连接服务器请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }

    public void addZan() {
        if (MyApplication.getInstance().self == null) {
            activity.showToast("未登录或注册无法完成操作");
            activity.login();
            return;
        }
        if (PUBLISH_ZAN_TIME > 0) {
            activity.showToast("太快了，休息下吧");
            return;
        }
        PUBLISH_ZAN_TIME = 5;
        activity.handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                PUBLISH_ZAN_TIME = 0;
            }
        }, 5000);
        activity.handler.post(new Runnable() {

            @Override
            public void run() {
                String url = TLUrl.getInstance().URL_new + "api/uc/praise";
                String params = "uid=" + MyApplication.getInstance().self.getId() + "&nid=" + news.getId() + "&species=" + news.getSpecial() + "&time=" + news.getTime();
                LogUtil.i(Tag, url + "?" + params);
                HttpRequest.sendPost(url, params, new HttpRevMsg() {

                    @Override
                    public void revMsg(final String msg) {

                        LogUtil.i(Tag, msg);
                        activity.handler.post(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    JSONObject allJson = new JSONObject(msg);

                                    String status = allJson.getString("status");
                                    String message = allJson.getString("msg");
                                    if (status.equals("1")) {

                                        JSONObject jodata = allJson.getJSONObject("joData");
                                        String num = jodata.optInt("len", 0) + "";
                                        news.setHaveZan(true);
                                        news.setZan(num);
                                        tv_zan_num.setText(num);

                                        ImageView btn_zan = (ImageView) findViewById(R.id.iv_zan);
                                        btn_zan.setImageResource(R.drawable.img_news_zan2);
                                        tv_zan_num.setTextColor(getResources().getColor(R.color.redTitlebj));
                                    } else {

                                    }
                                    activity.showToast(message);
                                } catch (Resources.NotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                    activity.handler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            // TODO Auto-generated method stub
                                            Toast.makeText(activity, "无法连接服务器请稍后再试", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }
                        });

                    }
                });
            }

        });

    }

    private PopupWindow popupWindow;

    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    private void showPopupView() {
        if (popupWindow == null) {
            // 一个自定义的布局，作为显示的内容
            RelativeLayout contentView = (RelativeLayout) LayoutInflater.from(activity).inflate(R.layout.tljr_dialog_share_news, null);

            ((Button) contentView.findViewById(R.id.btn_cancle)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (popupWindow != null)
                        popupWindow.dismiss();
                }
            });

            LinearLayout ly1 = (LinearLayout) contentView.findViewById(R.id.ly1);

            for (int i = 0; i < ly1.getChildCount(); i++) {
                final int m = i;
                ly1.getChildAt(i).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        shareNewsUrl(m);
                        popupWindow.dismiss();
                    }
                });
            }
            popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
//			popupWindow.getContentView().measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setAnimationStyle(R.style.AnimationPreview);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setAlpha(1f);
                }
            });
        }

        setAlpha(0.8f);
        int[] location = new int[2];
        View v = findViewById(R.id.tljr_news_bottom_f);
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]);
    }

    private void setAlpha(float f) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = f;
        lp.dimAmount = f;
        activity.getWindow().setAttributes(lp);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    // type 0:QQ 1 微信 2新浪微博 3 朋友圈
    private void shareNewsUrl(int type) {
        switch (type) {
            case 0:
                LogUtil.i(Tag, "surl:" + news.getSurl() + "--purl:" + news.getpUrl());
                ShareQQPlatform.getInstance().share(this, news.getSurl(), news.getTitle(), news.getpUrl(), "华人邦", ShareContent.appName);
                break;
            case 1:
                shareWeiXinPlatform.setUrl(news.getSurl());
                shareWeiXinPlatform.setTitle(news.getTitle().length() > 22 ? news
                        .getTitle().substring(0, 22) + "..." : news.getTitle());

                String ct = Util.getTextFromHtml(news.getContent());

                shareWeiXinPlatform.setContent(ct.length() > 26 ? ct.substring(0,
                        26) + "..." : ct);
                shareWeiXinPlatform.wechatShare(0);

                break;
            case 2:
                ShareWeiboPlatform.getInstanse().share(activity,
                        news.getSurl(), news.getTitle(), news.getTitle());
                break;
            case 3:
                shareWeiXinPlatform.setUrl(news.getSurl());
                shareWeiXinPlatform.setTitle(news.getTitle());
                shareWeiXinPlatform.wechatShare(1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tljr_img_news_back: // 返回
                activity.finish();
                break;
            case R.id.tljr_btn_news_addCollect:
            case R.id.tljr_ly_btn_news_addCollect:
                addCollect();
                break;
            case R.id.tljr_btn_news_share:
            case R.id.tljr_ly_btn_news_share:
                if (MyApplication.getInstance().self == null) {
                    activity.showToast("你还没有登录！");
                    activity.login();
                    return;
                }
                showPopupView();
                break;
            case R.id.tljr_btn_news_AddComment:
            case R.id.tljr_ly_btn_news_AddComment:
                if (MyApplication.getInstance().self == null) {
                    activity.showToast("你还没有登录！");
                    activity.login();
                    return;
                }
                new SpeakDialog(activity).show();
                break;
            case R.id.iv_zan:
            case R.id.tv_zan_num:
            case R.id.ly_zan:

                addZan();
                break;
            case R.id.iv_cai:
            case R.id.tv_cai_num:
            case R.id.ly_cai:
                addCai();
                break;
            default:
                break;
        }

    }

    public void getNewsComment() {
        if (Constent.netType.equals("未知")) {
            return;
        }
        String url = TLUrl.getInstance().URL_new + "api/uc/cget";
        String param = "oper=1&id=" + news_Id;
        try {
            // NetUtil.sendPost(TLUrl.getInstance().URL_comment, param, new NetResult() {
            LogUtil.i(Tag, url + "?" + param);
            HttpRequest.sendPost(url, param, new HttpRevMsg() {

                @Override
                public void revMsg(final String msg) {
                    activity.handler.post(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                LogUtil.i(Tag, msg);
                                // newest_comment.setVisibility(View.VISIBLE);
                                ArrayList<Comment> list = new ArrayList<Comment>();
                                JSONObject allJson = new JSONObject(msg);
                                String status = allJson.getString("status");
                                if (status != null && !status.equals("1")) {
                                    return;
                                }
                                JSONObject commentObject = allJson.getJSONObject("joData");
                                JSONArray array = commentObject.getJSONArray("data");
                                if (array == null || array.length() <= 0) {
                                    newest_comment.setVisibility(View.GONE);
                                    return;
                                } else {
                                    newest_comment.setVisibility(View.VISIBLE);
                                }

                                cmt_ly.removeAllViews();
                                for (int i = 0; i < array.length(); i++) {

                                    Comment cmt = new Comment();

                                    cmt.setContent(array.getJSONObject(i).optString("comment"));
                                    cmt.setId(array.getJSONObject(i).optString("id"));

                                    cmt.setTime(HuanQiuShiShi.getStandardDate(array.getJSONObject(i).optLong("insertTime")));

                                    cmt.setNewsId(array.getJSONObject(i).optString("nid"));
                                    cmt.setSpecies(array.getJSONObject(i).optString("species"));
                                    cmt.setUser_id(array.getJSONObject(i).optString("uid"));

                                    cmt.setName(array.getJSONObject(i).optString("nickname"));
                                    cmt.setAurl(array.getJSONObject(i).optString("avatar", "default"));

                                    cmt.setPraise(array.getJSONObject(i).optString("likes", "0"));


                                    JSONArray childArray = array.getJSONObject(i).getJSONArray("replys");
                                    if (childArray != null && childArray.length() > 0) {
                                        Reply[] replys = new Reply[childArray.length()];
                                        for (int g = 0; g < childArray.length(); g++) {
                                            JSONObject ob = childArray.getJSONObject(g);
                                            Reply reply = new Reply();
                                            reply = new Reply();
                                            reply.setReply(ob.optString("reply"));
                                            reply.setNickname(ob.optString("nickname"));
                                            replys[g] = reply;
                                        }
                                        cmt.setReply(replys);
                                    }

                                    list.add(cmt);
                                }

                                int allNum = commentObject.optInt("all");
                                ((TextView) findViewById(R.id.cmt_num)).setText("最新评论(" + allNum + ")");
                                if (allNum > 2) {
                                    btn_more.setVisibility(View.VISIBLE);
                                }

                                addCommentView(list);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                activity.handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(activity, "无法连接服务器请稍后再试", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    });

                }
            });
        } catch (Exception e) {
        }
    }

    private void bottomBarStateUpdate() {
        if (news.isHaveSee()) {
            img_piyue.setVisibility(View.VISIBLE);
        }

        if (news.isHaveCollect()) {
            tljr_btn_news_addCollect.setBackgroundResource(R.drawable.img_news_shoucang2);
        }

        if (news.isHaveZan()) {
            iv_zan.setImageResource(R.drawable.img_news_zan2);
            tv_zan_num.setTextColor(getResources().getColor(R.color.redTitlebj));
        }

        if (news.isHaveCai()) {
            iv_cai.setImageResource(R.drawable.img_news_cai2);
            tv_cai_num.setTextColor(getResources().getColor(R.color.redTitlebj));
        }
    }

    public View.OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    break;
                case MotionEvent.ACTION_MOVE:
                    int scrollY = v.getScrollY();
                    int height = v.getHeight();
                    int scrollViewMeasuredHeight = scrollView.getChildAt(0).getMeasuredHeight();
                    // if(scrollY==0){
                    // System.out.println("滑动到了顶端 view.getScrollY()="+scrollY);
                    // }

                    // LogUtil.i(Tag, "scrollViewMeasuredHeight /2:"
                    // + scrollViewMeasuredHeight / 2);
                    // LogUtil.i(Tag, "scrollY+height :" + scrollY + height);
                    // if((scrollY+height)==scrollViewMeasuredHeight){

                    // 滑动到中间时获取评论
                    if ((scrollY + height) > (scrollViewMeasuredHeight / 2)) {
                        if (!isScrollLoadComment) {
                            getNewsComment();
                        }
                        isScrollLoadComment = true;

                    }
                    break;

                default:
                    break;
            }
            return false;
        }
    };

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //mRealm.close();
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                showToast("分享成功");
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                showToast("取消分享");
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                showToast("分享失败，Error Message: " + baseResponse.errMsg);
                break;
        }
    }

    class SpeakDialog extends Dialog implements View.OnClickListener {
        private LocalZiXunDetialsActivity activity;
        private EditText et;
        private Comment comment;

        public SpeakDialog(LocalZiXunDetialsActivity activity) {
            super(activity, R.style.dialog);
            this.activity = activity;
            setContentView(R.layout.tljr_dialog_speak);
            setCanceledOnTouchOutside(false);
            init();
            windowDeploy();
        }

        public void setComment(Comment comment) {
            this.comment = comment;
        }

        // 设置窗口显示
        public void windowDeploy() {
            Window win = getWindow(); // 得到对话框
            win.setWindowAnimations(R.style.speakdialog_bottom); // 设置窗口弹出动画
            win.getDecorView().setPadding(0, 0, 0, 0); // 宽度占满，因为style里面本身带有padding
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.FILL_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);

        }

        private void init() {

            findViewById(R.id.tljr_img_speak_fanhui).setOnClickListener(this);
            findViewById(R.id.tljr_btn_speak).setOnClickListener(this);
            et = (EditText) findViewById(R.id.tljr_et_speak_msg);
            // et.setFocusable(true);

            activity.handler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    InputTools.ShowKeyboard(et);
                }
            }, 400);

        }

        private void speak() {  // 发表评论
            if (System.currentTimeMillis() - activity.PUBLISH_COMMENT_TIME < 15 * 1000) { // 15秒发言时间间隔
                activity.showToast("太快了，休息下吧");
                return;
            }
            String s = et.getText().toString().trim();
            if (s.equals("")) {
                activity.showToast("请输入评论内容");
                return;
            }
            if (MyApplication.getInstance().self == null) {
                activity.showToast("未登录或注册无法完成操作");
                activity.login();
                return;
            }
            String url = "";
            String params = "";
            String uid = MyApplication.getInstance().self.getId();
            if (comment == null) {
                url = TLUrl.getInstance().URL_new + "api/uc/cadd";
                params = "uid=" + uid + "&oper=1&comment=" + s + "&species=" + news.getSpecial() + "&id=" +news.getId() + "&time=" + news.getTime();
            } else {
                url = TLUrl.getInstance().URL_new + "api/uc/cadd";
                params = "uid=" + uid + "&oper=2&reply=" + s + "&cid=" + comment.getId() + "&id=" + comment.getNewsId();
            }

            LogUtil.i("LocalZiXunDetialsActivity", url + "?" + params);
            ProgressDlgUtil.showProgressDlg("", activity);
            HttpRequest.sendPost(url, params, new HttpRevMsg() {

                @Override
                public void revMsg(final String msg) {

                    activity.handler.post(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                LogUtil.i("LocalZiXunDetialsActivity", "msg:" + msg);

                                JSONObject obj = new JSONObject(msg);
                                if (obj != null) {
                                    String status = obj.optString("status");
                                    if (status.equals("1")) {
                                        Toast.makeText(activity, obj.optString("msg"), Toast.LENGTH_SHORT).show();
                                        dismiss();
                                        getNewsComment();
                                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                        activity.PUBLISH_COMMENT_TIME = System.currentTimeMillis();
                                    } else {
                                        Toast.makeText(activity, obj.optString("msg"), Toast.LENGTH_SHORT).show();
                                    }

                                }
                                ProgressDlgUtil.stopProgressDlg();
                                InputTools.HideKeyboard(et);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                activity.handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(activity, "无法连接服务器请稍后再试", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    });
                }
            });
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tljr_img_speak_fanhui:
                    InputTools.HideKeyboard(et);
                    this.dismiss();
                    break;
                case R.id.tljr_btn_speak:
                    speak();
                    break;
                default:
                    break;
            }
        }
    }
}
