package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/2/1.
 */
public class SexSelectAcitivity extends BaseActivity implements View.OnClickListener {


    @InjectView(R.id.my_sex_rbMan)
    RadioButton mySexRbMan;
    @InjectView(R.id.my_sex_rbWomen)
    RadioButton mySexRbWomen;

    private Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sexselect);
        ButterKnife.inject(this);
        String sex = MyApplication.getInstance().self.getSex();

        mySexRbMan.setOnClickListener(this);
        mySexRbWomen.setOnClickListener(this);
        if (sex.equals("1")) {
            mySexRbMan.setChecked(true);
            mySexRbWomen.setChecked(false);
        } else {
            mySexRbMan.setChecked(false);
            mySexRbWomen.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.my_sex_rbMan:


                changeSex("1");
                intent.putExtra("sex", "1");

                mySexRbWomen.setChecked(false);

                break;
            case R.id.my_sex_rbWomen:
                changeSex("0");
//                showToast("修改成功");
                intent.putExtra("sex", "0");
                mySexRbMan.setChecked(false);
                break;
        }
        setResult(2, intent);

    }

    private void changeSex(String sex) {
        String postUrl = TLUrl.getInstance().URL_user
                + "changegender?iou=1";
        String params = "token="
                + Util.token + "&gender=" + sex;
        ProgressDlgUtil.showProgressDlg("修改中", this);
        HttpRequest.sendPost(postUrl, params, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zjz", "change_sex=" + msg);
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.optInt("code") == 1) {
                                showToast("修改成功");
                                ProgressDlgUtil.stopProgressDlg();
                                finish();
                            }
                            String msg1 = msg;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }
}
