package com.abcs.huaqiaobang.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    @InjectView(R.id.feedback)
    MultiAutoCompleteTextView feedback;
    @InjectView(R.id.use_phone)
    EditText usePhone;
    @InjectView(R.id.btn_commit)
    Button btnCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.inject(this);

        findViewById(R.id.tljr_per_btn_lfanhui).setOnClickListener(this);
        btnCommit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tljr_per_btn_lfanhui:
                finish();
                break;

            case R.id.btn_commit:
                String str=feedback.getText().toString();
                if(str.length()==0){
                    showToast("请输入您的问题或意见！");
                }else {
                    upFeedBack();
                }
                break;

        }

    }

    Handler handler=new Handler();

    private void upFeedBack() {


//
//        type   1为ios  2为安卓
//        context  反馈描述
//        phone   联系电话  可选
        ProgressDlgUtil.showProgressDlg("",this);
        
        HttpRequest.sendPost(TLUrl.getInstance().URL_hqb_feedback, "&key=" + MyApplication.getInstance().getMykey()+"&type=2"+"&context="+feedback.getText().toString().trim()+"&phone="+usePhone.getText().toString().trim(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            
                            if(TextUtils.isEmpty(msg)){
                                ProgressDlgUtil.stopProgressDlg();
                                return;
                            }

                            Log.i("zds", "msg=" + msg);
                            
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("state") == 1) {
                                ProgressDlgUtil.stopProgressDlg();
                                
                                showToast(object.optString("msg"));
                                finish();
                               
                            } else {
                                ProgressDlgUtil.stopProgressDlg();
                                Log.i("zds", "goodsDetail:解析失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zds", e.toString());
                            Log.i("zds", msg);
                            e.printStackTrace();
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }
}
