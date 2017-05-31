package com.abcs.huaqiaobang.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

public class ProgressDlgUtil {

    private static Dialog loadingDialog;
    private static Activity context;
    private static ImageView iv;
    private static int[] bjs = {R.drawable.img_jiazai1,
            R.drawable.img_jiazai2, R.drawable.img_jiazai3,
            R.drawable.img_jiazai4, R.drawable.img_jiazai5,
            R.drawable.img_jiazai6, R.drawable.img_jiazai7,
            R.drawable.img_jiazai8, R.drawable.img_jiazai9,
            R.drawable.img_jiazai10, R.drawable.img_jiazai11,
            R.drawable.img_jiazai12, R.drawable.img_jiazai13};
    private static boolean isStart = false;
    private static Handler handler = new Handler();
    private static int show = 0;
    private static TextView tv_msg;

    /**
     * 启动进度条
     *
     */
    public static void showProgressDlg(String msg, Activity context) {
//        context = MyApplication.getInstance().getNowActivity();
        if (null == loadingDialog || ProgressDlgUtil.context != context) {
            if (!context.isDestroyed() && !context.isFinishing()) {
                init(context, msg);
            }
        } else {
            if (loadingDialog.isShowing()) {
            } else if (!context.isDestroyed() && !context.isFinishing()) {

//                tv_msg.setText(msg);
                loadingDialog.show();
//                show = 0;
//                isStart = true;
//                handler.postDelayed(runnable, 100);
            }
        }
    }

    private static void init(final Activity context, String msg) {
        if (null != loadingDialog && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        if (context == null)
            return;
        //Home键问题
//        try {

        ProgressDlgUtil.context = context;
        System.err.print(context);
        ////////////////////////
        View v = View.inflate(context, R.layout.dialog_loading, null);// 得到加载view

//        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
//        tipTextView.setText(msg);// 设置加载信息

        /////////////////////////////
//        View v = View.inflate(context, R.layout.occft_dialog_loading_icon, null);// 得到加载view
//        iv = (ImageView) v.findViewById(R.id.img_app_icon1);
//        tv_msg =
//                (TextView) v.findViewById(R.id.textView1);
//        tv_msg.setText(msg);
//        show = 0;
//        isStart = true;
//        new Thread(runnable).start();
        loadingDialog = new Dialog(context, R.style.dialog);
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(v, new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));// 设置布局
        loadingDialog
                .setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        stopProgressDlg();
                    }
                });

        if (!context.isDestroyed() && !context.isFinishing())
            loadingDialog.show();

//        } catch (Exception e) {
//
//        }

    }

    private static Runnable runnable = new Runnable() {

        @Override
        public void run() {
            show++;
            if (show == bjs.length) {
                show = 0;
            }
            iv.post(runnable1);
            if (isStart)
                handler.postDelayed(runnable, 100);
        }
    };
    private static Runnable runnable1 = new Runnable() {

        @Override
        public void run() {
            iv.setBackgroundResource(bjs[show]);
        }
    };

    /**
     * 结束进度条
     */
    public static void stopProgressDlg() {
        if (null != loadingDialog && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
//        isStart = false;
        // loadingDialog = null;
    }
}
