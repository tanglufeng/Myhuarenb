package com.abcs.huaqiaobang.chart;
 
import android.app.Activity;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.chart.PullToRefreshListView.OnRefreshListener;
import com.abcs.huaqiaobang.chart.WeSeeAdapter.WeseeBean;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.http.RequestParams;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.ArrayList;


public class WeSeeView {
	Activity activity;
	View RealTimeView;
	PullToRefreshListView listview;
	public ArrayList<WeseeBean> array;
	private WeSeeAdapter adapter;
	private RelativeLayout rl_bedown;
	private EditText edit;
	private TextView send,bedown_number;
	private String TAG = "WeSeeView";
	private long uptime = 0;
	private int loadingsize = 10;
	private int position = 0;
	private boolean isloading = false;
	private String tempid = "";
	private int noshownumber = 0;

	private String  getlist_url=TLUrl.getInstance().URL_goods_base+"/HQChat/rest/real/getlist";
	private String  send_url= TLUrl.getInstance().URL_goods_base+"/HQChat/rest/real/send";

	public WeSeeView(Activity activity) {
		this.activity = activity;
		RealTimeView = activity.getLayoutInflater().inflate(
				R.layout.tljr_chart_edit2, null);
		findView();
//		initData(true);
		Ask(10,"",1,false,false);
	}
	public WeSeeView(Activity activity,String getlist_url,String send_url) {
		this.activity = activity;
		RealTimeView = activity.getLayoutInflater().inflate(
				R.layout.tljr_chart_edit2, null);
		findView();
//		initData(true);
		this.getlist_url=getlist_url;
		this.send_url=send_url;
		Ask(10,"",1,false,false);
	}

	public View getView() {
		return RealTimeView;
	}

	
	//获得列表
	   public void Ask(int size,String id,int direction,final boolean updatanews,final boolean isaddhistory){
		   try {
	        	String param = "size="+size+"&type=1";
	        	if(!id.equals("")){
	        		param += "&id="+id+"&direction="+direction;
	        	}
	        	if(isaddhistory){
	        		if(!tempid.equals("")){
	        			if(tempid.equals(id)){
	        				return;
	        			}
	        		}else{
	        			tempid = id;
	        		}
	        	}
	        	 HttpRequest.sendGet(getlist_url,
	                    param
	                    ,new HttpRevMsg() {
	                @Override
	                public void revMsg(String msg) {
	                    // TODO Auto-generated method stub
	                    Log.d(TAG, "WeSeeView getdata:"+msg+"   isadd:"+updatanews);
	                    if(msg.equals("error")){
	                    	if(!isaddhistory && !updatanews){
	                    		handler.sendEmptyMessage(7);
	                    	}
	                    }else{
	                        JSONObject js = JSONObject.parseObject(msg);
	                        if(js.getInteger("status") == 1){
	                        	JSONArray arr = js.getJSONArray("msg");

								if(arr.size()==0){

									listview.onRefreshComplete();
									return;
								}
	                        	ArrayList<WeseeBean> l = new ArrayList<WeseeBean>();
	                        	if(isaddhistory){
	                        		position = array.size();
	                        		for(int i = arr.size()-1; i>=0;i--){
                        				JSONObject ob = arr.getJSONObject(i);
                        				WeseeBean we = new WeseeBean();
                        				we.uid = ob.getString("uid");
                        				we.nickname = ob.getString("nickname");
                        				we.content = ob.getString("msg");
                        				we.time = ob.getLongValue("date");
                        				we.avatar = ob.getString("avatar");
                        				we.id = ob.getString("id");
                        				l.add(we);
                        			}
	                        		array.addAll(0, l);
	                        		handler.sendEmptyMessage(8);
	                        	}else{
	                        		if(updatanews){
	                        			for(int i = arr.size()-1; i>=0;i--){
	                        				JSONObject ob = arr.getJSONObject(i);
	                        				WeseeBean we = new WeseeBean();
	                        				we.uid = ob.getString("uid");
	                        				we.nickname = ob.getString("nickname");
	                        				we.content = ob.getString("msg");
	                        				we.time = ob.getLongValue("date");
	                        				we.avatar = ob.getString("avatar");
	                        				we.id = ob.getString("id");
	                        				l.add(we);
	                        			}
	                        			if(l.size()>0){
	                        				if(!l.get(l.size()-1).id.equals(array.get(array.size()-1).id)){
	                        					array.addAll(l);
	                        					if(array.size()>100){
	                        						array.remove(0);
	                        					}
	                        					noshownumber += arr.size();
	                        					handler.sendEmptyMessage(1);
	                        				}
	                        			}
	                        		}else{
	                        			for(int i = arr.size()-1; i>=0;i--){
	                        				JSONObject ob = arr.getJSONObject(i);
	                        				WeseeBean we = new WeseeBean();
	                        				we.uid = ob.getString("uid");
	                        				we.nickname = ob.getString("nickname");
	                        				we.content = ob.getString("msg");
	                        				we.time = ob.getLongValue("date");
	                        				we.avatar = ob.getString("avatar");
	                        				we.id = ob.getString("id");
	                        				l.add(we);
	                        			}
	                        			array.clear();
	                        			array.addAll(l);
	                        			handler.sendEmptyMessage(0);
	                        		}
	                        	}
	                        }
	                    }
					}
	            });
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	        	handler.sendEmptyMessage(7);
	            e.printStackTrace();
	        }
	    }
	   
