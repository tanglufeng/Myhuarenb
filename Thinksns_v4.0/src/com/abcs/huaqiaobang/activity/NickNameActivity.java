package com.abcs.huaqiaobang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NickNameActivity extends BaseActivity {

    @InjectView(R.id.et_nickNameInput)
    EditText etNickNameInput;
    @InjectView(R.id.dialog_cancel)
    Button dialogCancel;
    @InjectView(R.id.dialog_ok)
    Button dialogOk;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_modifynickname);
        ButterKnife.inject(this);
        etNickNameInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        etNickNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                dialogOk.setClickable(s.toString().equals("") ? true : false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void doClick(View v) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(getCurrentFocus(), InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        switch (v.getId()) {
            case R.id.dialog_ok:
                if (!etNickNameInput.getText().toString().trim().equals("")) {
                    modifyNickName(etNickNameInput.getText().toString().trim());
                } else {
                    showToast("请输入您的昵称");

                }


                break;
            case R.id.dialog_cancel:

                finish();
                break;
        }
    }

    private void modifyNickName(final String name) {


        String geturl = TLUrl.getInstance().URL_user + "changenickname?iou=1";
        String param = "token=" + Util.token + "&nickname="
                + name;
        Log.i("zds_nichen",geturl+param);

        HttpRequest.sendPost(geturl, param, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                ProgressDlgUtil.stopProgressDlg();
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            JSONObject object = new JSONObject(
                                    msg);
                            if (object.getInt("code") == 1) {
                                MyApplication.getInstance().self
                                        .setNickName(name);
                                showToast("修改成功");
                                Intent intent = new Intent();
                                intent.putExtra("name", etNickNameInput.getText().toString().trim());
                                setResult(1, intent);
                                finish();
                            }else if(object.getInt("code") == -1029){
                                showToast("昵称已存在");
                            } else {
//                                activity.showToast("修改失败");
                                showToast("修改失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
//                            activity.showToast("修改失败");
                            showToast("修改失败");
                        }

                    }
                });
            }
        });

//        MyApplication.getInstance().self.setNickName(name);


    }
}
