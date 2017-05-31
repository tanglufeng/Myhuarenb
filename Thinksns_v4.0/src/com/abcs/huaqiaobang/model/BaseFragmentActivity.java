package com.abcs.huaqiaobang.model;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.huaqiaobang.ytbt.util.AppManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

/**
 * @author xbw
 * @version 创建时间：2015年8月31日 下午4:42:57
 */
public class BaseFragmentActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        MyApplication.getInstance().addActivity(this);
        if (arg0 != null) {
            final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
//        MyApplication.list.add(this);
        AppManager.getAppManager().addActivity(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }

//		initSystemBar();
    }
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            handleMsg(msg);
        }
    };

    public void handleMsg(Message msg) {
    }

    public void post(Runnable r) {
        if (handler != null)
            handler.post(r);
    }

    public void postDelayed(final Runnable r, long time) {
        if (handler != null)
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (handler != null)
                        r.run();
                }
            }, time);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        MyApplication.list.remove(this);
        MyApplication.getInstance().removeActivity(this);
//        AppManager.getAppManager().finishActivity(this);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
    }

    public void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // tintManager.setStatusBarTintColor(Color.parseColor("#eb5041"));
            tintManager.setStatusBarTintColor(Color.parseColor("#000000"));
        }
    }

    public void initSystemBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(color);
        }
    }

//	public void login() {
//		MobclickAgent.onEvent(BaseFragmentActivity.this, "Login");
//		Intent intent = new Intent(BaseFragmentActivity.this,
//				WXEntryActivity.class);
//		startActivity(intent);
//		overridePendingTransition(R.anim.move_left_in_activity,
//				R.anim.move_right_out_activity);
//	}

    public void login(boolean... noShowPrompt) {
//		if (noShowPrompt.length == 0) {
//			new PromptDialog(this, "登录获得更多功能，是否立刻登录？", new Complete() {
//
//				@Override
//				public void complete() {
//					MobclickAgent.onEvent(BaseFragmentActivity.this, "Login");
//					Intent intent = new Intent(BaseFragmentActivity.this,
//							WXEntryActivity.class);
//					startActivity(intent);
//					overridePendingTransition(R.anim.move_left_in_activity,
//							R.anim.move_right_out_activity);
//				}
//			}).show();
//			return;
//		}
        MobclickAgent.onEvent(BaseFragmentActivity.this, "Login");
        Intent intent = new Intent(BaseFragmentActivity.this,WXEntryActivity.class);
        intent.putExtra("isthome",true);
        startActivity(intent);
        overridePendingTransition(R.anim.move_left_in_activity,
                R.anim.move_right_out_activity);
    }

    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();

        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MyApplication.getInstance().stopKillService();
    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
