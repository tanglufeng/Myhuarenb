package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.tljr.news.widget.InputTools;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CompanyEditActivity extends BaseActivity implements View.OnClickListener{

    View view;
    @InjectView(R.id.et_company_name)
    EditText etCompanyName;
    @InjectView(R.id.et_company_code)
    EditText etCompanyCode;
    @InjectView(R.id.t_cancel)
    TextView tCancel;
    @InjectView(R.id.t_bind)
    TextView tBind;

    private Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_company_edit, null);
        }
        setContentView(view);
        ButterKnife.inject(this);
        tCancel.setOnClickListener(this);
        tBind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.t_cancel:
                finish();
                InputTools.HideKeyboard(v);
                break;
            case R.id.t_bind:
                editCompany();
                InputTools.HideKeyboard(v);
                break;
        }
    }

    private void editCompany() {
        String companyName=etCompanyName.getText().toString().trim();
        String companyCode=etCompanyCode.getText().toString().trim();
        if(TextUtils.isEmpty(companyName)){
            showToast("请输入企业名称");
            return;
        }else if(TextUtils.isEmpty(companyCode)){
            showToast("请输入企业邀请码");
            return;
        }
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_edit_company_connect, "&key=" + MyApplication.getInstance().getMykey() + "&enterprise_name=" + companyName + "&invitation_code=" + companyCode, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            Log.i("zjz", "bind_msg=" + msg);
                            if (object.getInt("code") == 200) {
                                JSONObject errObj = object.optJSONObject("datas");
                                if (errObj != null) {
                                    showToast(errObj.optString("error"));
                                } else {
                                    showToast("修改绑定企业成功！");
                                    MyUpdateUI.sendUpdateCollection(CompanyEditActivity.this, MyUpdateUI.BINDCOMPANY);
                                    Intent intent = new Intent();
                                    setResult(3, intent);
                                    finish();
                                }

                                ProgressDlgUtil.stopProgressDlg();
                            } else {
                                ProgressDlgUtil.stopProgressDlg();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                            ProgressDlgUtil.stopProgressDlg();
                        } finally {
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }
}
