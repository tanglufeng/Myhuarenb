package com.abcs.sociax.t4.android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.view.recyclerview.NetworkUtils;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.login.AutoLogin;
import com.abcs.huaqiaobang.tljr.data.InitData;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.ServerUtils;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.RegisterActivity;
import com.abcs.sociax.android.R;
import com.abcs.sociax.constant.AppConstant;
import com.abcs.sociax.t4.android.img.RoundImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.sociax.thinksnsbase.utils.ActivityStack;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;

public class ThinksnsActivity extends InstrumentedActivity {
	public static boolean canOpenGoogle=false;

	public static ImageLoader imageLoader;
	public static DisplayImageOptions options;
	private ImageView bj;
	private TextView pro;
	// private ProgressBar bar;
	private ImageView bar;
	private static String showScreen = ".HQ_SHOWSCREEN";

	private static final String TAG = "Init Activity";
	protected static final int SHOW_GUIDE = 4;
	private static final int LOGIN = 5;
	protected static final int REGISTER = 6;
	protected static final int GET_KEY = 7;
	protected static final int SHOW_SCREEN = 8;

	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private ViewGroup guide;
	private LayoutInflater inflater;
	private TextView tv_register, tv_login;
	public static SharedPreferences preferences;

	private ImageView smalldot;		// 广告位小圆点
	private ImageView[] smalldots;	// 广告位所有小圆点
	private LinearLayout ll_find_ads_dots;	// 广告位红点

	public Handler handlerUI;

	private Thinksns app;
	private static ThinksnsActivity instance = null;

	public static ThinksnsActivity getInstance() {
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		boolean loginOut = getIntent().getBooleanExtra("login_out", false);
		if(loginOut) {
			Thinksns.clearAllActivity();
			//清除当前用户信息
			Thinksns.getUserSql().clear();
		}


		if (Util.preference.getBoolean("ydlogin", false)) {
			Toast.makeText(this,"账号异地登陆！请修改密码！",Toast.LENGTH_LONG);
			Util.preference.edit().putBoolean("ydlogin", false).commit();
		}

		Thinksns.addActivity(this);
		instance = this;
		preferences = getSharedPreferences("count", MODE_WORLD_READABLE);
		inflater = getLayoutInflater();
		setContentView(R.layout.main);
		new InitData(ThinksnsActivity.this);
		initHandler();
		initStartView();
		isgoogle();

		String currHost= MyApplication.getCurrentHost();
		if(!TextUtils.isEmpty(currHost)){

			String basU_hua=currHost.substring(currHost.lastIndexOf("/")+1,currHost.length());

			Log.i("zdskai","basU_base=="+currHost);
			Log.i("zdskai","basU_hua=="+basU_hua);

			TLUrl.URL_BASE=currHost;
			TLUrl.URL_huayouhui=basU_hua;
			TLUrl.getInstance().isChange=true;

			Log.i("zdskai","basU_base2=="+TLUrl.getInstance().getUrl());
			Log.i("zdskai","basU_hua2=="+TLUrl.getInstance().getHuaUrl());


		}
	}




