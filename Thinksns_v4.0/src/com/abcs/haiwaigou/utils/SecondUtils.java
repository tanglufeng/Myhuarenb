package com.abcs.haiwaigou.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.sociax.android.R;

/**
 * Created by zjz on 2016/6/28 0028.
 * 主要用于海外购的二级分类和海外购弹出的工具类
 */
public class SecondUtils {
    /**
     * 通知公告的对话框*/
    private static Dialog noticeDialog;
    private static Activity context;
    /**
     * 获取屏幕宽度*/
    public  static   int   getScreenWith(Activity    activity){
        WindowManager wm = (WindowManager) activity .getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕高度
     * */
    public   static   int   getScreenHight(Activity   activity){
        WindowManager wm = (WindowManager) activity .getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 显示公告对话框*/
    public static void showNoticeDlg(String msg, Activity context) {
        if (context==null){
            return;
        }
        SecondUtils.context=context;
        View v = View.inflate(context, R.layout.layout_hwg_dlg, null);// 得到加载view
        TextView    dlgTv=   (TextView)v.findViewById(R.id.layout_hwg_dlg_tv);
        dlgTv.setText(msg);
        Typeface fontFace = Typeface.createFromAsset(context.getAssets(),
                "font/fangzhenglantinghei.TTF");
        dlgTv.setTypeface(fontFace);
        noticeDialog = new Dialog(context, R.style.dialog);
        noticeDialog.setContentView(v, new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));// 设置布局

        noticeDialog
                .setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        noticeDialog.dismiss();
                    }
                });
        if (!context.isDestroyed() && !context.isFinishing())
            noticeDialog.show();

    }

}
