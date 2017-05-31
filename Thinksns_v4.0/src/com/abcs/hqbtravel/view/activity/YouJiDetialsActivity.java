package com.abcs.hqbtravel.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.utils.TraCommeDialog;
import com.abcs.hqbtravel.biz.YouJiDetialsBiz;
import com.abcs.hqbtravel.entity.YouJiDetials;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.CircleImageView;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YouJiDetialsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView img_back,iv_menu1,iv_menu3;
    private String youjiId;
    //    private int youjiId=1;
    private TextView tv_youji_numbers;
    private TextView rr,tv_comment_num,tv_top_num;
    private TextView tv_youji_time;
    private TextView tv_youji_name;
    private TextView tv_youji_title,tv_menu;
    private TextView tv_youji_content;
    private WebView webView;
    private ImageView ivMenu;
    private List<YouJiDetials.BodyBean.PoiNamesBean> poiNamesList;

    private DrawerLayout drawerLayout;
//    private LinearLayout drawerContent;
    private ListView drawerview;
    TraCommeDialog mTraCommeDialog;

    private CircleImageView img_youji_dec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_jidetials);

        youjiId = getIntent().getStringExtra("youjiId");
        img_back = (ImageView) findViewById(R.id.img_back);
        iv_menu1 = (ImageView) findViewById(R.id.iv_menu1);
        iv_menu3 = (ImageView) findViewById(R.id.iv_menu3);
        tv_comment_num = (TextView) findViewById(R.id.tv_comment_num);
        tv_top_num = (TextView) findViewById(R.id.tv_top_num);
        tv_comment_num.setOnClickListener(this);
        img_back.setOnClickListener(this);
        iv_menu1.setOnClickListener(this);
        iv_menu3.setOnClickListener(this);
        img_youji_dec = (CircleImageView) findViewById(R.id.img_avatar);
        tv_youji_numbers = (TextView) findViewById(R.id.tv_youji_numbers);
        rr = (TextView) findViewById(R.id.rr);
        tv_youji_time = (TextView) findViewById(R.id.tv_youji_time);
        tv_youji_name = (TextView) findViewById(R.id.tv_youji_name);
        tv_youji_title = (TextView) findViewById(R.id.tv_youji_title);
        tv_youji_content = (TextView) findViewById(R.id.tv_youji_content);
        webView = (WebView) findViewById(R.id.webView);
        ivMenu = (ImageView) findViewById(R.id.iv_menu);
        tv_menu = (TextView) findViewById(R.id.tv_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerContent = (LinearLayout) findViewById(R.id.drawer_content);
        drawerview = (ListView) findViewById(R.id.poi_listview);

        mTraCommeDialog=new TraCommeDialog(this,youjiId);
        ivMenu.setOnClickListener(this);
        tv_menu.setOnClickListener(this);
        poiNamesList = new ArrayList<>();

        if (!TextUtils.isEmpty(youjiId)) {
            getYouJiDetials();
        }

        adapter = new PoiNamesAdapter(this,poiNamesList);
        drawerview.setAdapter(adapter);
        drawerview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String anchor = poiNamesList.get(position).anchor;
                Log.i("xuke28","精彩游记的anchor:"+anchor);
                drawerLayout.closeDrawers();
                getData(anchor);
            }
        });

        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");//设置编码格式
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(false);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(false);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(webViewClient);
    }


    WebViewClient webViewClient = new WebViewClient(){
        /**
         * 多页面在同一个WebView中打开，就是不新建activity或者调用系统浏览器打开
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };


    private void getYouJiDetials() {
        ProgressDlgUtil.showProgressDlg("Loading...", this);

        YouJiDetialsBiz.getInstance(this).getYouJiDetials(youjiId, new Response.Listener<YouJiDetials>() {
            @Override
            public void onResponse(YouJiDetials response) {
                if (response != null) {
                    ProgressDlgUtil.stopProgressDlg();

                    if (response.result == 1) {
                        MyApplication.imageLoader.displayImage(response.body.avatar, img_youji_dec, MyApplication.getAvatorOptions());

                        tv_top_num.setText(response.body.travelNoteTopCount+"");
                        tv_comment_num.setText(response.body.travelNoteCommentCount+"");

                        if (response.body.read > 0) {
                            rr.setVisibility(View.VISIBLE);
                            tv_youji_numbers.setText(response.body.read + "");
                        } else {
                            rr.setVisibility(View.GONE);
                            tv_youji_numbers.setVisibility(View.GONE);
                        }
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        tv_youji_time.setText(format.format(new Date(Long.valueOf(response.body.time))) + "");
                        tv_youji_name.setText(response.body.nickName + "");
                        tv_youji_title.setText(response.body.title + "");
//                      tv_youji_content.setText(response.body.content);
//                      webView.loadData(response.body.content, "text/html", "UTF-8");  //加载定义的代码，并设定编码格式和字符集。;
//                      webView.loadDataWithBaseURL("", response.body.content, "text/html", "UTF-8", "");  //加载定义的代码，并设定编码格式和字符集。;
                        Log.i("xuke28","游记详情:"+"http://120.24.19.29:7075/travelnotedetail/getNoteContent?id="+youjiId);
                        webView.loadUrl(TLUrl.getInstance().URL_BASE+"/travel/travelnotedetail/getNoteContent?id="+youjiId);

                        if (response.body.poiNames != null) {
                            if(response.body.poiNames.size() > 0){
                                ivMenu.setClickable(true);
                                tv_menu.setClickable(true);
                                poiNamesList.addAll(response.body.poiNames);
                            }else {
                                ivMenu.setClickable(false);
                                tv_menu.setClickable(false);
                            }
                        }

//                        if (response.body.poiNames != null && response.body.poiNames.size() > 0) {
//                            adapter.addAll(response.body.poiNames);
//                            adapter.notifyDataSetChanged();
//                        } else {
//                            Toast.makeText(YouJiDetialsActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
//                        }

                    }

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(YouJiDetialsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                ProgressDlgUtil.stopProgressDlg();
            }
        }, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.iv_menu3:
            case R.id.tv_comment_num:
                if(MyApplication.getInstance().getUid()==0){
                    Intent intent = new Intent(this, WXEntryActivity.class);
                    intent.putExtra("isthome",true);
                    startActivity(intent);
                    return;
                }else {
                    if(mTraCommeDialog!=null){
                        mTraCommeDialog.show();

                        mTraCommeDialog.setGetComCount(new TraCommeDialog.getComCount() {
                            @Override
                            public void setCount(int count) {
                                tv_comment_num.setText(count+"");
                            }
                        });
                    }
                }
                break;
            case R.id.iv_menu1:  // 游记置顶

                if(MyApplication.getInstance().getUid()==0){
                    Intent intent = new Intent(this, WXEntryActivity.class);
                    intent.putExtra("isthome",true);
                    startActivity(intent);
                    return;
                }else {
                    initNoteTop();
                }
                break;

            case R.id.iv_menu:
            case R.id.tv_menu:
//                showpopupwindow();
                drawerLayout.openDrawer(drawerview);
                break;
        }
    }

    Handler mHandler=new Handler();
    private void initNoteTop(){
        //  http://120.24.19.29:7075/note/travelNoteTop?id=110008&uid=10666
        HttpRequest.sendGet("http://120.24.19.29:7075/note/travelNoteTop", "id="+youjiId+"&uid="+MyApplication.getInstance().getUid(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(!TextUtils.isEmpty(msg)){
                            Log.e("zdsoteTop",msg+"");

                            try {
                                JSONObject object=new JSONObject(msg);

                                if(object.optInt("result")==1){
                                    JSONObject bodg=object.optJSONObject("body");
                                    if(bodg!=null&&bodg.optInt("count")>0){
                                        tv_top_num.setText(bodg.optInt("count")+"");
                                    }

                                    Toast.makeText(YouJiDetialsActivity.this,object.optString("info"),Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(YouJiDetialsActivity.this,object.optString("info"),Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    private PopupWindow popupWindow;
    private PoiNamesAdapter adapter;

    private void showpopupwindow() {
        View itemView = View.inflate(this, R.layout.popup_youji_poiname, null);
        final ListView poiListView = (ListView) itemView.findViewById(R.id.poi_listview);

        adapter = new PoiNamesAdapter(this,poiNamesList);
        poiListView.setAdapter(adapter);
        poiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String anchor = poiNamesList.get(position).anchor;
                Log.i("xuke28","精彩游记的anchor:"+anchor);
                popupWindow.dismiss();
                getData(anchor);
            }
        });

        popupWindow = new PopupWindow(itemView, Util.WIDTH,Util.HEIGHT*1/2);
        //触摸点击事件
        popupWindow.setTouchable(true);
        //聚集
        popupWindow.setFocusable(true);
        //设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        //点击返回键popupwindown消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //背景变暗
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //监听如果popupWindown消失之后背景变亮
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow()
                        .getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ffffff")));
        popupWindow.showAtLocation(findViewById(R.id.inf), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    private void getData(String anchor) {
        Log.i("xuke28","跳转url:"+"http://120.24.19.29:7075/travelnotedetail/getNoteContent?id="+youjiId+anchor);
        webView.loadUrl(TLUrl.getInstance().URL_BASE+"/travel/travelnotedetail/getNoteContent?id="+youjiId+anchor);
    }

    class PoiNamesAdapter extends BaseAdapter {
        private List<YouJiDetials.BodyBean.PoiNamesBean> list;
        private Context context;

        public PoiNamesAdapter(Context context,List<YouJiDetials.BodyBean.PoiNamesBean> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(context,R.layout.item_youjidetail_text,null);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            tv.setText((position+1)+"."+list.get(position).name);
            return view;
        }
    }
}