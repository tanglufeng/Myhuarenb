package com.abcs.huaqiaobang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.alibaba.fastjson.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/2/2.
 */
public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener {


    //    @InjectView(R.id.tljr_mdy_pwd)
//    EditText tljrMdyPwd;
//    @InjectView(R.id.tljr_mdy_n_pwd)
//    EditText tljrMdyNPwd;
//    @InjectView(R.id.tljr_mdy_n_rpwd)
//    EditText tljrMdyNRpwd;
//    @InjectView(R.id.btn_cancel)
//    Button btnCancel;
//    @InjectView(R.id.btn_ok)
//    Button btnOk;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tljr_mdy_pwd)
    EditText tljrMdyPwd;
    @InjectView(R.id.tljr_mdy_n_pwd)
    EditText tljrMdyNPwd;
    @InjectView(R.id.tljr_mdy_n_rpwd)
    EditText tljrMdyNRpwd;
    @InjectView(R.id.btn_cancel)
    Button btnCancel;
    @InjectView(R.id.btn_ok)
    Button btnOk;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.occft_dialog_modiypwd);
        setContentView(R.layout.hwg_edit_pwd);
        ButterKnife.inject(this);

        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        relativeBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {

            case R.id.btn_cancel:

                imm.hideSoftInputFromWindow(tljrMdyNRpwd.getWindowToken(), 0);
                finish();

                break;
            case R.id.relative_back:

                imm.hideSoftInputFromWindow(tljrMdyNRpwd.getWindowToken(), 0);
                finish();

                break;
            case R.id.btn_ok:

                modifyPwd();

                break;
        }

    }

    private void modifyPwd() {
        String str_pwd = tljrMdyPwd.getText().toString().trim();
        String str_n_pwd = tljrMdyNPwd.getText().toString().trim();
        String str_n_rpwd = tljrMdyNRpwd.getText().toString().trim();
        if (str_pwd.length() == 0) {
            showToast("请输入密码");
            return;
        }

        if (str_n_pwd.length() == 0) {
            showToast("请输入新密码");
            return;
        }
        if (str_n_rpwd.length() == 0) {
            showToast("请输入确认新密码");
            return;
        }
        if (!str_n_pwd.equals(str_n_rpwd)) {
            showToast("两次密码不一致");
            return;
        }
        if (str_n_pwd.length() < 6 && str_n_pwd.length() < 20) {
            showToast("密码长度不对");
            return;
        }
        if (!Util.checkPassword(str_n_pwd)) {
            showToast("密码应由英文+数字组成");
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tljrMdyNRpwd.getWindowToken(), 0);
//                MobclickAgent.onEvent(activity, "modifyPwd");
        String postUrl = TLUrl.getInstance().URL_user + "changepwd?iou=1";
        String param = "uname="
                + MyApplication.getInstance().self.getUserName()
                + "&pwd=" + str_pwd + "&changepwd=" + str_n_pwd;
        ProgressDlgUtil.showProgressDlg("修改中", this);
        HttpRequest.sendPost(postUrl, param, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        ProgressDlgUtil.stopProgressDlg();
                        if (msg.length() == 0) {
//                            Snackbar.make(myRlModifyPassword, "修改失败", Snackbar.LENGTH_SHORT).show();
                            showToast("修改失败");
                            return;
                        }
                        JSONObject obj = JSONObject.parseObject(msg);
                        Log.i("zjz", "modify_pwd=" + msg);
                        int code = obj.getIntValue("code");
                        if (code == 1) {
//                            Snackbar.make(myRlModifyPassword, "修改成功", Snackbar.LENGTH_SHORT).show();
                            showToast("修改成功，请重新登录");
                            Intent intent = new Intent();
                            setResult(1, intent);
                            finish();
                            MyApplication.getInstance().getMainActivity().mHandler.sendEmptyMessage(6);
                        } else {
//                            activity.showToast("修改密码失败");
                            showToast("修改失败");
                        }

                    }
                });

            }
        });

    }
}
