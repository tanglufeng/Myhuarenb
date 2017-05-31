package com.abcs.huaqiaobang.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.Util;
import com.umeng.analytics.MobclickAgent;

/**
 * @author xbw
 * @version 创建时间：2015-6-2 上午11:04:17
 */
public class ExitDialog extends Dialog implements OnClickListener {
    Context context;

    public ExitDialog(Context context) {
        super(context, R.style.dialog);
        // TODO Auto-generated constructor stub
        setContentView(R.layout.occft_dialog_exit);
        setCanceledOnTouchOutside(false);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (Util.WIDTH * 0.9); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        ((TextView) findViewById(R.id.tljr_dialog_exit_msg))
                .setText("确定退出" + getContext().getResources().getString(R.string.app_name) + "？");
        findViewById(R.id.tljr_dialog_exit_cancel).setOnClickListener(this);
        findViewById(R.id.tljr_dialog_exit_ok).setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.tljr_dialog_exit_cancel:
                this.dismiss();
                break;
            case R.id.tljr_dialog_exit_ok:
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                    MobclickAgent.onKillProcess(context);
                }
                MyApplication.getInstance().exit();
                break;

            default:
                break;
        }
    }
}
