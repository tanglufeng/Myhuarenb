package com.abcs.huaqiaobang.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.abcs.sociax.android.R;


/**
 * Created by zjz on 2016/7/29 0029.
 */
public class ShowMessageDialog {

    PopupWindow popupWindow = null;
    View view;
    Activity activity;
    int with;
    public ShowMessageDialog(View view, Activity activity, int with, String str, String title) {
        this.view=view;
        this.activity=activity;
        this.with=with;
        initPopWindow(title,str);
    }

    public void initPopWindow(String title,String str) {

        View popview = LayoutInflater.from(activity).inflate(R.layout.hwg_pop_point_detail, null);
        TextView t_text= (TextView) popview.findViewById(R.id.tljr_dialog_exit_msg);
        TextView t_title= (TextView) popview.findViewById(R.id.tljr_txt_share_title);
        t_title.setText(title);
        t_text.setText(str);
//        ViewGroup.LayoutParams layoutParams=new LinearLayout.LayoutParams(Util.WIDTH*2/3, ViewGroup.LayoutParams.WRAP_CONTENT);
//        popview.setLayoutParams(layoutParams);
        popupWindow = new PopupWindow(popview, with, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.5f;
        activity.getWindow().setAttributes(params);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow()
                        .getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ffffff")));
        Button btnOk = (Button) popview.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//        popupWindow.setContentView(popview);
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(true);

    }
}
