package com.abcs.haiwaigou.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CompanyConnectActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.seperate)
    View seperate;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.et_company_name)
    EditText etCompanyName;
    @InjectView(R.id.et_company_code)
    EditText etCompanyCode;
    @InjectView(R.id.t_bind)
    TextView tBind;
    @InjectView(R.id.relative_edit)
    RelativeLayout relativeEdit;
    @InjectView(R.id.linear_company_name)
    RelativeLayout linearCompanyName;
    @InjectView(R.id.linear_company_code)
    RelativeLayout linearCompanyCode;
    @InjectView(R.id.linear_my_company_name)
    RelativeLayout linearMyCompanyName;
    @InjectView(R.id.linear_my_company_code)
    RelativeLayout linearMyCompanyCode;
    @InjectView(R.id.t_company_name)
    TextView tCompanyName;
    @InjectView(R.id.t_company_code)
    TextView tCompanyCode;

    private View view;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.hwg_activity_company_connect_acitivity, null);
        }
        setContentView(view);
        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            seperate.setVisibility(View.VISIBLE);
        }



        tBind.setOnClickListener(this);
        relativeBack.setOnClickListener(this);
        relativeEdit.setOnClickListener(this);
        if (MyApplication.getInstance().getMykey() != null) {
            initCompany();
        }
    }


    private void initCompany() {
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_find_company_connect, "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            Log.i("zjz", "find_company_msg=" + msg);
                            if (object.getInt("code") == 200) {
                                JSONObject errObj = object.optJSONObject("datas");
                                if (errObj == null) {
                                    JSONArray array = object.optJSONArray("datas");
                                    if (array != null && array.length() != 0) {
                                        JSONObject jsonObject = array.getJSONObject(0);
                                        jsonObject.optString("id");
                                        if (tCompanyName != null)
                                            tCompanyName.setText(jsonObject.optString("enterprise_name"));
                                        if (tCompanyCode != null)
                                            tCompanyCode.setText(jsonObject.optString("invitation_code"));
                                        jsonObject.optString("discount");
                                        jsonObject.optString("invitation_code");
                                        jsonObject.optString("member_id");
                                        jsonObject.optString("member_name");
                                        if (relativeEdit != null)
                                            relativeEdit.setVisibility(View.VISIBLE);
                                        if (linearCompanyName != null)
                                            linearCompanyName.setVisibility(View.INVISIBLE);
                                        if (linearCompanyCode != null)
                                            linearCompanyCode.setVisibility(View.INVISIBLE);
                                        if (tBind != null)
                                            tBind.setVisibility(View.INVISIBLE);
                                        if (linearMyCompanyCode != null)
                                            linearMyCompanyCode.setVisibility(View.VISIBLE);
                                        if (linearMyCompanyName != null)
                                            linearMyCompanyName.setVisibility(View.VISIBLE);

                                    }
                                } else {
                                    Log.i("zjz", "no_bind");
                                    if (linearCompanyName != null)
                                        linearCompanyName.setVisibility(View.VISIBLE);
                                    if (etCompanyCode != null && etCompanyName != null) {
                                        etCompanyName.setVisibility(View.VISIBLE);
                                        etCompanyCode.setVisibility(View.VISIBLE);
                                    }
                                    if (linearCompanyCode != null)
                                        linearCompanyCode.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.t_bind:
                bindCompany();
                break;
            case R.id.relative_back:
                finish();
                InputTools.HideKeyboard(view);
                break;
            case R.id.relative_edit:
                Intent intent=new Intent(this,CompanyEditActivity.class);
                startActivityForResult(intent,3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3 && resultCode == 3 && data != null){
            initCompany();
        }
    }

    private void bindCompany() {
        String companyName = etCompanyName.getText().toString().trim();
        String companyCode = etCompanyCode.getText().toString().trim();
        if (TextUtils.isEmpty(companyName)) {
            showToast("请输入企业名称");
            return;
        } else if (TextUtils.isEmpty(companyCode)) {
            showToast("请输入企业邀请码");
            return;
        }
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_company_connect, "&key=" + MyApplication.getInstance().getMykey() + "&enterprise_name=" + companyName + "&invitation_code=" + companyCode, new HttpRevMsg() {
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
                                    showToast("绑定企业成功！");
                                    MyUpdateUI.sendUpdateCollection(CompanyConnectActivity.this, MyUpdateUI.BINDCOMPANY);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
