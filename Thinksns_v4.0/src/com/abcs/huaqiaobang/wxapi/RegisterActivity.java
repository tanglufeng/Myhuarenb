package com.abcs.huaqiaobang.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.abcs.haiwaigou.utils.ACache;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.db.UserSqlHelper;
import com.abcs.sociax.t4.android.ActivityHome;
import com.abcs.sociax.t4.android.Thinksns;
import com.abcs.sociax.t4.model.ModelUser;
import com.thinksns.sociax.thinksnsbase.exception.DataInvalidException;
import com.thinksns.sociax.thinksnsbase.network.ApiHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends BaseActivity {
    private View mMainView;
    private boolean isChooseJiGou;
    private Button btn_reg;
    private RadioButton r_geRen, r_jiGou;
    private EditText et_jiGou, et_name, et_pwd, et_rePwd;
    private static final int NEEDNAME = -2001;
    private static final int NEEDPWD = -2002;
    private static final int NEEDREPWD = -2003;
    private static final int TWOPWD = -2004;
    private static final int NEEDMOBILE = -2005;
    private static final int NEEDJIGOU = -2006;
    private static final int REGERROR = -2007;
    private static final int NAMELENGTH = -2008;
    private static final int PWDLENGTH = -2009;
    private ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainView = getLayoutInflater().inflate(
                R.layout.occft_activity_register, null);
        this.setContentView(mMainView);
        aCache = ACache.get(this);
        r_geRen = (RadioButton) mMainView
                .findViewById(R.id.tljr_reg_rbtn_lgeren);
        r_jiGou = (RadioButton) mMainView
                .findViewById(R.id.tljr_reg_rbtn_ljigou);
        et_jiGou = (EditText) mMainView.findViewById(R.id.tljr_reg_et_jigou);
        et_name = (EditText) mMainView.findViewById(R.id.tljr_reg_et_lname);
        et_pwd = (EditText) mMainView.findViewById(R.id.tljr_reg_et_lpwd);
        et_rePwd = (EditText) mMainView.findViewById(R.id.tljr_reg_et_repwd);
        btn_reg = (Button) mMainView.findViewById(R.id.tljr_reg_btn_zhuce);
        initListener();

    }


    private void initListener() {
        r_geRen.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isChooseJiGou = false;
                et_jiGou.setVisibility(View.GONE);
            }
        });

        r_jiGou.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isChooseJiGou = true;
                et_jiGou.setVisibility(View.VISIBLE);
            }
        });

        mMainView.findViewById(R.id.tljr_reg_btn_lfanhui).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(getCurrentFocus(), InputMethodManager.SHOW_FORCED);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        RegisterActivity.this.finish();
                        // overridePendingTransition(R.anim.move_right_in_activity,
                        // R.anim.move_right_out_activity);
                    }
                });
        btn_reg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String name = et_name.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();
                String rePwd = et_rePwd.getText().toString().trim();
                String jiGou = et_jiGou.getText().toString().trim();
                if (name.length() == 0) {
                    showToast(NEEDNAME);
                    et_name.requestFocus();
                    return;
                }
                if (pwd.length() == 0) {
                    et_pwd.requestFocus();
                    showToast(NEEDPWD);
                    return;
                }
                if (rePwd.length() == 0) {
                    showToast(NEEDREPWD);
                    et_rePwd.requestFocus();
                    return;
                }

                if (name.length() < 6) {
                    showToast(NAMELENGTH);
                    et_name.requestFocus();
                    return;
                }
                if (pwd.length() < 6 || pwd.length() > 12) {
                    showToast(PWDLENGTH);
                    et_pwd.requestFocus();
                    return;
                }

                if (!Util.checkPassword(et_pwd.getText().toString().trim())) {
//                    Toast.makeText(RegisterActivity.this, "密码过于简单", Toast.LENGTH_SHORT).show();
                    showToast("密码过于简单");
                    et_pwd.requestFocus();
                    return;
                }

                if (!pwd.equals(rePwd)) {
                    showToast(TWOPWD);
                    et_rePwd.requestFocus();
                    return;
                }
                // if (!isMobile(name)) {
                // showToast(NEEDMOBILE);
                // et_name.requestFocus();
                // return;
                // }

                if (isChooseJiGou) {
                    if (jiGou.length() == 0) {
                        showToast(NEEDJIGOU);
                        et_jiGou.requestFocus();
                        return;
                    }
                }
                Util.isThirdLogin = false;
                sendRegist();
            }
        });
    }

    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    private void sendRegist() {

        ProgressDlgUtil.showProgressDlg("注册中...", RegisterActivity.this);
        String param = "uname=" + et_name.getText().toString().trim()
                + "&upass=" + et_pwd.getText().toString().trim() + "&repass="
                + et_rePwd.getText().toString().trim() + "&from=华侨帮";
        if (isChooseJiGou) {
            param += "&organization=" + et_jiGou.getText().toString().trim();
        }
        HttpRequest.sendPost(TLUrl.getInstance().URL_regist, param, new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDlgUtil.stopProgressDlg();
                        if (msg.length() == 0) {
                            showToast(REGERROR);
                            return;
                        }
                        try {
                            JSONObject json = new JSONObject(msg);
                            if (json != null && json.has("code")) {
                                int code = json.getInt("code");
                                if (code == 1) {
                                    showToast("注册成功");
                                    String token = json.getString("result");
                                    Util.token = token;
                                    loginForToken(token);
                                } else {
                                    showToast(code);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }

    private void loginForToken(final String token) {
        ProgressDlgUtil.showProgressDlg("登录中...", RegisterActivity.this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_oauth + "?iou=1", "token=" + token,
                new HttpRevMsg() {
                    @Override
                    public void revMsg(String msg) {

                        Log.e("WX", msg.toString());
                        // TODO Auto-generated method stub
                        try {
                            final JSONObject json = new JSONObject(msg);
                            Log.i("zjz", "login_msg=" + msg);
                            if (json != null && json.has("code")) {
                                int code = json.getInt("code");
                                Log.e("WECHAT", "自己服务器的code" + code);
                                if (code == 1) {
                                    Util.token = token;
                                    Util.preference.edit().putString("token", Util.token).commit();

                                    JSONObject obj = new JSONObject(json.getString("result"));


                                    JSONObject sns = obj.getJSONObject("sns");

                                    if (!sns.has("uid")) {
                                        showToast("登录失败,请检查您的网络");
                                        return;
                                    }


                                    ModelUser newUser = new ModelUser(sns);
                                    newUser.setFace(obj.optString("avatar"));
                                    newUser.setPostUface(obj.optString("avatar"));

                                    LogUtil.i("haologin", "avator: " + newUser.getFace());

                                    String oauth_token = sns.getString("oauth_token");
                                    String oauth_token_secret = sns.getString("oauth_token_secret");
                                    ApiHttpClient.TOKEN = oauth_token;
                                    ApiHttpClient.TOKEN_SECRET = oauth_token_secret;
                                    newUser.setToken(oauth_token);
                                    newUser.setSecretToken(oauth_token_secret);

                                    Thinksns.setMy(newUser);
                                    //添加用户信息至数据库
                                    UserSqlHelper db = UserSqlHelper.getInstance(RegisterActivity.this);
                                    db.addUser(newUser, true);
                                    String username = newUser.getUserName();
                                    if (!db.hasUname(username))
                                        db.addSiteUser(username);


                                    loginSuccess(json.getString("result"));

                                    Log.e("WECHAT", "Util.token =" + Util.token);
                                } else {
                                    showToast(code);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (DataInvalidException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void loginSuccess(String msg) {
        // Intent intent = new Intent(this, MainActivity.class);
        // intent.putExtra("login", msg);
        // startActivity(intent);
        if (msg.length() > 0) {
            Editor editor = Util.preference.edit();
            editor.putBoolean("lizai_auto", true);
            editor.putString("lizai_pwd", et_pwd.getText().toString().trim());
            if (aCache.getAsString(TLUrl.getInstance().LOGINTOKEN) != null)
                aCache.remove(TLUrl.getInstance().LOGINTOKEN);
            aCache.put(TLUrl.getInstance().LOGINTOKEN, Util.token, 5 * 24 * 60 * 60);
            editor.commit();
        }
        ProgressDlgUtil.stopProgressDlg();
        Intent it = new Intent(this, ActivityHome.class);
        it.putExtra("msg", msg);
        startActivity(it);
        overridePendingTransition(R.anim.move_left_in_activity,
                R.anim.move_right_out_activity);
        Activity activity = MyApplication.getInstance().activityMap.get("com.abcs.huaqiaobang.wxapi.WXEntryActivity");
        if (activity != null) {
            activity.finish();
        }
        finish();


    }

    private void showToast(int code) {
        String msg = "";
        switch (code) {
            case -1:
                msg = "参数非法";
                break;
            case -1000:
                msg = "用户不存在";
                break;
            case -1001:
                msg = "登录失败";
                break;
            case -1002:
                msg = "帐号停权";
                break;
            case -1003:
                msg = "用户详情未找到";
                break;
            case -1004:
                msg = "密码不一样";
                break;
            case -1005:
                msg = "注册失败";
                break;
            case -1006:
                msg = "用户已存在";
                break;
            case -1007:
                msg = "微信配置文件未找到";
                break;
            case -1008:
                msg = "非法的Code";
                break;
            case -1009:
                msg = "请重新登录";// "非法的token"
                break;
            case -2001:
                msg = "请输入用户名";
                break;
            case -2002:
                msg = "请输入密码";
                break;
            case -2003:
                msg = "请输入确认密码";
                break;
            case -2004:
                msg = "两次密码不一致";
                break;
            case -2005:
                msg = "请输入正确的手机号码";
                break;
            case -2006:
                msg = "请输入机构名称";
                break;
            case -2007:
                msg = "注册失败";
                break;
            case -2008:
                msg = "用户名长度不少于6位";
                break;
            case -2009:
                msg = "密码长度6-12位";
                break;
            default:
                break;
        }
        final String tishi = msg;
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                showToast(tishi);
            }
        });
    }

    Handler handler = new Handler();
}