	private void initStartView() {
		pro = (TextView) findViewById(R.id.tljr_txt_jindu);
		bar = (ImageView) findViewById(R.id.tljr_pro_qd);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		bj = (ImageView) findViewById(R.id.tljr_img_qdbj);

		if(ServerUtils.isConnect(this)){  // 有网络
			getUrl();
		}else {
			try {
				String data = Util.getStringFromFile(showScreen);
				JSONObject obj = new JSONObject(data);
				String url;
				if (!obj.optString("fixed").equals("")) {
					url = obj.optString("fixed");
				} else {
					JSONArray array = obj.optJSONArray("common");
					url = array.getString(new Random().nextInt(array.length()));
				}

				Log.i(TAG, "initStartView: "+url);

				if(!TextUtils.isEmpty(url)){
					MyApplication.imageLoader.displayImage(url,bj,MyApplication.getListOptions());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		new InitData(ThinksnsActivity.this);

		bar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pro_move_left_in));
		((TextView) findViewById(R.id.tljr_txt_info)).setText(getVersion());
		Message msg=new Message();
		msg.what = SHOW_SCREEN;
		handlerUI.sendMessageDelayed(msg, 2000);
	}

	private void getUrl() {
		HttpRequest.sendPost(TLUrl.getInstance().URL_AD, "", new HttpRevMsg() {

			@Override
			public void revMsg(final String msg) {
				handlerUI.post(new Runnable() {
					@Override
					public void run() {
						try {
							JSONObject object = new JSONObject(msg);
							Log.i("zjz", "start_act=" + msg);
							if (object.getInt("status") == 1) {
								JSONObject obj = object.getJSONObject("result");
								try {
									if(obj.has("fixed")){
										String fixed=obj.optString("fixed");
										if(!TextUtils.isEmpty(fixed)){
											MyApplication.imageLoader.displayImage(fixed,bj,MyApplication.getListOptions());
										}
									}

									if(obj.has("common")){
										JSONArray array = obj.getJSONArray("common");
										for (int i = 0; i < array.length(); i++) {
											Util.onlyDownImage(array.getString(i));
										}
										byte[] b = obj.toString().getBytes();
										Util.writeFileToFile(showScreen, b, b.length);
									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} catch (JSONException e) {
							Log.i("zjz", "start_act失败！");
							e.printStackTrace();
						}
					}
				});
			}
		});
	}
	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return this.getString(R.string.version_name) + "V" + version;
		} catch (Exception e) {
			e.printStackTrace();
			return this.getString(R.string.can_not_find_version_name);
		}
	}
	private void initHandler() {
		handlerUI = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.arg1 == SHOW_GUIDE) {
					showGuide();
				}else if(msg.arg1==GET_KEY){
					Class<? extends Activity> clz;
					if(msg.what==LOGIN){
					//	clz = ActivityLogin.class;
//						clz = WXEntryActivity.class;  //原来的
						clz = ActivityHome.class;
					}else{
						clz = RegisterActivity.class;
					}
					//进入登录主页
					ActivityStack.startActivity(ThinksnsActivity.this, clz);
					finish();
				}else if(msg.what==SHOW_SCREEN){
					initApp();
				}
			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	protected void initApp() {
		app = (Thinksns) this.getApplicationContext();
		app.initApi();
		if (app.HasLoginUser()&& NetworkUtils.isNetAvailable(getApplicationContext())) {
			// 已经有登录的用户，直接进入主页
			Log.i("zjz","有用户");
//			new AutoLogin(this);
//			Log.i("zjz","isLoginSuccess="+AutoLogin.isLoginSuccess());
			if(getIntent().getBundleExtra(AppConstant.YYG_PUSH) != null){
				//云购中奖
				Log.i("zjz","云购中奖");
				new AutoLogin(this,true,getIntent().getBundleExtra(AppConstant.YYG_PUSH));
//				ActivityStack.startActivity(ThinksnsActivity.this, ActivityHome.class, getIntent().getBundleExtra(AppConstant.YYG_PUSH));
			}
			else {
				new AutoLogin(this);
//				ActivityStack.startActivity(ThinksnsActivity.this,ActivityHome.class);
			}

//			finish();
		} else {
			Log.i("zjz","无用户");
			if(!Util.preference.getBoolean(MyString.ISFIRST,false)){
				//第一次进入
				Log.i("zjz","首次进入");
				MyApplication.getInstance().saveIsFirstLocal(true);
				Message msg=new Message();
				msg.arg1 = SHOW_GUIDE;
				handlerUI.sendMessage(msg);
			}else {
				//			Message msg2 = new Message();
//			msg2.arg1 = SHOW_GUIDE;
//			handlerUI.sendMessage(msg2);
				Message msg=new Message();
				msg.arg1 = GET_KEY;
				msg.what = LOGIN;
//			handlerUI.sendMessageDelayed(msg, 2000);
				handlerUI.sendMessage(msg);
			}
		}
	}

	boolean initGuide = false;

	private void showGuide() {
		if (!initGuide) {
			pageViews = new ArrayList<View>();
			if (inflater!=null) {
				pageViews.add(inflater.inflate(R.layout.guideitem1, null));
//				pageViews.add(inflater.inflate(R.layout.guideitem2, null));
				pageViews.add(inflater.inflate(R.layout.guideitem3, null));
//				pageViews.add(inflater.inflate(R.layout.guideitem4, null));
				View view4=inflater.inflate(R.layout.guideitem4, null);
				ImageView img_lijikaiqi= (ImageView) view4.findViewById(R.id.img_lijikaiqi);
				img_lijikaiqi.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Message msg=new Message();
						msg.arg1 = GET_KEY;
						msg.what = LOGIN;
						handlerUI.sendMessage(msg);
					}
				});
				pageViews.add(view4);
				guide = (ViewGroup) inflater.inflate(R.layout.guide, null);
				
				viewPager = (ViewPager) guide.findViewById(R.id.guidePages);
				viewPager.setAdapter(new GuidePageAdapter());
				tv_login = (TextView) guide.findViewById(R.id.tv_login);
				tv_register = (TextView) guide.findViewById(R.id.tv_register);

				ll_find_ads_dots = (LinearLayout) guide.findViewById(R.id.ll_find_ads_dot);
				smalldots = new ImageView[pageViews.size()];
				for (int i = 0; i < smalldots.length; i++) {
					smalldot = new RoundImageView(this);

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					lp.setMargins(30, 0, 0, 0);
					smalldot.setLayoutParams(lp);
					smalldots[i] = smalldot;
					if (i == 0) {
						smalldots[i].setBackgroundResource(R.drawable.dot_ring_checked);
					} else {
						smalldots[i].setBackgroundResource(R.drawable.dot_ring_unchecked);
					}
					ll_find_ads_dots.addView(smalldots[i]);
				}

				viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageScrolled(int i, float v, int i1) {

					}

					@Override
					public void onPageSelected(int i) {
						if (smalldots == null)
							return;
//						for (int j = 0; j < smalldots.length; j++) {
//							if (j == i) {
//								smalldots[j].setBackgroundResource(R.drawable.dot_ring_checked);
//							} else {
//								smalldots[j].setBackgroundResource(R.drawable.dot_ring_unchecked);
//							}
//						}
					}

					@Override
					public void onPageScrollStateChanged(int i) {

					}
				});

