package com.abcs.hqbtravel.wedgt;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.local.view.SideLetterBar;
import com.abcs.haiwaigou.model.QuHao;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.huaqiaobang.adapter.SelectQuHao2Adapter;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.sociax.android.R;
import com.google.gson.Gson;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.yixia.camera.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.abcs.sociax.android.R.id.relative_back;
import static com.abcs.sociax.t4.android.video.ToastUtils.showToast;


public class SelectedQuHaoDialog extends Dialog implements OnClickListener {
	private Activity activity;

	public interface GetQuHao{
		void setQHao(String numbers);
	}

	public GetQuHao mQuHao;

	public void setQuHao(GetQuHao quHao) {
		mQuHao = quHao;
	}

	public SelectedQuHaoDialog(Activity activity) {
		super(activity, R.style.dialog_changecity);
		this.activity = activity;
		setContentView(R.layout.activity_select_qu_hao);
		setCanceledOnTouchOutside(false);
		init();
		windowDeploy();
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


	RelativeLayout relativeBack;
	ListView mylistview;
	EditText ed_sercher;
	TextView tvLetterOverlay;

	SideLetterBar sideLetterBar;


	SelectQuHaoAdapter adapter;

	ACache aCache;

	private void init() {

		sideLetterBar=(SideLetterBar)findViewById(R.id.side_letter_bar);
		tvLetterOverlay=(TextView)findViewById(R.id.tv_letter_overlay);
		ed_sercher=(EditText) findViewById(R.id.ed_sercher);
		mylistview=(ListView) findViewById(R.id.mylistview);
		relativeBack=(RelativeLayout) findViewById(R.id.relative_back);

		aCache = ACache.get(activity);

		initListener();

		if(!TextUtils.isEmpty(aCache.getAsString("quhao"))){
			Log.i("zds","bendi"+aCache.getAsString("quhao"));
			initDatas(aCache.getAsString("quhao"));
		}else {
			loadDatas();
		}

	}

	private void initListener(){
		relativeBack.setOnClickListener(this);

		ed_sercher.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				Log.i("zds_i",i+"===="+i1+"-----------"+i2+"++++++++++++"+charSequence);
				String cont=charSequence.toString();
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		initSlideBar();
	}


	private void initSlideBar() {
		sideLetterBar.setOverlay(tvLetterOverlay);
		sideLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
			@Override
			public void onLetterChanged(String letter) {

				Log.i("zds_let",letter+"");

				if (letter.equals("#")) {
					mylistview.setSelection(0);
				} else {
					int position = adapter.getLetterPosition(letter);
					mylistview.setSelection(position);

					Log.i("zds_pos",position+"");
				}
			}
		});
	}

	Handler mHandler=new Handler();

	private void loadDatas(){
		ProgressDlgUtil.showProgressDlg("",activity);
//        http://120.24.19.29:7075/find/getAreaCode?pageno=1&pageSize=300
		HttpRequest.sendGet(TLUrl.getInstance().URL_get_country_quhao, "pageno=1&pageSize=300", new HttpRevMsg() {
			@Override
			public void revMsg(final String msg) {
				ProgressDlgUtil.stopProgressDlg();

				mHandler.post(new Runnable() {
					@Override
					public void run() {

						if(!TextUtils.isEmpty(msg)){

							LogUtil.i("zds","wanluo"+msg);

							if(TextUtils.isEmpty(aCache.getAsString("quhao"))){
								aCache.put("quhao",msg);
							}

							LogUtil.i("zds_quhao",msg+"");

							initDatas(msg);

						}
					}
				});
			}
		});
	}

	private void initDatas(String msg){

		if(!TextUtils.isEmpty(msg)){

			QuHao bean=new Gson().fromJson(msg,QuHao.class);

			if(bean!=null){
				if(bean.result==1){
					if(bean.body!=null){
						if(bean.body.size()>0){
							adapter=new SelectQuHaoAdapter(activity,bean.body);
							mylistview.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}else {
							showToast("抱歉！暂无数据！");
						}
					}
				}
			}else {
				showToast("数据出错！");
			}
		}

	}



	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case relative_back:
			this.dismiss();
			break;
		}
	}


	public class SelectQuHaoAdapter extends BaseAdapter {
		private Activity mcontext;
		private LayoutInflater inflater;
		private List<QuHao.BodyBean> cityList=new ArrayList<>();

		private HashMap<String, Integer> letterIndexes;

		private String[] sections;


		public SelectQuHaoAdapter(Activity mcontext,List<QuHao.BodyBean> cityList) {
			this.mcontext = mcontext;
			this.cityList = cityList;
			inflater = LayoutInflater.from(mcontext);

			letterIndexes = new HashMap<>();
			sections = new String[cityList.size()];
			for (int index = 0; index < cityList.size(); index++) {
				//当前城市拼音首字母
				String currentLetter = cityList.get(index).firstLetter;
				//上个首字母，如果不存在设为""
				String previousLetter = index >= 1 ? cityList.get(index).firstLetter : "";
				letterIndexes.put(currentLetter, index);
				sections[index] = currentLetter;
//            if (!TextUtils.equals(currentLetter, previousLetter)) {
//                letterIndexes.put(currentLetter, index);
//                sections[index] = currentLetter;
//            }
			}

		}


		/**
		 * 获取字母索引的位置
		 *
		 * @param letter
		 * @return
		 */
		public int getLetterPosition(String letter) {
			Integer integer = letterIndexes.get(letter);
			return integer == null ? -1 : integer;
		}

		public void  addDatas(List<QuHao.BodyBean> dat){
			if(dat!=null&&dat.size()>0){
				cityList.addAll(dat);
				notifyDataSetChanged();
			}
		}

		public void removeAllDAtas(List<QuHao.BodyBean> dat){
			if(dat!=null&&dat.size()>0){
				for (int i=0;i<dat.size();i++){
					cityList.remove(i);
				}
				notifyDataSetChanged();
			}
		}
		@Override
		public int getCount() {
			return cityList.size();
		}

		@Override
		public QuHao.BodyBean getItem(int position) {
			return cityList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder=null;
			final QuHao.BodyBean bodyBean= getItem(position);
			if(convertView==null){
				convertView=inflater.inflate(R.layout.item_select_pingyin,parent,false);
				holder=new ViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}

			if(bodyBean!=null){
				if(!TextUtils.isEmpty(bodyBean.firstLetter)){
					holder.pingyin_tv.setText(bodyBean.firstLetter+"");
				}
				if(bodyBean.countrys!=null&&bodyBean.countrys.size()>0){
					mQuHao2Adapter=new SelectQuHao2Adapter(mcontext);
					holder.mylistview.setAdapter(mQuHao2Adapter);
					mQuHao2Adapter.addDatas(bodyBean.countrys);

					holder.mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
							QuHao.BodyBean.CountrysBean countrysBean=(QuHao.BodyBean.CountrysBean) adapterView.getItemAtPosition(i);
							/*Intent intent = new Intent();
							intent.putExtra("quhao", countrysBean.mobilePrefix+"");
							mcontext.setResult(9, intent);
							mcontext.finish();*/

						/*	if(mQuHao!=null){
							}*/

							android.util.Log.i("zds==pos",countrysBean.mobilePrefix+"");
							mQuHao.setQHao(countrysBean.mobilePrefix+"");

							SelectedQuHaoDialog.this.dismiss();

						}
					});
				}
			}

			return convertView;
		}

		private SelectQuHao2Adapter mQuHao2Adapter;
		public class  ViewHolder{

			private TextView pingyin_tv;
			private MyListView mylistview;
			public ViewHolder(View view) {
				pingyin_tv=(TextView) view.findViewById(R.id.pingyin_tv);
				mylistview=(MyListView) view.findViewById(R.id.mylistview);
			}

		}
	}

}
