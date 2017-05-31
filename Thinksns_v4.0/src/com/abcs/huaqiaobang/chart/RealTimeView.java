package com.abcs.huaqiaobang.chart;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.chart.CustomerServiceAdapter.RealBean;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.view.XListView;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;


public class RealTimeView {

    Activity activity;
    View RealTimeView;
    XListView listview;
    ArrayList<RealBean> array;
    private CustomerServiceAdapter adapter;
    private int loadingsize = 10;
    private EditText edit;
    private TextView send;
    private String TAG = "RealTimeView";
    private Handler ChartAcHandler;
    public static String im_realtimeid = "";
    private boolean nomore = false;

    public RealTimeView(Activity activity, Handler handler) {
        this.activity = activity;
        ProgressDlgUtil.showProgressDlg("", activity);
        RealTimeView = activity.getLayoutInflater().inflate(R.layout.tljr_chart_realtime, null);
        findView();
        Ask(10, "", 1, false, false);
        ChartAcHandler = handler;

    }

    public View getView() {
        return RealTimeView;
    }

    private void findView() {

        listview = (XListView) RealTimeView.findViewById(R.id.list);
        listview.setPullLoadEnable(false);
//        SimpleHeader header = new SimpleHeader(activity);
//        header.setTextColor(0xffeb5041);
//        header.setCircleColor(0xffeb5041);
//        listview.setHeadable(header);

        // 设置加载更多的样式（可选）
//        SimpleFooter footer = new SimpleFooter(activity);
//        footer.setCircleColor(0xffeb5041);
//        listview.setFootable(footer);
//        listview.startLoadMore();
        listview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

                Log.i("kefu", array.get(0).id);
                Ask(10, array.get(0).id, -1, false, true);
            }

            @Override
            public void onLoadMore() {

            }
        });
//        listview.setOnRefreshStartListener(new OnStartListener() {
//
//            @Override
//            public void onStart() {
//                // TODO Auto-generated method stub
//
//            }
//        });

//        listview.setOnLoadMoreStartListener(new OnStartListener() {
//
//            @Override
//            public void onStart()
//            {
//            	if(nomore){
//            		listview.setLoadMoreSuccess();
//            	}else{
//            		Ask(10, array.get(array.size()-1).id, -1,false,true);
//            	}
//            }
//        });

        if (MyApplication.getInstance().self == null) {
            MobclickAgent.onEvent(activity, "Login");
            Intent intent = new Intent(activity, WXEntryActivity.class);
            activity.startActivity(intent);
            activity.overridePendingTransition(
                    R.anim.move_left_in_activity,
                    R.anim.move_right_out_activity);
            return;
        }
//		if(MyApplication.getInstance().self.isIsadmin()){
//			RealTimeView.findViewById(R.id.yzx_bottom).setVisibility(View.VISIBLE);
//		}else{
//			RealTimeView.findViewById(R.id.yzx_bottom).setVisibility(View.GONE);
//		}

        edit = (EditText) RealTimeView.findViewById(R.id.im_send_text);
        edit.addTextChangedListener(warcher);
        send = (TextView) RealTimeView.findViewById(R.id.im_send_btn);
        send.setOnClickListener(onclick);

        array = new ArrayList<RealBean>();
//		adapter = new RealTimeAdapter(activity, array);
//		listview.setAdapter(adapter);
        handler.post(run);
    }

    public Runnable run = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
//			((ChartActivity)activity).reflushDP("sh", "000001", ((ChartActivity)activity).hushenview);
//			((ChartActivity)activity).reflushDP("sz", "399001", ((ChartActivity)activity).hushenview);
//			((ChartActivity)activity).reflushDP("sz","399006",((ChartActivity)activity).zhongchuanview);
//			((ChartActivity)activity).reflushDP("sz","399005",((ChartActivity)activity).zhongchuanview);
            Log.i("RealTimeView", "RealTimeView run");
