package com.abcs.haiwaigou.yyg.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.sociax.android.R;

/**
 * Created by Administrator on 2016/8/26.
 */
public class YYGLotteryMsgActivity extends BaseActivity {
    Dialog dialog;
    String code;
    String name;
    String act_id;
    String goods_id;
    boolean isEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isEntity=getIntent().getBooleanExtra("isEntity",false);
        code=getIntent().getStringExtra("periodsNum");
        name=getIntent().getStringExtra("goodsName");
        act_id=getIntent().getStringExtra("activityId");
        goods_id=getIntent().getStringExtra("hqbGoodsId");
        View diaView = View.inflate(this, R.layout.hwg_activity_yyglottery_msg, null);
        TextView tCode = (TextView) diaView.findViewById(R.id.t_code);
        tCode.setText("期号：" + code);
        TextView tName = (TextView) diaView.findViewById(R.id.t_name);
        tName.setText(name);
        ImageView imgBtn = (ImageView) diaView.findViewById(R.id.img_btn);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(YYGLotteryMsgActivity.this, YYGSetAddressActivity.class);
//                intent.putExtra("act_id", act_id);
//                intent.putExtra("goods_id", goods_id);
                Intent intent = new Intent(YYGLotteryMsgActivity.this, LotteryDetailActivity.class);
                intent.putExtra("act_id", act_id);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
        ImageView imgCancel = (ImageView) diaView.findViewById(R.id.img_cancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        if (!isEntity) {
            imgBtn.setVisibility(View.GONE);
        }
        dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(diaView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(keylistener);
        dialog.setCancelable(false);
        Window win = dialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        dialog.show();
    }
    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    } ;
}
