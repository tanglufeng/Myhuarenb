package com.abcs.huaqiaobang.ytbt.im;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.ytbt.adapter.LabelListAdapter;
import com.abcs.huaqiaobang.ytbt.bean.LabelBean;
import com.abcs.huaqiaobang.ytbt.util.TLUrl;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LabelListActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
	private ListView lv;
	private LabelListAdapter adapter; 
	private LabelChangeBroadCast receiver;
	private List<LabelBean> labels = new ArrayList<>();
	public static final String ACTION_LABEL_CHANGED = "ACTION_LABEL_CHANGED";
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_label);
		try {
			MyApplication.dbUtils.createTableIfNotExist(LabelBean.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
		initView();
		getAllLabels();
		receiver = new LabelChangeBroadCast();
		registerReceiver(receiver, new IntentFilter(ACTION_LABEL_CHANGED));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	private void getAllLabels() {
		 String url = TLUrl.URL_GET_VOIP + "User/findgrouping?uid="+MyApplication.getInstance().getUid();
         HttpUtils httpUtils = new HttpUtils(30000);
         httpUtils.configCurrentHttpCacheExpiry(1000);
         Tool.showProgressDialog(LabelListActivity.this, "正在加载...",true);
         httpUtils.send(HttpMethod.GET, url,new RequestCallBack<String>() {
        	 
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Tool.removeProgressDialog();
					Tool.showInfo(LabelListActivity.this, "网络异常");
					//loadData();
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					Tool.removeProgressDialog();
					String result = arg0.result;
					Log.i("info", result);
					try {
						JSONObject object = new JSONObject(result);
						if(object.getInt("status")==1){
							JSONArray array = object.getJSONArray("msg");
							if(array.length()==0){
								return;
							}
							labels.clear();
							for (int i = 0; i < array.length(); i++) {
								JSONObject obj = array.getJSONObject(i);
								LabelBean bean = new LabelBean();
								bean.setId(obj.getJSONObject("_id").getString("$oid"));
								bean.setLabelName(obj.getString("grouping"));
								labels.add(bean);
							}
							MyApplication.dbUtils.saveOrUpdateAll(labels);
						}
					} catch (Exception e) {
						Tool.showInfo(LabelListActivity.this, "加载失败");
					}finally{
						setData();
					}
				}
			});
     }

	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.create).setOnClickListener(this);
		lv = (ListView) findViewById(R.id.labellist);
		lv.setEmptyView(findViewById(R.id.empty_tip_recommend_bind_tv));
		lv.setOnItemClickListener(this);
	}

	private void loadData() {
		labels.clear();
		try {
			labels.addAll(MyApplication.dbUtils.findAll(LabelBean.class));
		} catch (DbException e) {
			e.printStackTrace();
		}
		if(labels.size()==0){
			getAllLabels();
		}else
		setData();
	}
		
	private void setData() {
		if(adapter==null){
			adapter = new LabelListAdapter(this, R.layout.item_label_list, labels);
			lv.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.create:
//			Intent intent = new Intent(LabelListActivity.this,
//					FriendsListActivity.class);
//			startActivityForResult(intent, CREATE_LABAL_REQUEST);
			createLabel();
			break;
		}
	}
	private void createLabel() {
	 	LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.nickname_edittext, null);
        final EditText inputNickName = (EditText) layout.findViewById(R.id.editText1);
        inputNickName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        inputNickName.setHint("请输入分组名称");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("创建分组").setView(layout).setPositiveButton("取消", null);
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                final String LabelName = inputNickName.getText().toString().trim();               
                if (LabelName.length() == 0) {
                	Tool.showInfo(LabelListActivity.this, "请输入分组名称");
                    return;
                }
           //     ProgressDlgUtil.showProgressDlg("修改中", activity);
                String url = TLUrl.URL_GET_VOIP + "User/addgrouping";
                HttpUtils httpUtils = new HttpUtils(30000);
                httpUtils.configCurrentHttpCacheExpiry(1000);
                RequestParams params = new RequestParams();
                params.addBodyParameter("uid", MyApplication.getInstance().getUid()+"");
                params.addBodyParameter("grouping", LabelName);
                Tool.showProgressDialog(LabelListActivity.this, "正在创建...",true);
                httpUtils.send(HttpMethod.POST, url,params,new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Tool.removeProgressDialog();
						Tool.showInfo(LabelListActivity.this, "创建失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Tool.removeProgressDialog();
						String result = arg0.result;
						try {
							JSONObject object = new JSONObject(result);
							if(object.getInt("status")==1){
								Tool.showInfo(LabelListActivity.this, "创建成功");
								LabelBean bean = new LabelBean();
								bean.setId(object.getJSONArray("msg").getJSONObject(0).getJSONObject("_id").getString("$oid"));
								bean.setLabelName(LabelName);
								MyApplication.dbUtils.save(bean);
								loadData();
							}else{
								Tool.showInfo(LabelListActivity.this, "创建失败"+object.getString("msg"));
							}
						} catch (Exception e) {
							e.printStackTrace();
							getAllLabels();
						}
						
					}
				});
            }
        });
        builder.show();
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		LabelBean bean = labels.get(position);
		Intent intent = new Intent(LabelListActivity.this,LabelDetailActivity.class);
		intent.putExtra("label", bean);
		startActivity(intent);
	}
	
	class LabelChangeBroadCast extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			loadData();
		}
	}
	
	
}