//            if (array != null && array.size() > 0) {
//                Ask(10, array.get(0).id, 1, false, true);
//            } else {
//                Ask(10, "", 1, false, false);
//            }
            if (((ChartActivity) activity).iskaishi) {
                ChartAcHandler.sendEmptyMessage(2);
            }
            if (((ChartActivity) activity).weseeview.array.size() > 0) {
                ((ChartActivity) activity).weseeview.Ask(10, ((ChartActivity) activity).weseeview.array.get(((ChartActivity) activity).weseeview.array.size() - 1).id, 1, true, false);
            } else {
                ((ChartActivity) activity).weseeview.Ask(10, "", 1, false, false);
            }
            handler.postDelayed(run, 5000);
        }
    };

    //获得列表
    public synchronized void Ask(int size, String id, int direction, final boolean isadd, final boolean needmore) {
        try {
            String param = "size=" + size;
            if (!id.equals("")) {
                param += "&id=" + id;
//                param += "&uid=" + id + ;
            }
            param += "&uid=" + MyApplication.getInstance().self.getId() + "&direction=" + direction + "&token=" + Util.token;
            Log.i(TAG, param);
            //&mobile=   &avatar=
            HttpRequest.sendGet(TLUrl.getInstance().URL_goods_base+"/HQChat/rest/online/getList",
                    param
                    , new HttpRevMsg() {
                        @Override
                        public void revMsg(String msg) {
                            // TODO Auto-generated method stub
                            Log.d(TAG, "RealTime :" + msg + "   isadd:" + isadd);
                            if (msg.equals("error")) {
                                Toast.makeText(activity, "获取失败，请重新刷新", Toast.LENGTH_LONG).show();
                                ProgressDlgUtil.stopProgressDlg();
                            } else {
                                JSONObject js = JSONObject.parseObject(msg);
                                if (js.getInteger("status") == 1) {
                                    Log.d(TAG, "RealTime status");
//                                    handler.sendEmptyMessage(1);
                                    JSONArray arr = js.getJSONArray("msg");
                                    Log.d(TAG, "RealTime status 2" + arr);

                                    if (arr.size() == 0) {
//                                        ProgressDlgUtil.stopProgressDlg();
                                        handler.sendEmptyMessage(5);
//
                                        Log.d(TAG, "RealTime status 2" + arr);


                                        return;
                                    }
                                    if (needmore) {
                                        ArrayList<RealBean> list = new ArrayList<RealBean>();
                                        Log.d(TAG, "RealTime 1");
                                        for (int i = arr.size() - 1; i >= 0; i--) {
                                            Log.d(TAG, "RealTime 2");
                                            JSONObject ob = arr.getJSONObject(i);
                                            RealBean real = new RealBean();
                                            real.id = ob.getString("id");
                                            real.uid = ob.getString("uid");
                                            real.name = ob.getString("nickName");
                                            real.msg = ob.getString("msg");
                                            real.image = ob.getString("avatar");
                                            Log.d(TAG, "RealTime 2");
                                            real.time = ob.getLongValue("date");
                                            list.add(real);
                                        }
                                        if (list.size() > 0) {
                                            array.addAll(0, list);
                                            handler.sendEmptyMessage(4);
                                        } else {
                                            nomore = false;
                                            Toast.makeText(activity, "没有历史消息", Toast.LENGTH_LONG).show();
                                            listview.stopRefresh();
                                        }
                                    } else {
                                        if (isadd) {
                                            for (int i = arr.size() - 1; i <= 0; i--) {
                                                JSONObject ob = arr.getJSONObject(i);
                                                RealBean real = new RealBean();
                                                real.id = ob.getString("id");
                                                real.uid = ob.getString("uid");
                                                real.name = ob.getString("nickName");
                                                real.msg = ob.getString("msg");
                                                real.time = ob.getLongValue("date");
                                                real.image = ob.getString("avatar");
                                                array.add(0, real);
                                            }

                                        } else {
                                            Log.d(TAG, "RealTime 1");
                                            ArrayList<RealBean> list = new ArrayList<RealBean>();
                                            Log.d(TAG, "RealTime 1");
                                            for (int i = arr.size() - 1; i >= 0; i--) {
                                                Log.d(TAG, "RealTime 2");
                                                JSONObject ob = arr.getJSONObject(i);
                                                RealBean real = new RealBean();
                                                real.id = ob.getString("id");
                                                real.uid = ob.getString("uid");
                                                real.name = ob.getString("nickName");
                                                real.msg = ob.getString("msg");
                                                real.image = ob.getString("avatar");
                                                Log.d(TAG, "RealTime 2");
                                                real.time = ob.getLongValue("date");
                                                list.add(real);
                                            }
                                            Log.d(TAG, "RealTime 3");
                                            array.clear();
                                            array.addAll(list);
                                            ProgressDlgUtil.stopProgressDlg();
                                            handler.sendEmptyMessage(0);
                                        }
                                    }
                                } else {
                                    ProgressDlgUtil.stopProgressDlg();
                                    Toast.makeText(activity, "获取信息错误，请重新刷新", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //发表
    public void send(final String msg) {
        try {

            RequestParams params = new RequestParams();
            params.addBodyParameter("token", Util.token);
            params.addBodyParameter("uid", MyApplication.getInstance().self.getId());
            params.addBodyParameter("msg", msg);
            XUtilsHelper.sendPost(TLUrl.getInstance().URL_goods_base+"/HQChat/rest/online/send",
                    params, new HttpCallback() {
                        @Override
                        public void callback(String data) {
                            // TODO Auto-generated method stub
                            Log.d(TAG, "Real send :" + data);
                            if (data.equals("error")) {
                            } else if (data.equals("")) {
                                handler.sendEmptyMessage(3);
                            } else {
                                JSONObject js = JSONObject.parseObject(data);
                                if (js.getInteger("status") == 1) {
                                    Message msgMessage = Message.obtain();

                                    msgMessage.obj = msg;
                                    msgMessage.what = 2;
                                    handler.sendMessage(msgMessage);
                                } else {
                                    Log.d("ChartActivity", "Real send status是不是0:" + js.getString("msg"));
                                }

                            }
                        }
                    });
            //&mobile=   &avatar=
            Log.d(TAG, "trl :" + "http://120.24.235.202:8080/HQChat/rest/real/send" +
                    "?token=" + Constent.device_token + "&uid=" + MyApplication.getInstance().self.getId()
                    + "&msg=" + msg);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.d(TAG, "array .size :" + array.size());
                    adapter = new CustomerServiceAdapter(activity, array);
//				adapter = new RealTimeAdapter(activity, array);
                    listview.setAdapter(adapter);
                    listview.setSelection(adapter.getCount() - 1);
//				adapter.notifyDataSetChanged();
//				listview.setSelection(listview.FOCUS_DOWN);
                    Constent.preference.edit().putString("im_realtimeid", array.get(array.size() - 1).id).commit();
                    im_realtimeid = array.get(array.size() - 1).id;
                    nomore = false;
//                    listview.startLoadMore();
                    listview.setRefreshTime(Util.getDateDayhhmm(System.currentTimeMillis()));
                    listview.stopRefresh();
                    break;
                case 1:

//                    adapter.notifyDataSetChanged();
//				listview.setSelection(listview.FOCUS_DOWN);
                    break;
                case 2:
                    Toast.makeText(activity, "发送成功", Toast.LENGTH_LONG).show();

                    if (MyApplication.getInstance().self != null) {
                        RealBean realBean = new RealBean();
                        realBean.uid = MyApplication.getInstance().self.getId();
                        realBean.name = MyApplication.getInstance().self.getNickName();
                        realBean.image = MyApplication.getInstance().self.getAvatarUrl();
                        realBean.time = System.currentTimeMillis();
                        realBean.msg = msg.obj.toString();
                        array.add(realBean);

                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                            listview.setSelection(adapter.getCount() - 1);
                        } else {
                            adapter = new CustomerServiceAdapter(activity, array);
                            listview.setAdapter(adapter);
                            listview.setSelection(adapter.getCount() - 1);
                        }
                        im_realtimeid = array.get(array.size() - 1).id;
                        Constent.preference.edit().putString("im_realtimeid", array.get(array.size() - 1).id).commit();
                    }
//				realBean.=System.currentTimeMillis();
                    break;
                case 3:
                    Toast.makeText(activity, "发送失败，请重新发送或询问服务器", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    adapter.notifyDataSetChanged();
                    listview.stopRefresh();
//                    listview.setSelection(adapter.getCount() - 1);
                    break;
                case 5:
                    Toast.makeText(activity, "无更多历史数据", Toast.LENGTH_LONG).show();
                    ProgressDlgUtil.stopProgressDlg();
                    listview.stopRefresh();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private OnClickListener onclick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.im_send_btn:
                    send(edit.getText().toString());
                    edit.setText("");
                    break;
                default:
                    break;
            }
        }
    };

    private TextWatcher warcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            if (edit.getText().toString().equals("")) {
                send.setVisibility(View.GONE);
            } else {
                send.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }
    };


    public static int time = 0;

    public static void CheckNewIMReal() {
        try {
            Log.d("OneAskActivity", "im_realtimeid :" + im_realtimeid);
//			Log.d("OneAskActivity", "time :"+time+" islive :"+ChartActivity.islive+"  im_realtimeid:"+im_realtimeid);
            if (ChartActivity.islive) {
                return;
            }
//			if(im_realtimeid.equals("")){
//				return;
//			}
//			time++;
//			if(time<5){
//				return;
//			}
//			time = 0;
            Log.d("OneAskActivity", "im_realtimeid :" + im_realtimeid);
            if (MyApplication.getInstance().self != null) {
                String param = "size=10&id=" + im_realtimeid + "&direction=1" + "&uid=" + MyApplication.getInstance().self.getId() + "&token=" + Util.token;
                //&mobile=   &avatar=
                HttpRequest.sendGet(TLUrl.getInstance().URL_customer_service,
                        param, new HttpRevMsg() {
                            @Override
                            public void revMsg(String msg) {
                                // TODO Auto-generated method stub
                                Log.d("OneAskActivity", "check :" + msg);
                                if (msg.equals("error")) {
                                } else {
                                    JSONObject js = JSONObject.parseObject(msg);
                                    if (js.getInteger("status") == 1) {
                                        JSONArray arr = js.getJSONArray("msg");
                                        if (arr.size() > 0) {

                                            Message message = Message.obtain();
                                            message.arg1 = arr.size();
                                            message.what = 95;
                                            Log.i("tga", "sendmessage====" + arr.size());
                                            MyApplication.getInstance().getMainActivity().mHandler.sendMessage(message);
                                        }
                                    }
                                }
                            }
                        });
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