				tv_login.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Message msg=new Message();
						msg.arg1 = GET_KEY;
						msg.what = LOGIN;
						handlerUI.sendMessage(msg);
					}
				});
				tv_register.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Message msg=new Message();
						msg.arg1 = GET_KEY;
						msg.what = REGISTER;
						handlerUI.sendMessage(msg);
					}
				});
				initGuide = true;
			};
			setContentView(guide);
			}
	}

	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		public Parcelable saveState() {
			return null;
		}

		public void startUpdate(View arg0) {

		}

		public void finishUpdate(View arg0) {

		}

	}

	private RequestQueue mRequestQueue;

	private  void isgoogle(){
		mRequestQueue = Volley.newRequestQueue(this);

		JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, "http://www.google.com", null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				canOpenGoogle = true;
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				canOpenGoogle = false;
			}
		});
		mRequestQueue.add(jr);



//		HttpRequest.sendGet("http://www.google.com", "", new HttpRevMsg() {
//			@Override
//			public void revMsg(String msg) {
////				LogUtil.i("isok","---success:" );
//				canOpenGoogle = true;
//				//  Toast.makeText(getBaseContext(), "sucesss", Toast.LENGTH_LONG).show();
//			}
//
////			@Override
////			public void failed(String msg) {
////
////				//   Toast.makeText(getBaseContext(), "failed", Toast.LENGTH_LONG).show();
////				LogUtil.i("isok","---failed:" );
////			}
//		},5000);
	}
}