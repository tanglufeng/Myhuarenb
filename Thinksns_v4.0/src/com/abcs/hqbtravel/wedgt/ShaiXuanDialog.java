//package com.abcs.hqbtravel.wedgt;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.abcs.haiwaigou.local.activity.RestauraActivity;
//import com.abcs.haiwaigou.local.beans.AARease;
//import com.abcs.haiwaigou.local.beans.City1;
//import com.abcs.hqbtravel.adapter.CityGridViewAdapter;
//import com.abcs.huaqiaobang.MyApplication;
//import com.abcs.sociax.android.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class ShaiXuanDialog extends Dialog implements OnClickListener {
//	private Activity activity;
//
//
//
//	public interface GetCity{
//		void setCity(City1.BodyBean.DataBean.CitysBean citysBean);
//	}
//
//	private GetCity mGetCity;
//
//	public void setGetCity(GetCity getCity) {
//		mGetCity = getCity;
//	}
//
//	public void setCityId(String cityId) {
//		this.cityId = cityId;
//	}
//
//	public void setCityName(String cityName) {
//		this.cityName = cityName;
//	}
//
//	public ShaiXuanDialog(Activity activity) {
//		super(activity, R.style.dialog_changecity);
//		this.activity = activity;
//		setContentView(R.layout.local_shanxuan_view);
//		setCanceledOnTouchOutside(false);
//		init();
//		windowDeploy();
//		speak();
//	}
//
//	// 设置窗口显示
//	public void windowDeploy() {
//		Window win = getWindow(); // 得到对话框
//		win.setWindowAnimations(R.style.speakdialog_bottom); // 设置窗口弹出动画
//		win.getDecorView().setPadding(0, 0, 0, 0); // 宽度占满，因为style里面本身带有padding
//		WindowManager.LayoutParams lp = win.getAttributes();
//		lp.width = WindowManager.LayoutParams.FILL_PARENT;
//		lp.height = WindowManager.LayoutParams.FILL_PARENT;
//		win.setAttributes(lp);
//
//	}
//
//
//	private   ListView listview_left,listview_right;
//
//	private void init() {
//
//		listview_left=(ListView)findViewById(R.id.listview_left);
//		listview_right=(ListView)findViewById(R.id.listview_right);
//	}
//
//
//
//	Handler mhandle=new Handler();
//
//	private void speak() {
//
//	}
//
//
//	public class MyListAdapterLeft extends BaseAdapter {
//
//		Context context;
//		public MyListPopAdapter(Context context,List<AARease> list) {
//			this.context = context;
//			this.list = list;
//		}
//
//		List<AARease> list=new ArrayList<>();
//
//
//
//		@Override
//		public int getCount() {
//			return list.size();
//		}
//
//		@Override
//		public Object getItem(int i) {
//			return list.get(i);
//		}
//
//		@Override
//		public long getItemId(int i) {
//			return i;
//		}
//
//		@Override
//		public View getView(int i, View view, ViewGroup viewGroup) {
//
//			RestauraActivity.MyListPopAdapter.VieeHolder vieeHolder=null;
//			if(view==null){
//				view= LayoutInflater.from(context).inflate(R.layout.item_text,viewGroup,false);
//				vieeHolder=new RestauraActivity.MyListPopAdapter.VieeHolder(view);
//				view.setTag(vieeHolder);
//			}else {
//				vieeHolder=(RestauraActivity.MyListPopAdapter.VieeHolder) view.getTag();
//			}
//
//			AARease bean=(AARease) getItem(i);
//
//			vieeHolder.tv.setText(bean.name+"");
//
//			if (selectedPosition == i) {
//				vieeHolder.rela_bg.setSelected(true);
//				vieeHolder.rela_bg.setPressed(true);
//				vieeHolder.rela_bg.setBackgroundColor(getResources().getColor(R.color.hwg_bg));
//			} else {
//				vieeHolder.rela_bg.setSelected(false);
//				vieeHolder.rela_bg.setPressed(false);
//				vieeHolder.rela_bg.setBackgroundColor(Color.TRANSPARENT);
//
//			}
//
//			return view;
//
//
//		}
//		public class VieeHolder{
//			TextView tv;
//			RelativeLayout rela_bg;
//
//			public VieeHolder(View view) {
//
//				tv=(TextView) view.findViewById(R.id.tv);
//				rela_bg=(RelativeLayout) view.findViewById(R.id.rela_bg);
//			}
//		}
//
//		public void setSelectedPosition(int position) {
//			selectedPosition = position;
//		}
//
//		private int selectedPosition = -1;// 选中的位置
//
//	}
//
//	/////////////////////////////////////////
//
//	CityGridViewAdapter mGridViewAdapter;
//
//	public class CityListAdapter2 extends BaseAdapter {
//		private Activity mcontext;
//		private LayoutInflater inflater;
//		private List<City1.BodyBean.DataBean> cityList;
//
//		public CityListAdapter2(Activity mcontext, List<City1.BodyBean.DataBean> cityList) {
//			this.mcontext = mcontext;
//			inflater = LayoutInflater.from(mcontext);
//			this.cityList = cityList;
//		}
//
//		@Override
//		public int getCount() {
//			return cityList.size();
//		}
//
//		@Override
//		public City1.BodyBean.DataBean getItem(int position) {
//			return cityList.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(final int position, View convertView, ViewGroup parent) {
//
//			ViewHolder holder=null;
//			if(convertView==null){
//				convertView=inflater.inflate(R.layout.local_activity_country_city3_item,parent,false);
//				holder=new ViewHolder(convertView);
//				convertView.setTag(holder);
//			}else{
//				holder=(ViewHolder) convertView.getTag();
//			}
//			if(!TextUtils.isEmpty(cityList.get(position).areaCnName)){
//				holder.tv_acer.setText(cityList.get(position).areaCnName);
//			}
//
//			if(cityList.get(position).citys!=null&&cityList.get(position).citys.size()>0){
//
//				mGridViewAdapter=new CityGridViewAdapter(mcontext,cityList.get(position).citys);
//				holder.cityGridView.setAdapter(mGridViewAdapter);
//
//			}
//
//			holder.cityGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//					City1.BodyBean.DataBean.CitysBean citysBean=(City1.BodyBean.DataBean.CitysBean)adapterView.getItemAtPosition(i);
//
////					setCityId(citysBean.cityId+"");
////					setCityName(citysBean.cateName+"");
//
//					MyApplication.saveTravelCityId(citysBean.cityId+"");
//					MyApplication.saveCityName(citysBean.cateName+"");
//
//					if (mGetCity!=null){
//						mGetCity.setCity(citysBean);
//					}
//
//
//					dismiss();
//
//				}
//			});
//
//			return convertView;
//		}
//	}
//
//	String cityId;
//	String cityName;
//
//	public String getCityName() {
//		return cityName;
//	}
//
//	public String getCityId() {
//		return cityId;
//	}
//
//	public class  ViewHolder{
//
//		private TextView tv_acer;
//		private MyGridView cityGridView;
//		public ViewHolder(View view) {
//			tv_acer=(TextView) view.findViewById(R.id.area_tv);
//			cityGridView= (MyGridView) view.findViewById(R.id.local_gv);
//		}
//
//	}
//
//	@Override
//	public void onClick(View v) {
//
//		switch (v.getId()) {
//		case R.id.local_iv_back:
//			this.dismiss();
//			break;
//		}
//	}
//
//}