	 //发表
	   public void send(String msg){
	        try {
	        	
	        	RequestParams params = new RequestParams();
	        	   params.addBodyParameter("token", Util.token);
	        	   params.addBodyParameter("uid", MyApplication.getInstance().self.getId());
	        	   params.addBodyParameter("msg", msg);
	        	   params.addBodyParameter("type", "1");
	        	   XUtilsHelper.sendPost(send_url,
	        			   params, new HttpCallback() {
							@Override
							public void callback(String data) {
								// TODO Auto-generated method stub
								Log.d(TAG, "Real send :"+data);
			                    if(data.equals("error")){
			                    }else if(data.equals("")){
//			                    	handler.sendEmptyMessage(3);
			                    }else{
			                        JSONObject js = JSONObject.parseObject(data);
			                        if(js.getInteger("status") == 1){
			                        	handler.sendEmptyMessage(9);
			                        }else{
			                        	Log.d("ChartActivity", "Real send status是不是0:"+js.getString("msg"));
			                        }
			                        
			                    }
							}
						});
	            //&mobile=   &avatar=
	        	Log.d(TAG, "trl :"+"http://120.24.235.202:8080/HQChat/rest/real/send"+
	                    "?token="+Constent.device_token+"&uid="+MyApplication.getInstance().self.getId()
	                    +"&msg="+msg);
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	
	

	String path;

	private void findView() {
//		RealTimeView.findViewById(R.id.im_send_image).setVisibility(View.GONE);
//		RealTimeView.findViewById(R.id.im_more).setVisibility(View.GONE);
//		RealTimeView.findViewById(R.id.im_audio).setVisibility(View.GONE);
		
		rl_bedown = (RelativeLayout) RealTimeView.findViewById(R.id.bedown);
		bedown_number = (TextView) RealTimeView.findViewById(R.id.bedown_unumber);
		rl_bedown.setOnClickListener(onclick);
		
		edit = (EditText) RealTimeView.findViewById(R.id.im_send_text);
		edit.addTextChangedListener(warcher);
		send = (TextView) RealTimeView.findViewById(R.id.im_send_btn);
		send.setOnClickListener(onclick);
		listview = (PullToRefreshListView) RealTimeView.findViewById(R.id.list);
		listview.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				Log.d(TAG, "on refreshStart");
				loadingsize = loadingsize + 10;
				isloading = true;
				if(array.size()>90){
					Toast.makeText(activity, "聊天信息过多,不能显示更多聊天信息", Toast.LENGTH_LONG).show();
					listview.onRefreshComplete();
				}else{
					Ask(10, array.get(0).id, -1, true, true);
				}
			}
		});
		array = new ArrayList<WeseeBean>();
//		adapter = new WeSeeAdapter(activity, array,handler);
//		listview.setAdapter(adapter);
		
		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(arg1+arg2 == arg3){
					rl_bedown.setVisibility(View.GONE);
					noshownumber = 0;
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// array = new ArrayList<WeSeeBean>();
		// adapter = new WeSeeAdapter(activity, array);
		// listview.setAdapter(adapter);
	}

	final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

	private OnClickListener onclick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Log.d(TAG, "in onclick ");
			switch (arg0.getId()) {
			case R.id.im_send_btn:
				send(edit.getText().toString());
				edit.setText("");
				break;
			case R.id.bedown:
				listview.setSelection(listview.FOCUS_DOWN);
				rl_bedown.setVisibility(View.GONE);
				noshownumber = 0;
				break;
			default:
				break;
			}
		}
	};

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				//第一次更新
				Log.d("TAG", "array .size :" + array.size());
				adapter = new WeSeeAdapter(activity, array,handler);
				listview.setAdapter(adapter);
//				adapter.notifyDataSetChanged();
				listview.setSelection(listview.FOCUS_DOWN);
				RealTimeView.findViewById(R.id.jiazai).setVisibility(View.GONE);
				handler.sendEmptyMessageDelayed(4, 1000);
				break;
			case 1:
				//新消息来的处理
				adapter.notifyDataSetChanged();
				Log.d(TAG, "position :"+listview.getFirstVisiblePosition()+"   arr.size  :"+array.size());
				if(listview.getFirstVisiblePosition()>=array.size()-7){
					listview.setSelection(listview.FOCUS_DOWN);
				}else{
					rl_bedown.setVisibility(View.VISIBLE);
					bedown_number.setText(noshownumber+"");
				}
//				listview.setSelection(listview.FOCUS_DOWN);
				break;
//			case 2:
//				RealTimeView.findViewById(R.id.im_ll_record).setVisibility(
//						View.GONE);
//				RealTimeView.findViewById(R.id.im_key).setVisibility(View.GONE);
//				RealTimeView.findViewById(R.id.im_audio).setVisibility(
//						View.VISIBLE);
//				break;
			case 3:
				adapter.notifyDataSetChanged();
				break;
			case 4:
				listview.requestFocus();
				listview.setSelection(listview.getBottom());
				break;
			case 7:
				//加载失败
				((TextView)RealTimeView.findViewById(R.id.jiazai)).setText("加载失败,重新加载中...");
				break;
			case 8:
				adapter.notifyDataSetChanged();
				Log.d(TAG, "we :"+position+"  ar :"+array.size());
				listview.onRefreshComplete();
				listview.setSelection(array.size()-position);
				break;
			case 9:
				Ask(10, array.get(array.size()-1).id, 1, true,false);
				
				break;
			default:
				break;
			}
		};
	};

	private TextWatcher warcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
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
	
}
