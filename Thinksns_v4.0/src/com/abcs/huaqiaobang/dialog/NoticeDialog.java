package com.abcs.huaqiaobang.dialog;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.util.Util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;


public class NoticeDialog {

    private static Dialog noticeDialog;
    private static TextView textView;
    private static Activity context;
    private static Handler handler = new Handler();

    /**
     * 启动进度条
     *
     * @param strMessage 进度条显示的信息
     * @param activity   当前的activity
     */
    public static void showNoticeDlg(final String msg, final Activity context) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (null == noticeDialog || NoticeDialog.context != context) {
                    // extractedOne(context);
                    getView(context);
                } else {
                    if (noticeDialog.isShowing()) {
                    } else if (!context.isFinishing()) {
                        noticeDialog.show();
                    }
                }
                textView.setText(msg);
            }
        });
    }

    private static void getView(Activity context) {
        NoticeDialog.context = context;
        textView = new TextView(context);
        textView.setBackground(context.getResources().getDrawable(
                R.drawable.img_tishikuang));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(context.getResources().getColor(
                R.color.tljr_white));
        textView.setTextSize(18);
        noticeDialog = new Dialog(context, R.style.dialog);
        noticeDialog.setCancelable(true);
        noticeDialog.setCanceledOnTouchOutside(false);
        noticeDialog.setContentView(textView, new LayoutParams(
                (int) (Util.WIDTH * 0.8), (int) (Util.HEIGHT * 0.1)));// 设置布局
        Window dialogWindow = noticeDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = (int) (Util.HEIGHT * 0.2); // 新位置Y坐标
        dialogWindow.setAttributes(lp);
        if (!context.isDestroyed()&&!context.isFinishing())
            noticeDialog.show();
    }

    /**
     * 结束进度条
     */
    public static void stopNoticeDlg() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (null != noticeDialog && noticeDialog.isShowing()) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            noticeDialog.dismiss();
                        }
                    });
                }
            }
        }, 100);

        // noticeDialog = null;
    }

}
