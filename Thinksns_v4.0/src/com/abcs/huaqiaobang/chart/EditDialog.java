package com.abcs.huaqiaobang.chart;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.alibaba.fastjson.JSONObject;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;


/**
 * @author xbw
 * @version 创建时间：2015-6-2 上午11:04:17
 */
public class EditDialog extends Dialog implements OnClickListener {
	private Context context;
	private Complete complete;
	private Complete cancelComplete;
	private EditText edit;
	private Handler handler;

	public EditDialog(Context context,Handler handler) {
		super(context, R.style.dialog);
		this.context = context;
		this.handler = handler;
		setContentView(R.layout.activity_chart_amswer);
		findViewById(R.id.sure).setOnClickListener(this);
		edit = (EditText) findViewById(R.id.edit);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.sure:
			if(!OneAskActivity.id.equals("")){
				Ask(edit.getText().toString(),OneAskActivity.id);
			}
			break;
		default:
			break;
		}
	}
	
	//回答
		public void Ask(String msg,String id){
			ProgressDlgUtil.showProgressDlg(null, (Activity)context);
	        try {
	        	//&mobile=   &avatar=
	        	Log.d("ChartActivity", "回答 :"+msg+"   id :"+id);
	            HttpRequest.sendPost(TLUrl.getInstance().URL_goods_base+"/HQChat/rest/userqa/answer",
	            		"uid="+MyApplication.getInstance().self.getId()+"&msg="+msg
	            		+"&id="+id
	            		,new HttpRevMsg() {
	                @Override
	                public void revMsg(String msg) {
	                    // TODO Auto-generated method stub
	                    Log.d("ChartActivity", "回答 :"+msg);
	                    if(!msg.equals("error")){
	                    	JSONObject js = JSONObject.parseObject(msg);
	                    	if(js.getInteger("status") == 1){
	                    		ProgressDlgUtil.stopProgressDlg();
	                    		handler.sendEmptyMessage(10);
	                    		dismiss();
	                    	}else{
	                    		ProgressDlgUtil.stopProgressDlg();
	                    		Toast.makeText(getContext(), "回答失败，请检查网络", Toast.LENGTH_LONG).show();
	                    	}
	                    }
					}
	            });
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	
	
}
