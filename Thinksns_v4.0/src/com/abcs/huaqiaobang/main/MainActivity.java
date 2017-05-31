package com.abcs.huaqiaobang.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.fragment.HWGMainFragment;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.MyProductActivity;
import com.abcs.huaqiaobang.activity.RegularActivity;
import com.abcs.huaqiaobang.activity.StartActivity;
import com.abcs.huaqiaobang.chart.RealTimeView;
import com.abcs.huaqiaobang.dialog.ExitDialog;
import com.abcs.huaqiaobang.dialog.NoticeDialog;
import com.abcs.huaqiaobang.fragment.MainFragment2;
import com.abcs.huaqiaobang.fragment.MyFragment3;
import com.abcs.huaqiaobang.fragment.Regular;
import com.abcs.huaqiaobang.fragment.SheQuFragment;
import com.abcs.huaqiaobang.login.AutoLogin;
import com.abcs.huaqiaobang.login.LoginResultReceiver;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;
import com.abcs.huaqiaobang.model.MainFragmentLayout;
import com.abcs.huaqiaobang.model.Product;
import com.abcs.huaqiaobang.model.StatusBarCompat;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.abcs.huaqiaobang.tljr.news.HuanQiuShiShi;
import com.abcs.huaqiaobang.tljr.news.channel.bean.ChannelItem;
import com.abcs.huaqiaobang.tljr.news.store.DBManager;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yuntongxun.ecsdk.ECDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

@SuppressLint("HandlerLeak")
public class MainActivity extends BaseFragmentActivity {
    protected static final String TAG = "LOG_DEBUG";

    @InjectView(R.id.main_bottom_navigation)
    RadioGroup mainBottomNavigation;
    @InjectView(R.id.notice_icon)
    ImageView noticeIcon;
    private FragmentViewPagerAdapter adapter;
    public ViewPager viewpager;
    private List<Fragment> list = null;
    private TitleBar titleBar;
    public RelativeLayout v;
    private boolean isPause = false;
    //    public Main main;
    public MainFragment2 main;
    //    public Current current;
    public static HuanQiuShiShi huanQiuShiShi;// 新闻
    public Regular regular;
    //    public My my;
//    public MyFragment2 my;
//    public MyFragment my;
    public MyFragment3 my;
    public ArrayList<Product> regularList = new ArrayList<Product>();// 定期
    public ArrayList<Product> currentList = new ArrayList<Product>();// 活期
    //    public ArrayList<Product> vipList;// vip
    public ArrayList<Product> vipList = new ArrayList<Product>();// vip
    //	private ChartActivity chart;
    private boolean jsonerror = false;

    //zjz
//    public HaiWaiGou haiWaiGou;
//    public HaiwaiGouFragment haiWaiGou;
//    public HWGFragment haiWaiGou;
    public HWGMainFragment haiWaiGou;
    public static ImageView shopcar;
    public RelativeLayout rl_shopcar;

    /**
     * 用户栏目列表
     */
    public static ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
    /**
     * 其它栏目列表
     */
    public static ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
    private LinearLayout line_bottom;
    private DBManager dbManager;
    public ArrayList<MainFragmentLayout> maintype_layout_list;
    private String layout;
    public String[] type_str = new String[]{
            "Main", "Regular", "News", "My"
    };
    private Map<String, Fragment> fragmentMap;
    private boolean isBind = false;
    private boolean isPrepaer = false;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private static final int FRAGMENT_MAIN_POSITION = 0;
    private static final int FRAGMENT_SHOPPING_POSITION = 1;
    private static final int FRAGMENT_NEWS_POSITION = 2;
    private static final int FRAGMENT_DINGQI_POSITION = 3;
    private static final int FRAGMENT_MY_POSITION = 4;
    public static final int NOTIFYCATION_CHANGE = 100;//未读消息

    public static final int NOTIFYCATION_CHANGE2 = 101;//已读消息
    public static final String BAIDULOCATION = "com.abcs.huaqiaobang.mypersonallocation";//定位广播
    public Handler mHandler;
    public LocationClient mLocationClient;
    private String location;
    private SheQuFragment sheQuFragment;
    private LoginResultReceiver resultReceiver;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.hqbactivity_main);
        ButterKnife.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Translucent status bar
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            setTranslucentStatus(true);
            StatusBarCompat.compat(this, true);
        }
//        }

