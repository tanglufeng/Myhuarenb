package com.abcs.haiwaigou.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.ServerUtils;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class TestLogin extends BaseActivity {
    private EditText name, pwd;
    private View scmm;
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.occft_activity_login);
        mRequestQueue = Volley.newRequestQueue(this);
        initUI();
    }
    private void initUI() {
        findViewById(R.id.tljr_img_login).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findViewById(R.id.tljr_grp_host)
                                .setVisibility(View.INVISIBLE);
                        findViewById(R.id.tljr_grp_login)
                                .setVisibility(View.VISIBLE);
                    }
                });


        findViewById(R.id.tljr_btn_lfanhui).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        loginSuccess("");
                    }
                });



        name = (EditText) findViewById(R.id.tljr_et_lname);
        pwd = (EditText) findViewById(R.id.tljr_et_lpwd);
        scmm = findViewById(R.id.tljr_img_scmm);
        pwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    scmm.setVisibility(View.VISIBLE);
                } else {
                    scmm.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        scmm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pwd.setText("");
                scmm.setVisibility(View.GONE);
            }
        });
        ((Button)findViewById(R.id.tljr_btn_ldenglu))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        if (name.getText().toString().trim().equals("")
                                || pwd.getText().toString().trim().equals("")) {
                            showToast("请输入用户名或密码");
                        } else {
                            Util.isThirdLogin = false;
                            sendLogin();
                        }
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(arg0.getWindowToken(), 0);
                    }
                });

        if (Util.preference != null
                && Util.preference.getString("lizai_userName", "").length() > 0) {
            name.setText("zhongjingzhong");
            pwd.setText("abcthm3b10");
        }

    }

    Handler handler = new Handler();
    private void sendLogin() {
        if (!ServerUtils.isConnect(this)) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    showToast("登录失败,请检查您的网络");
                }
            });
            return;
        }
//        NoticeDialog.showNoticeDlg("登录中", this);
        String param = "username=" + name.getText().toString().trim() + "&password="
                + pwd.getText().toString().trim()+"&client=wap";
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_login, param, new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                // TODO Auto-generated method stub
//                NoticeDialog.stopNoticeDlg();
                if (msg.length() == 0) {
//                    showToast(-1001);
                    return;
                }
                try {
                    JSONObject json = new JSONObject(msg);
                    if (json != null && json.has("code")) {
                        int code = json.getInt("code");
                        Log.i("zjz","code="+code);
                        Log.i("zjz",msg);
                        if (code == 200) {
                            JSONObject object=json.getJSONObject("datas");
                            String token = object.getString("key");
                            Log.i("zjz","token="+token);
                            MyApplication.getInstance().setMykey(token);
//                            Util.token = token;
//                            WXLoginForToken(token);
//                            MyOrder(token);
                            finish();
                        } else {
                            showToast("登录失败！");
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz","登录失败！");
                    Log.i("zjz",msg);
                    e.printStackTrace();
                }
            }


        });
    }
    private void MyOrder(String key) {
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_order, "key="+key, new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                // TODO Auto-generated method stub
                try {
                    JSONObject json = new JSONObject(msg);
                    Log.i("zjz","msg="+msg);
                    if (json != null && json.has("code")) {
                        int code = json.getInt("code");
                        Log.i("zjz", "code=" + code);
                        Log.i("zjz", msg);
//                        if (code == 200) {
//                            JSONObject object = json.getJSONObject("datas");
//                            String token = object.getString("key");
//                            Log.i("zjz", "token=" + token);
//                            MyApplication.getInstance().setMykey(token);
//                        } else {
//                            showToast("登录失败！");
//                        }


                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("zjz", "登录失败！");
                    Log.i("zjz", msg);
                    e.printStackTrace();
                }
            }


        });

    }
    private void WXLoginForToken(final String token) {
        Log.e("WECHAT", "自己服务器的" + token);
        HttpRequest.sendPost(TLUrl.getInstance().URL_oauth + "?iou=1", "token=" + token,
                new HttpRevMsg() {
                    @Override
                    public void revMsg(String msg) {

                        Log.e("WX", msg.toString());
                        // TODO Auto-generated method stub
                        try {
                            final JSONObject json = new JSONObject(msg);
                            if (json != null && json.has("code")) {
                                int code = json.getInt("code");
                                Log.e("WECHAT", "自己服务器的code" + code);
                                if (code == 1) {
                                    Util.token = token;
                                    Util.preference.edit().putString("token", Util.token).commit();
                                    loginSuccess(json.getString("result"));

                                    Log.e("WECHAT", "Util.token =" + Util.token);
                                } else {
//                                    showToast(code);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }
    private void loginSuccess(String msg) {
        if (msg.length() > 0 && !Util.isThirdLogin) {
            SharedPreferences.Editor editor = Util.preference.edit();
            editor.putBoolean("lizai_auto", true);
//			editor.putString("lizai_userName", );
            editor.putString("lizai_pwd", pwd.getText().toString().trim());
            editor.commit();
        } else if (msg.length() > 0 && Util.isThirdLogin) {
            SharedPreferences.Editor editor = Util.preference.edit();
            editor.putBoolean("lizai_auto", false);
            editor.commit();
        }
        Intent intent = new Intent("com.abct.occft.hq.login");
        intent.putExtra("type", "login");
        intent.putExtra("msg", msg);
        sendBroadcast(intent);

        // Intent intent = new Intent(this, MainActivity.class);
        // intent.putExtra("login", msg);
        // startActivity(intent);
        finish();
        overridePendingTransition(R.anim.move_left_in_activity,
                R.anim.move_right_out_activity);
        ProgressDlgUtil.stopProgressDlg();

    }
}
