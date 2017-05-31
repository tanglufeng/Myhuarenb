package com.abcs.huaqiaobang.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.model.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NoticeDialogActivity extends BaseActivity {

    @InjectView(R.id.tljr_dialog_exit_msg)
    TextView tljrDialogExitMsg;
    @InjectView(R.id.tljr_dialog_exit_ok)
    Button tljrDialogExitOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_dialog);
        ButterKnife.inject(this);

        if (getIntent() != null) {
            tljrDialogExitMsg.setText(getIntent().getStringExtra("msg"));
        }

        tljrDialogExitOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