//        upDateAPP();
        //初始百度lbs
        initBaiduLbs();

        mLocationClient.start();
        Util.init();
        StartActivity.imageLoader = ImageLoader.getInstance();

        dbManager = new DBManager(this);

        mHandler = new MainHandler();
        initMain();
        //zjz
        rl_shopcar = (RelativeLayout) getLayoutInflater().inflate(
                R.layout.hwg_bottom_goods_shopcar, null);
        rl_shopcar.setGravity(RelativeLayout.ALIGN_PARENT_BOTTOM);
        shopcar = (ImageView) rl_shopcar.findViewById(R.id.shopcar);

        manager = getSupportFragmentManager();

        mainBottomNavigation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = manager.beginTransaction();
                switch (checkedId) {
                    //主界面
                    case R.id.fragment_main:
                        hintFragment();
                        if (main == null) {
                            main = new MainFragment2();
                            transaction.add(R.id.content, main);
                        } else {
                            transaction.show(main);

                        }

//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_main)).setTextColor(getResources().getColor(R.color.tljr_statusbarcolor));
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_shopping)).setTextColor(getResources().getColor(R.color.default_text));
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_news)).setTextColor(getResources().getColor(R.color.default_text));
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_my)).setTextColor(getResources().getColor(R.color.default_text));
//                        StatusBarCompat.compat(MainActivity.this, true);

                        break;
                    //海外购界面
                    case R.id.fragment_shopping:
                        hintFragment();
                        if (haiWaiGou == null) {
//                            haiWaiGou = new HaiwaiGouFragment();
//                            haiWaiGou = new HWGFragment();
                            haiWaiGou=new HWGMainFragment();
                            transaction.add(R.id.content, haiWaiGou);
                        } else {
                            transaction.show(haiWaiGou);
//                            MyUpdateUI.sendUpdateCollection(MainActivity.this, MyUpdateUI.MYHWG);
                        }
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_shopping)).setChecked(true);
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_shopping)).setTextColor(getResources().getColor(R.color.tljr_statusbarcolor));
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_main)).setTextColor(getResources().getColor(R.color.default_text));
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_news)).setTextColor(getResources().getColor(R.color.default_text));
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_my)).setTextColor(getResources().getColor(R.color.default_text));
//                        StatusBarCompat.compat(MainActivity.this,  getResources().getColor(R.color.statusbar), true);
                        break;

                    //新闻界面
                    case R.id.fragment_news:
                        hintFragment();
                        if (huanQiuShiShi == null) {
                            huanQiuShiShi = new HuanQiuShiShi();
                            transaction.add(R.id.content, huanQiuShiShi);
                        } else {
                            transaction.show(huanQiuShiShi);
                        }
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_news)).setTextColor(getResources().getColor(R.color.tljr_statusbarcolor));
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_main)).setTextColor(getResources().getColor(R.color.default_text));
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_shopping)).setTextColor(getResources().getColor(R.color.default_text));
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_my)).setTextColor(getResources().getColor(R.color.default_text));
//                        StatusBarCompat.compat(MainActivity.this, getResources().getColor(R.color.statusbar), true);
                        break;

                    //社区界面
//                    case R.id.fragment_shequ:
//                        hintFragment();
//                        mHandler.sendEmptyMessage(NOTIFYCATION_CHANGE2);
//                        if (sheQuFragment == null) {
//                            sheQuFragment = new SheQuFragment();
//                            transaction.add(R.id.content, sheQuFragment);
//                        } else {
//                            transaction.show(sheQuFragment);
//
//                        }
////                        StatusBarCompat.compat(MainActivity.this,  getResources().getColor(R.color.statusbar), true);
//                        break;

                    //我的界面
                    case R.id.fragment_my:

//                        if (!Util.preference.getBoolean("islogin", false) || MyApplication.getInstance().self == null) {
//                            startActivity(new Intent(MainActivity.this, WXEntryActivity.class));
//                            trun2Fragment(0);
//                            return;
//                        } else {

                            hintFragment();
                            if (my == null) {
                                my = new MyFragment3();
//                                my2=new MyFragment();
                                transaction.add(R.id.content, my);
                            } else {
                                transaction.show(my);
                                MyUpdateUI.sendUpdateCollection(MainActivity.this, MyUpdateUI.MYORDERNUM);
                            }
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_my)).setTextColor(getResources().getColor(R.color.tljr_statusbarcolor));
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_main)).setTextColor(getResources().getColor(R.color.default_text));
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_shopping)).setTextColor(getResources().getColor(R.color.default_text));
//                        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_news)).setTextColor(getResources().getColor(R.color.default_text));
//                            StatusBarCompat.compat(MainActivity.this, true);
//                        }
                        break;
                }

