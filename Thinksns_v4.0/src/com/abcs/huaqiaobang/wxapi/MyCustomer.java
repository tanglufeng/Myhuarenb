package com.abcs.huaqiaobang.wxapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.CustomOffline;
import com.abcs.huaqiaobang.model.Customer;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.official.share.ShareQQPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.ShareWeiXinPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.ShareWeiboPlatform;
import com.abcs.huaqiaobang.wxapi.official.share.util.ShareContent;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyCustomer extends BaseActivity {

    private View customerView;
    private ImageView img_head;
    private TextView txt_id;
    private TextView txt_nichen;
    private ImageLoader imageLoader;
    private Handler handler;
    private ArrayList<Customer> customer_list;
    private ArrayList<CustomOffline> activity_list;
    private TextView vipcode;
    private TextView custom_count;
    private TextView vip_count;
    private String id;
    private int page = 1;
    private ListView act_listview;
    private TextView address, starttime, endtime, state;
    private ShareWeiXinPlatform shareWeiXinPlatform;
    private Customer customer;
    private TextView huodong_count;
    private ActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        shareWeiXinPlatform = new ShareWeiXinPlatform(this);
        ShareQQPlatform.getInstance().registerShare(this);
        customerView = getLayoutInflater().inflate(
                R.layout.occft_activity_customer, null);
        View headerView=LayoutInflater.from(this).inflate(R.layout.activity_customer_header,null);
        img_head = (ImageView) headerView.findViewById(R.id.occft_head);
        txt_id = (TextView) headerView.findViewById(R.id.occft_id);
        txt_nichen = (TextView) headerView.findViewById(R.id.occft_nichen);
        huodong_count = (TextView) headerView.findViewById(R.id.huodong_count);
        vipcode = (TextView) headerView.findViewById(R.id.occft_qunhao);
        custom_count = (TextView) headerView.findViewById(R.id.custom_count);
        vip_count = (TextView) headerView.findViewById(R.id.vip_count);
        act_listview = (ListView) customerView.findViewById(R.id.listview_xiangqing);
        act_listview.addHeaderView(headerView);
        address = (TextView) headerView.findViewById(R.id.txt_server_address);
        starttime = (TextView) headerView.findViewById(R.id.txt_shengxiao_time);
        endtime = (TextView) headerView.findViewById(R.id.txt_daoqi_time);
        state = (TextView) headerView.findViewById(R.id.state);
        customerView.findViewById(R.id.promptly_invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopupView();
            }
        });
        handler = new Handler();
        customer_list = new ArrayList<Customer>();
        activity_list = new ArrayList<>();
        if (checkNetWork()) {
            initData();
        } else {
            Toast.makeText(this, "网络不好,稍后重试", Toast.LENGTH_SHORT).show();
            finish();
        }
        setContentView(customerView);


    }

    public boolean checkNetWork() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        // 检查网络连接，如果无网络可用，就不需要进行连网操作等
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null || !manager.getBackgroundDataSetting()) {
            return false;
        } else {
            return true;
        }
    }

    private void initData() {
        // TODO Auto-generated method stub
        Intent intent = getIntent();
        String img_head_url = MyApplication.getInstance().self.getAvatarUrl();
        id = MyApplication.getInstance().self.getId();
        String name = MyApplication.getInstance().self.getNickName();
        txt_id.setText(id);
        txt_nichen.setText(name);

        imageLoader = ImageLoader.getInstance();


        String param = "method=getoffline&uid=" + id + "&token=" + Util.token;
        Log.i("tga", "custormparam===" + param);
        imageLoader.displayImage(img_head_url,
                img_head, Options.getCircleListOptions());


        //获取offline

        String msg = getIntent().getStringExtra("msg");
//        HttpRequest.sendGet(TLUrl.getInstance().URL_customer, param, new HttpRevMsg() {
//            @Override
//            public void revMsg(final String msg) {
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {

        Log.i("tga", "" + msg);
        JSONObject custom = null;
        try {
            custom = new JSONObject(msg);
            if (custom.length() != 0 && custom.getInt("status") == 1) {
                JSONObject custom_msg = custom.getJSONObject("msg");

                if (custom_msg.length() >= 0) {
                    customer = new Customer();
                    customer.setDirectcount(custom_msg.getString("directcount"));
                    customer.setIndirectcount(custom_msg.getString("indirectcount"));
                    customer.setVipcode(custom_msg.getString("vipcode"));
                    customer.setViplevel(custom_msg.getString("viplevel"));
                    customer.setCountry(custom_msg.getString("country"));
                    customer.setEndTime(custom_msg.getLong("endTime"));
                    customer.setStartTime(custom_msg.getLong("startTime"));
//                            customer_list.add(customer);
                    initCustom(customer);
//                            }
//                        } else {//出错处理
//                            startActivity(new Intent(MyCustomer.this, NoticeDialogActivity.class).putExtra("msg", "您还没有该权限"));

//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    try {
//                                        Thread.sleep(1000);
//                                        finish();
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }).start();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


//                }, 0);
//            }
//        });
        //获取下线活动  size
        getoffLineActivity(10);

    }

    private void getoffLineActivity(int size) {

        String param = "method=getMyCustomerActivity&size=" + size + "&uid=" + id + "&token=" + Util.token + "&page=" + page;
        HttpRequest.sendGet(TLUrl.getInstance().URL_customer, param, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject activity_object = null;
                        try {
                            System.out.print("==========:"+msg);
                            activity_object = new JSONObject(msg);
                            if (activity_object.getInt("status") == 1) {
                                JSONArray activity_msg = activity_object.getJSONArray("msg");

                                if (activity_msg.length() == 0) {
                                    return;
                                }

                                for (int i = 0; i < activity_msg.length(); i++) {
                                    CustomOffline offline = new CustomOffline();
//                                offline.set_id(activity_msg.getJSONObject(i).getString("_id"));
                                    offline.setDate(activity_msg.getJSONObject(i).getString("date"));
                                    offline.setErrMsg(activity_msg.getJSONObject(i).getString("errMsg"));
                                    offline.setId(activity_msg.getJSONObject(i).getString("id"));
                                    offline.setUid(activity_msg.getJSONObject(i).getString("uid"));
                                    offline.setMoney(activity_msg.getJSONObject(i).getString("money"));
                                    offline.setNickname(activity_msg.getJSONObject(i).getString("nickname"));
                                    offline.setName(activity_msg.getJSONObject(i).getString("name"));

                                    activity_list.add(offline);
                                }
                                showData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }

    private void showData() {

        huodong_count.setText("(" + activity_list.size() + ")");
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }else {
            adapter = new ActivityAdapter(activity_list, this);
            act_listview.setAdapter(adapter);
            act_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if(firstVisibleItem+visibleItemCount==totalItemCount-1){
                            page++;
                            getoffLineActivity(3);
                        }

                }
            });

        }

    }

    private class ActivityAdapter extends BaseAdapter {


        Context context;
        ArrayList<CustomOffline> mData;

        public ActivityAdapter(ArrayList<CustomOffline> activity_list, Context context) {

            this.mData = activity_list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            CustomOffline offline = (CustomOffline) getItem(position);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_listview_itme, null);

                holder = new ViewHolder();
                holder.desc = (TextView) convertView.findViewById(R.id.act_desc);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.desc.setText(offline.getNickname() + "(id:" + offline.getUid() + ") "
                            + Util.getDate(Long.parseLong(offline.getDate())) + " " + offline.getName() + " " +
                            Util.formatMoney(Integer.valueOf(offline.getMoney()) / 100) + "元"
            );


            return convertView;
        }

        private class ViewHolder {
            TextView desc;
        }
    }

    private void initCustom(Customer customer) {

        vipcode.setText(customer.getVipcode());
        custom_count.setText(customer.getDirectcount());
        vip_count.setText(customer.getIndirectcount());
        address.setText("默认服务范围:" + customer.getCountry());
        starttime.setText(Util.getDateOnlyDat(customer.getStartTime()) + "生效");
        endtime.setText(Util.getDateOnlyDat(customer.getEndTime()) + "到期");

        if (customer.getStartTime() > customer.getEndTime()) {
            state.setText("当前:无效");
        } else {
            state.setText("当前:有效");

        }


    }

    private PopupWindow popupWindow;

    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    private void showPopupView() {
        if (popupWindow == null) {
            // 一个自定义的布局，作为显示的内容
            RelativeLayout contentView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tljr_dialog_share_news, null);

            ((Button) contentView.findViewById(R.id.btn_cancle)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (popupWindow != null)
                        popupWindow.dismiss();
                }
            });

            LinearLayout ly1 = (LinearLayout) contentView.findViewById(R.id.ly1);

            ly1.getChildAt(2).setVisibility(View.GONE);
            ly1.getChildAt(3).setVisibility(View.GONE);
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
        View v = customerView.findViewById(R.id.promptly_invite);
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]);
    }

    private void setAlpha(float f) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = f;
        lp.dimAmount = f;
        this.getWindow().setAttributes(lp);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }


    // type 0:QQ 1 微信 2新浪微博 3 朋友圈
    private void shareNewsUrl(int type) {
        switch (type) {
            case 0:
                ShareQQPlatform.getInstance().share(this, ShareContent.actionUrl, "欢迎加入我的客户群，vip群码:" + customer.getVipcode(), ShareContent.imgQQ_Url, null, ShareContent.appName);


                break;
            case 1:
                shareWeiXinPlatform.setUrl(ShareContent.actionUrl);
                shareWeiXinPlatform.setTitle("欢迎加入我的客户群，vip群码:" + customer.getVipcode());

//                String ct = Util.getTextFromHtml("");
//
//                shareWeiXinPlatform.setContent(ct.length() > 26 ? ct.substring(0,
//                        26) + "..." : ct);
                shareWeiXinPlatform.wechatShare(0);

                break;
            case 2:
                ShareWeiboPlatform.getInstanse().share(this,
                        "", "", "");
                break;
            case 3:
                shareWeiXinPlatform.setUrl("123");
                shareWeiXinPlatform.setTitle("123");
                shareWeiXinPlatform.wechatShare(1);
                break;
            default:
                break;
        }
    }

    public void onBack(View v) {
        finish();
    }

}
