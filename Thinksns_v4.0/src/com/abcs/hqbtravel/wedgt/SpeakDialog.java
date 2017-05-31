package com.abcs.hqbtravel.wedgt;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.abcs.haiwaigou.local.beans.City1;
import com.abcs.hqbtravel.Contonst;
import com.abcs.hqbtravel.adapter.CityGridViewAdapter;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;

import java.util.List;


public class SpeakDialog extends Dialog implements OnClickListener {
	private Activity activity;
	private String parms;
	private long PUBLISH_COMMENT_TIME;



	public interface GetCity{
		void setCity(City1.BodyBean.DataBean.CitysBean citysBean);
	}

	private GetCity mGetCity;

	public void setGetCity(GetCity getCity) {
		mGetCity = getCity;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public SpeakDialog(Activity activity) {
		super(activity, R.style.dialog_changecity);
		this.activity = activity;
		setContentView(R.layout.local_activity_country_city3);
		setCanceledOnTouchOutside(false);
		init();
		windowDeploy();
		speak();
	}

	// 设置窗口显示
	public void windowDeploy() {
		Window win = getWindow(); // 得到对话框
		win.setWindowAnimations(R.style.speakdialog_bottom); // 设置窗口弹出动画
		win.getDecorView().setPadding(0, 0, 0, 0); // 宽度占满，因为style里面本身带有padding
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.FILL_PARENT;
		win.setAttributes(lp);

	}


	private   ListView localLv;

	private void init() {

		findViewById(R.id.local_iv_back).setOnClickListener(this);
		localLv=(ListView)findViewById(R.id.local_lv);
	}


	CityListAdapter2 cityListAdapter2;

	Handler mhandle=new Handler();

	private void speak() {

		ProgressDlgUtil.showProgressDlg("loading...",activity);

		HttpRequest.sendGet(Contonst.LOCAL_CHANGECITY, "", new HttpRevMsg() {
			@Override
			public void revMsg(final String msg) {
				if (msg!=null) {
					ProgressDlgUtil.stopProgressDlg();

					mhandle.post(new Runnable() {
						@Override
						public void run() {
							City1 city=new Gson().fromJson(msg,City1.class);
							if (city!=null&&city.result==1) {

								if(city.body.data!=null&&city.body.data.size()>0){
									cityListAdapter2 = new CityListAdapter2(activity,city.body.data);
									localLv.setAdapter(cityListAdapter2);
								}
							}
						}
					});
				}else {
					ProgressDlgUtil.stopProgressDlg();
				}
			}

		});
	}

	CityGridViewAdapter mGridViewAdapter;

	public class CityListAdapter2 extends BaseAdapter {
		private Activity mcontext;
		private LayoutInflater inflater;
		private List<City1.BodyBean.DataBean> cityList;

		public CityListAdapter2(Activity mcontext, List<City1.BodyBean.DataBean> cityList) {
			this.mcontext = mcontext;
			inflater = LayoutInflater.from(mcontext);
			this.cityList = cityList;
		}

		@Override
		public int getCount() {
			return cityList.size();
		}

		@Override
		public City1.BodyBean.DataBean getItem(int position) {
			return cityList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder=null;
			if(convertView==null){
				convertView=inflater.inflate(R.layout.local_activity_country_city3_item,parent,false);
				holder=new ViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			if(!TextUtils.isEmpty(cityList.get(position).areaCnName)){
				holder.tv_acer.setText(cityList.get(position).areaCnName);
			}

			if(cityList.get(position).citys!=null&&cityList.get(position).citys.size()>0){

				mGridViewAdapter=new CityGridViewAdapter(mcontext,cityList.get(position).citys);
				holder.cityGridView.setAdapter(mGridViewAdapter);

			}

			holder.cityGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

					City1.BodyBean.DataBean.CitysBean citysBean=(City1.BodyBean.DataBean.CitysBean)adapterView.getItemAtPosition(i);

//					setCityId(citysBean.cityId+"");
//					setCityName(citysBean.cateName+"");

					MyApplication.saveTravelCityId(citysBean.cityId+"");
					MyApplication.saveCityName(citysBean.cateName+"");

					if (mGetCity!=null){
						mGetCity.setCity(citysBean);
					}


					dismiss();

				}
			});

			return convertView;
		}
	}

	String cityId;
	String cityName;

	public String getCityName() {
		return cityName;
	}

	public String getCityId() {
		return cityId;
	}

	public class  ViewHolder{

		private TextView tv_acer;
		private MyGridView cityGridView;
		public ViewHolder(View view) {
			tv_acer=(TextView) view.findViewById(R.id.area_tv);
			cityGridView= (MyGridView) view.findViewById(R.id.local_gv);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.local_iv_back:
			this.dismiss();
			break;
		}
	}

}