//                transaction.commit();
                transaction.commitAllowingStateLoss();
            }
        });
        ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_main)).setChecked(true);

    }


    /*private void upDateAPP() {

//        tvVUpdateDate.setText(getResources().getString(R.string.update_date) + Util.format.format(new Date()));
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在检查更新");
        dialog.show();
        PgyUpdateManager.register(this,
                new UpdateManagerListener() {


                    @Override
                    public void onUpdateAvailable(final String result) {
                        dialog.dismiss();

                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        new PromptDialog(MainActivity.this, "有新版本，是否更新?",
                                new Complete() {

                                    @Override
                                    public void complete() {

                                        Log.i("tga", "check Bulid====");
                                        startDownloadTask(MainActivity.this,
                                                appBean.getDownloadURL());
                                    }
                                }).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "已经是最新版", Toast.LENGTH_SHORT).show();
                        Log.i("tga", "check Bulid====");
                    }
                });

        if (!MyApplication.isupdate) {
            UmengUpdateAgent.update(this);
            MyApplication.isupdate = true;
        }

    }*/

    private void initBaiduLbs() {
        final Intent intent = new Intent(BAIDULOCATION);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.setLocOption(Util.initLocation());
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                intent.putExtra("location", bdLocation.getCity() + "·" + bdLocation.getDistrict());
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                    sendBroadcast(intent);
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                    sendBroadcast(intent);
                }
            }

        });

    }

    private void hintFragment() {

        if (main != null)
            transaction.hide(main);
        if (haiWaiGou != null)
            transaction.hide(haiWaiGou);
        if (huanQiuShiShi != null)
            transaction.hide(huanQiuShiShi);
        if (sheQuFragment != null)
            transaction.hide(sheQuFragment);
        if (my != null)
            transaction.hide(my);


    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void bindChartNotify() {

        Intent intent = new Intent("com.abct.occft.hq.notifychart");
        intent.setPackage(getPackageName());
        bindService(intent, conn, BIND_AUTO_CREATE);
     //   resultReceiver = new LoginResultReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.abct.occft.hq.login");
        registerReceiver(resultReceiver, filter);
        isBind = true;
    }

    public void startVipActivity(Product product) {
        Intent intent;
//        if (product.getProductType() == 2) {
//            intent = new Intent(this, CurrentActivity.class);
//        } else {
        intent = new Intent(this, RegularActivity.class);
//        }
        try {
            intent.putExtra("info", Util.getStringByObject(product));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }


    public void showMessage(String msg) {
        Message message = new Message();
        message.obj = msg;

        mHandler.sendMessage(message);
    }

    public void showMessage(int what, String msg) {
        Message message = new Message();
        message.what = what;
        message.obj = msg;
        mHandler.sendMessage(message);
    }


    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    NoticeDialog.showNoticeDlg("正在加载中...", MainActivity.this);
                    break;
                case 2:
                    NoticeDialog.stopNoticeDlg();
                    break;

                case 6://修改密码或退出

                    logout("true");
                    break;

                case 95:
//                    main.notify_circle.setVisibility(View.VISIBLE);
//                    main.notify_circle.setText(msg.arg1 + "");
                    break;
                case 900:// 支付成功重新刷新数据界面
//				current.initUser();
//                    my.myFinanceFragment.getUserCash();
                    Activity activity = MyApplication.getInstance().activityMap
                            .get("com.abcs.occft.activity.OneMyProductActivity");
                    if (activity != null)
                        activity.finish();

                    Activity activity1 = MyApplication.getInstance().activityMap
                            .get("com.abcs.occft.activity.MyProductActivity");
                    if (activity1 != null) {
                        ((MyProductActivity) activity1).getMyProduct();
                    }
                    break;
                case 901:// 银行卡数据改变刷新
                    if (MyApplication.getInstance().self != null) {
//                        my.getUserBanks();
                    }
                    break;
                case 902:// 身份绑定改变
                    if (MyApplication.getInstance().self != null) {
//                        my.checkUserisbindidenity();
                    }
                    break;
                case 1000:// 网络变化
                    Log.e("网络标签....", "网络变化了");
                    break;
                default:
                    showToast(msg.obj.toString());

                    break;
                case NOTIFYCATION_CHANGE:
                    noticeIcon.setVisibility(View.VISIBLE);

                    break;
                case NOTIFYCATION_CHANGE2:
                    noticeIcon.setVisibility(View.GONE);

                    break;
            }
        }
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

    private ImageView bj;
    private TextView pro;
    // private ProgressBar bar;
    private ImageView bar;
    private static String showScreen = ".HQ_SHOWSCREEN";


    private void initMain() {
        isPause = false;

//绑定ChartNotifyservice

        bindChartNotify();


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        isPrepaer = true;
        RealTimeView.im_realtimeid = Constent.preference.getString("im_realtimeid", "");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (!isPrepaer) {
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event != null
                && event.getRepeatCount() == 0 && isPrepaer) {
            showNoticeDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void logout() {

        Util.token = "";
        AutoLogin.isFirstStartApp = false;
        Constent.preference.edit().putString("news_p_id", "0").commit();
        Intent intent = new Intent("com.abct.occft.hq.login");
        intent.putExtra("type", "logout");
        intent.putExtra("msg", "true");
        sendBroadcast(intent);
        Util.preference.edit().putBoolean("islogin", false).commit();
        ECDevice.logout(new ECDevice.OnLogoutListener() {
            @Override
            public void onLogout() {
//                Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                for (Activity a : MyApplication.list) {
//                    if (a instanceof MainActivity) {
//                        continue;
//                    } else {
//                        a.finish();
//                    }
//                }
            }
        });
    }

    public void logout(String msg) {

        Util.token = "";
        AutoLogin.isFirstStartApp = false;
        Constent.preference.edit().putString("news_p_id", "0").commit();
        Intent intent = new Intent("com.abct.occft.hq.login");
        intent.putExtra("type", "logout");
        intent.putExtra("msg", msg);
        sendBroadcast(intent);
        if (Util.preference.getBoolean("islogin", false)) {
            Util.preference.edit().putBoolean("islogin", false).commit();
            ECDevice.logout(ECDevice.NotifyMode.NOT_NOTIFY, new ECDevice.OnLogoutListener() {
                @Override
                public void onLogout() {
//                    ECDevice.unInitial();
//                Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                    for (Activity a : MyApplication.list) {
//                        if (a instanceof MainActivity) {
//                            continue;
//                        } else {
//                            a.finish();
//                        }
//                    }

                }
            });
        }
        trun2Fragment(0);

        // Intent intent = new Intent(this, MainActivity.class);
        // intent.putExtra("logout", msg);
        // startActivity(intent);
    }

    public void startRegularActivity(int type) {
//        if (regularList.size() == 0 || regularList.size() < type) {
//            showToast("获取产品列表中，请稍候");
////            main.getRegular();
//            return;
//        }
        Intent intent = new Intent(this, RegularActivity.class);
        try {
            intent.putExtra("info",
                    Util.getStringByObject(regularList.get(type)));
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        startActivity(intent);
    }

    public void login() {
        MobclickAgent.onEvent(MainActivity.this, "Login");
        Intent intent = new Intent(MainActivity.this, WXEntryActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.move_left_in_activity,
                R.anim.move_right_out_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
        MobclickAgent.onPause(this);
    }

    public void showNoticeDialog() {
        if (!MainActivity.this.isFinishing()) {
            new ExitDialog(MainActivity.this).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            unbindService(conn);
            unregisterReceiver(resultReceiver);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }


    public void trun2Fragment(int position) {

        switch (position) {
            case FRAGMENT_MAIN_POSITION:
                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_main)).setChecked(true);
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_main)).setTextColor(getResources().getColor(R.color.tljr_statusbarcolor));
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_shopping)).setTextColor(getResources().getColor(R.color.default_text));
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_news)).setTextColor(getResources().getColor(R.color.default_text));
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_my)).setTextColor(getResources().getColor(R.color.default_text));
                break;
            case FRAGMENT_SHOPPING_POSITION:
                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_shopping)).setChecked(true);
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_shopping)).setTextColor(getResources().getColor(R.color.tljr_statusbarcolor));
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_main)).setTextColor(getResources().getColor(R.color.default_text));
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_news)).setTextColor(getResources().getColor(R.color.default_text));
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_my)).setTextColor(getResources().getColor(R.color.default_text));
                break;
            case FRAGMENT_NEWS_POSITION:
                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_news)).setChecked(true);
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_news)).setTextColor(getResources().getColor(R.color.tljr_statusbarcolor));
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_main)).setTextColor(getResources().getColor(R.color.default_text));
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_shopping)).setTextColor(getResources().getColor(R.color.default_text));
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_my)).setTextColor(getResources().getColor(R.color.default_text));
                break;
//            case FRAGMENT_DINGQI_POSITION:
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_shequ)).setChecked(true);
//                break;
            case FRAGMENT_MY_POSITION:
                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_my)).setChecked(true);
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_my)).setTextColor(getResources().getColor(R.color.tljr_statusbarcolor));
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_main)).setTextColor(getResources().getColor(R.color.default_text));
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_shopping)).setTextColor(getResources().getColor(R.color.default_text));
//                ((RadioButton) mainBottomNavigation.findViewById(R.id.fragment_news)).setTextColor(getResources().getColor(R.color.default_text));
                break;
        }

    }
}
