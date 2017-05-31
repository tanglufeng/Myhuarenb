package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.main.MainActivity;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * @author xbw
 * @version 创建时间：2015-5-27 上午11:47:21
 */
public class StartActivity extends BaseActivity {
    public static ImageLoader imageLoader;
    public static DisplayImageOptions options;
    private ImageView bj;
    private TextView pro;
    // private ProgressBar bar;
    private ImageView bar;
    private static String showScreen = ".HQ_SHOWSCREEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tljr_activity_start);
        MyApplication.getInstance().addActivity(this);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        if (Util.preference.getBoolean("ydlogin", false)) {
            showToast("账号异地登陆！请修改密码！");
            Util.preference.edit().putBoolean("ydlogin", false).commit();
        }
        initStartView();

    }

    private void initStartView() {
        pro = (TextView) findViewById(R.id.tljr_txt_jindu);
        bar = (ImageView) findViewById(R.id.tljr_pro_qd);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bj = (ImageView) findViewById(R.id.tljr_img_qdbj);
        try {
            String data = Util.getStringFromFile(showScreen);
            System.err.println("data:" + data);
            JSONObject obj = new JSONObject(data);
            String url;
            if (!obj.getString("fixed").equals("")) {
                url = obj.getString("fixed");
            } else {
                JSONArray array = obj.getJSONArray("common");
                url = array.getString(new Random().nextInt(array.length()));
            }
            System.err.println("url:" + url);
            Util.setImageOnlyDown(url, bj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pro_move_left_in));
        ((TextView) findViewById(R.id.tljr_txt_info)).setText(getVersion());
        // String TAG = "屏幕信息";
        // int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        // // 屏幕宽（像素，如：480px）
        // int screenHeight =
        // getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高（像素，如：800p）
        //
        // Log.e(TAG + "  getDefaultDisplay", "screenWidth=" + screenWidth
        // + "; screenHeight=" + screenHeight);

//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                getUrl();
////                init();
//                new InitData(StartActivity.this);
////                loadOnlineCfg();
//            }
//        }).start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 2000);
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

    private void init() {
        Constent.init();
        // 友盟增量更新
        UmengUpdateAgent.setUpdateCheckConfig(false);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        // UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus,
                                         UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case 0: // has update
                        UmengUpdateAgent.showUpdateDialog(getApplicationContext(),
                                updateInfo);
                        break;
                    case 1: // has no update
                        showToast("已是最新版本");
                        break;
                    case 2: // none wifi
                        showToast("没有wifi连接， 只在wifi下更新");
                        break;
                    case 3: // time out
                        showToast("超时");
                        break;

                }
            }

        });
        // 友盟数据统计
        MobclickAgent.updateOnlineConfig(this);

        // try {
        // String strTemp = Constent.preference.getString("newTypeSort", null);
        // if (strTemp != null) {
        // JSONArray array = new JSONArray(strTemp);
        // HuanQiuShiShi.TypeNameNames = new String[array.length()];
        // for (int i = 0; i < array.length(); i++) {
        // HuanQiuShiShi.TypeNameNames[i] = array.getString(i);
        // }
        // HuanQiuShiShi.newsTypeName = HuanQiuShiShi.TypeNameNames[0];
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

    private void getUrl() {
        HttpRequest.sendPost(TLUrl.getInstance().URL_AD, "", new HttpRevMsg() {

            @Override
            public void revMsg(String msg) {
                try {
                    JSONObject object = new JSONObject(msg);
                    Log.i("zjz","start_act="+msg);
                    if (object.getInt("status") == 1) {
                        JSONObject obj = object.getJSONObject("result");
                        try {
                            JSONArray array = obj.getJSONArray("common");
                            for (int i = 0; i < array.length(); i++) {
                                Util.onlyDownImage(array.getString(i));
                            }
                            byte[] b = obj.toString().getBytes();
                            Util.writeFileToFile(showScreen, b, b.length);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                } catch (JSONException e) {
                    Log.i("zjz","start_act失败！");
                    e.printStackTrace();
                }
            }
        });
    }

    public void showMessage(String msg) {
        Message message = new Message();
        message.obj = msg;
        handler.sendMessage(message);
    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    bar.clearAnimation();
                    bar.setX(0);
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            StartActivity.this.finish();
                        }
                    }, 500);
                    Intent intent = new Intent();
                    intent.setClass(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                default:
                    pro.setText(msg.obj.toString());
                    // Toast.makeText(StartActivity.this, msg.obj.toString(),
                    // Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    public static void loadOnlineCfg() {
        String _aboutUsUrl = MobclickAgent.getConfigParams(
                MyApplication.getInstance(), "aboutUsUrl");
        if (_aboutUsUrl.length() > 0) {
            Constent.aboutUsUrl = _aboutUsUrl;
        }
        MobclickAgent
                .setOnlineConfigureListener(new UmengOnlineConfigureListener() {
                    @Override
                    public void onDataReceived(JSONObject arg0) {
                        if (arg0 != null) {
                            Constent.aboutUsUrl = arg0.optString("aboutUsUrl")
                                    .length() == 0 ? "http://42.120.45.171:8008/weiXin/aboutUs.html"
                                    : arg0.optString("aboutUsUrl");
                        }
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
 


