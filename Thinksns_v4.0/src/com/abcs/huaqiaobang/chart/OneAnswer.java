package com.abcs.huaqiaobang.chart;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.chart.FutureAskViewAdapter.AskBean;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.activity.StartActivity;
import com.alibaba.fastjson.JSONObject;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;


public class OneAnswer extends BaseActivity{

	ImageView imagezan ;
	AskBean ob;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart_oneanswer);
//		getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.toumin));
		
		findView();
	}
	
	private void findView(){
		findViewById(R.id.tljr_img_futures_join_back).setOnClickListener(onclick);
		((TextView)findViewById(R.id.title)).setText(getIntent().getStringExtra("title"));
		((TextView)findViewById(R.id.title2)).setText(getIntent().getStringExtra("title"));
		imagezan = (ImageView) findViewById(R.id.zan);
		imagezan.setOnClickListener(onclick);
		
		if(getIntent().getStringExtra("type").equals("FutureAskViewAdapter")){
			ob = FutureAskViewAdapter.map.get(getIntent().getStringExtra("id"));
		}else{
			ob = OneAskActivity.map.get(getIntent().getStringExtra("id"));
		}
		((TextView)findViewById(R.id.name)).setText(ob.nickname);
		((TextView)findViewById(R.id.zan_number)).setText(ob.focus);
		((TextView)findViewById(R.id.content)).setText(ob.msg);
		StartActivity.imageLoader.displayImage(ob.avatar, (CircularImage)findViewById(R.id.im));
		if(ob.isfocus){
			imagezan.setBackgroundResource(R.drawable.zan_blue);
		}
	}
	
	private OnClickListener onclick = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.tljr_img_futures_join_back:
				finish();
				break;
			case R.id.zan:
				Ask(ob.id,!ob.isfocus);
				break;
			default:
				break;
			}
		}
	};
	
	//点赞
	public void Ask(String id,final boolean zanorno){
		ProgressDlgUtil.showProgressDlg("", this);
        try {
        	//&mobile=   &avatar=
        	Log.d("ChartActivity", "点赞 id :"+id);
        	String url = TLUrl.getInstance().URL_goods_base+"/HQChat/rest/userqa/";
        	if(zanorno){
        		url+="focus";
        	}else{
        		url+="unfocus";
        	}
        	Log.d("ChartActivity", "url :"+url+"?uid="+MyApplication.getInstance().self.getId()+"&id="+id);
            HttpRequest.sendGet(url, 
            		"uid="+MyApplication.getInstance().self.getId()+"&id="+id
            		,new HttpRevMsg() {
                @Override
                public void revMsg(String msg) {
                    // TODO Auto-generated method stub
                    Log.d("ChartActivity", "点赞 :"+msg);
                    if(!msg.equals("error")){
                    	JSONObject js = JSONObject.parseObject(msg);
                    	if(js.getInteger("status") == 1){
                    		ob.isfocus = zanorno;
                    		handler.sendEmptyMessage(1);
//	                    		handler.sendEmptyMessage(9);
                    	}else{
                    	}
                    }else{
                    	ProgressDlgUtil.stopProgressDlg();
                    }
				}
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				int i;
				if(ob.isfocus){
					imagezan.setBackgroundResource(R.drawable.zan_blue);
					i = Integer.parseInt(((TextView)findViewById(R.id.zan_number)).getText().toString())+1;
				}else{
					imagezan.setBackgroundResource(R.drawable.zan);
					i = Integer.parseInt(((TextView)findViewById(R.id.zan_number)).getText().toString())-1;
				}
				if(i<0){
					i = 0;
				}
				((TextView)findViewById(R.id.zan_number)).setText(i+"");
				ob.focus = i+"";
				setResult(1);
				ProgressDlgUtil.stopProgressDlg();
				break;

			default:
				break;
			}
		};
	};
}
