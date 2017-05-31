package com.abcs.haiwaigou.yyg.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcs.sociax.android.R;

import butterknife.InjectView;

/**
 * Created by Administrator on 2016/8/23.
 */
public class ShowLotteryMsgDialog {
    Dialog dialog;
    Context context;
    boolean isEntity;

    public ShowLotteryMsgDialog(Context context, boolean isEntity, String name, String code, String act_id, String goods_id) {
        this.context = context;
        this.isEntity = isEntity;
        initDialog(name, code, act_id, goods_id);
    }

    private void initDialog(String name, final String code, final String act_id, final String goods_id) {
        View diaView = View.inflate(context, R.layout.hwg_activity_yyglottery_msg, null);
        TextView tCode = (TextView) diaView.findViewById(R.id.t_code);
        tCode.setText("期号：" + code);
        TextView tName = (TextView) diaView.findViewById(R.id.t_name);
        tName.setText(name);
        ImageView imgBtn = (ImageView) diaView.findViewById(R.id.img_btn);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, YYGSetAddressActivity.class);
                intent.putExtra("act_id", act_id);
                intent.putExtra("goods_id", goods_id);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        ImageView imgCancel = (ImageView) diaView.findViewById(R.id.img_cancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (!isEntity) {
            imgBtn.setVisibility(View.GONE);
        }
        dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(diaView);
        Window win = dialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        dialog.show();

    }


}
