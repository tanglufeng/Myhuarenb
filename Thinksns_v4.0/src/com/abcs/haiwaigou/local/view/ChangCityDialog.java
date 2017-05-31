package com.abcs.haiwaigou.local.view;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.abcs.haiwaigou.local.adapter.CityGridViewAdapter2;
import com.abcs.haiwaigou.local.beans.City2;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.hqbtravel.wedgt.MyGridView;
import com.abcs.hqbtravel.wedgt.MyListView;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import java.util.List;


public class ChangCityDialog extends Dialog implements OnClickListener {
	private Activity activity;

	public interface GetCity{
		void setCity(City2.BodyBean.DataBean.CountrysBean.CitysBean citysBean);
	}

	public interface isChina{
		void getChina(boolean yes);
	}

	private isChina mIsChaina;
	private GetCity mGetCity;

	public void setisChina(isChina mIsChaina){
		this.mIsChaina=mIsChaina;
	}
	public void setGetCity(GetCity getCity) {
		mGetCity = getCity;
	}


	public ChangCityDialog(Activity activity) {
		super(activity, R.style.dialog_changecity);
		this.activity = activity;
		setContentView(R.layout.local_activity_country_city4);
		aCache = ACache.get(activity);
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


	ImageView localIvBack;
	LinearLayout lin_gnei;
	TextView localTvSeek;
	ListView localLv;
	CityListAdapter3 cityListAdapter3;
	Handler mHandler = new Handler();
	private ACache aCache;


	private void init() {

		lin_gnei=(LinearLayout)findViewById(R.id.lin_gnei);
		localIvBack=(ImageView)findViewById(R.id.local_iv_back);
		localTvSeek=(TextView)findViewById(R.id.local_tv_seek);
		localLv=(ListView)findViewById(R.id.local_lv);

		localIvBack.setOnClickListener(this);
		lin_gnei.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mIsChaina.getChina(true);
				dismiss();
			}
		});
	}

	private void speak() {

		ProgressDlgUtil.showProgressDlg("loading...", activity);

		HttpRequest.sendGet(TLUrl.getInstance().URL_local_get_area3_location, "version=v1.0", new HttpRevMsg() {
			@Override
			public void revMsg(final String msg) {
				ProgressDlgUtil.stopProgressDlg();

				if (msg != null) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							City2 city = new Gson().fromJson(msg, City2.class);
							if (city.result == 1) {
								if (city.body.data != null && city.body.data.size() > 0) {
									cityListAdapter3 = new CityListAdapter3(activity, city.body.data);
									localLv.setAdapter(cityListAdapter3);
								}
							}
						}
					});
				}
			}
		});
	}


	public class CityListAdapter3 extends BaseAdapter {
		private Activity mcontext;
		private LayoutInflater inflater;
		private List<City2.BodyBean.DataBean> cityList;

		public CityListAdapter3(Activity mcontext, List<City2.BodyBean.DataBean> cityList) {
			this.mcontext = mcontext;
			this.cityList = cityList;
			inflater = LayoutInflater.from(mcontext);
		}

		@Override
		public int getCount() {
			return cityList.size();
		}

		@Override
		public City2.BodyBean.DataBean getItem(int position) {
			return cityList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder= null;
			if (convertView == null){
				convertView = View.inflate(mcontext,R.layout.local_activity_area,null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}

			if (!TextUtils.isEmpty(cityList.get(position).area_cn_name)){
				holder.areaName.setText(cityList.get(position).area_cn_name);
			}

			if (cityList.get(position).countrys != null && cityList.get(position).countrys.size()>0){
				holder.cityListView.setAdapter(new CountryListAdapter(mcontext,cityList.get(position).countrys));
			}

			return convertView;
		}

		public class  ViewHolder{
			private TextView areaName;  //欧洲
			private MyListView cityListView;  //国家和城市
			public ViewHolder(View view) {
				areaName=(TextView) view.findViewById(R.id.item_tv_area);
				cityListView= (MyListView) view.findViewById(R.id.item_lv_area);
			}

		}
	}


	/**
	 * Created by xuke on 2016/12/13.
	 * 里层listView的adapter  item为国家和城市
	 */
	public class CountryListAdapter extends BaseAdapter {

		private List<City2.BodyBean.DataBean.CountrysBean> countrys;
		private Activity mcontext;

		public CountryListAdapter(Activity mcontext, List<City2.BodyBean.DataBean.CountrysBean> countrys) {
			this.mcontext = mcontext;
			this.countrys = countrys;
		}

		@Override
		public int getCount() {
			return countrys.size();
		}

		@Override
		public City2.BodyBean.DataBean.CountrysBean getItem(int position) {
			return countrys.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null){
				convertView = View.inflate(mcontext, R.layout.local_activity_country,null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (!TextUtils.isEmpty(countrys.get(position).country_name)){
				holder.countryName.setText(countrys.get(position).country_name);
			}

			if (countrys.get(position).citys!=null&&countrys.get(position).citys.size()>0){
				holder.cityGridView.setAdapter(new CityGridViewAdapter2(mcontext,countrys.get(position).citys));
			}

			holder.cityGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					City2.BodyBean.DataBean.CountrysBean.CitysBean citysBean = (City2.BodyBean.DataBean.CountrysBean.CitysBean) adapterView.getItemAtPosition(i);

					Util.preference.edit().putString(MyString.LOCAL_CITY_NAME, citysBean.cate_name).commit();
					Util.preference.edit().putString(MyString.LOCAL_CITY_ID, citysBean.city_id+"").commit();
					Util.preference.edit().putString(MyString.LOCAL_COUNTRY_NAME, countrys.get(position).country_name).commit();
					if (mGetCity!=null){
						mGetCity.setCity(citysBean);
					}

					dismiss();

				}
			});

			return convertView;
		}

		public class ViewHolder{
			private TextView countryName;
			private MyGridView cityGridView;
			public ViewHolder(View view){
				countryName = (TextView) view.findViewById(R.id.item_country_name);
				cityGridView= (MyGridView) view.findViewById(R.id.item_mgv_city);
			}
		}
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.local_iv_back:
				dismiss();
				break;
			case R.id.local_tv_seek:
				break;
		}
	}

}
