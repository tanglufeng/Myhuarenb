package com.abcs.huaqiaobang.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abcs.hqbtravel.wedgt.SelectedQuHaoDialog;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.abcs.huaqiaobang.wxapi.RegisterActivity;
import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegiestActivity extends BaseActivity implements OnClickListener {
    EditText et_regiest_phone;
    Button btn_regiest_regiest;
    TextView txt_login_regiest, txt_login_coderegiest, txt_regiest_normal;
    View status;

     TextView tv_quhao; // 区号

     LinearLayout liner_quhao;// 获取区号


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.occft_activity_regiest);
        status=findViewById(R.id.status);
        liner_quhao = (LinearLayout) findViewById(R.id.liner_quhao);
        et_regiest_phone = (EditText) findViewById(R.id.tljr_et_regiest_phone);
//		et_regiest_phone.set
        btn_regiest_regiest = (Button) findViewById(R.id.tljr_btn_regiest_regiest);
        tv_quhao = (TextView) findViewById(R.id.tv_quhao);
        txt_regiest_normal = (TextView) findViewById(R.id.tljr_txt_regiest_normal);
        findViewById(R.id.tljr_img_regiest_back).setOnClickListener(this);
        btn_regiest_regiest.setOnClickListener(this);
        liner_quhao.setOnClickListener(this);
        txt_regiest_normal.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            status.setVisibility(View.VISIBLE);
        }
    }


  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==9&&resultCode==9&&data!=null){

            String quhao=data.getStringExtra("quhao");
            if(!TextUtils.isEmpty(quhao)){
                tv_quhao.setText(quhao);
            }
        }
    }*/


    SelectedQuHaoDialog quHaoDialog;
    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.tljr_img_regiest_back:// 返回
                // Intent intent = new Intent(RegiestActivity.this,
                // LoginActivity.class);
                // startActivity(intent);
                this.finish();
            case R.id.liner_quhao:
               /* Intent ui=new Intent(this,SelectQuHaoActivity.class);
                startActivityForResult(ui,9);*/
                if(quHaoDialog==null){
                    quHaoDialog=new SelectedQuHaoDialog(this);
                }

                quHaoDialog.show();

                quHaoDialog.setQuHao(new SelectedQuHaoDialog.GetQuHao() {
                    @Override
                    public void setQHao(String numbers) {

                        if(!TextUtils.isEmpty(numbers)){
                            tv_quhao.setText(numbers);
                        }

                    }
                });

                break;
            case R.id.tljr_txt_regiest_normal:// 普通注册
                Intent intent = new Intent(RegiestActivity.this,
                        RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tljr_btn_regiest_regiest:// 下一步
                sendRegiest();
                break;

            default:
                break;
        }
    }

    private boolean isValidMobile(String mobiles) {
//        Pattern p = Pattern
//                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,3,5-9]))\\d{8}$");
        Pattern p = Pattern
                .compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    private void sendRegiest() {

        String phone = et_regiest_phone.getText().toString().trim();
        if (phone.length() == 0) {
            showMessage("请输入手机号码");
            return;
        }
//        if (phone.length() == 0 || !isValidMobile(phone)) {
//            showMessage("请输入正确的手机号码");
//            return;
//        }
        handler.sendEmptyMessage(1);

        MyApplication.QU_HAO=tv_quhao.getText().toString().trim();

        isExist(URLEncoder.encode(tv_quhao.getText().toString().trim()+et_regiest_phone.getText().toString().trim()));
    }

    private void isExist(final String phone) {
        HttpRequest.sendPost(TLUrl.getInstance().URL_isexist, "uname=" + phone
                + "&platform=PHONE", new HttpRevMsg() {

            @Override
            public void revMsg(String msg) {
                // TODO Auto-generated method stub
                LogUtil.e("isExist", msg);
                try {
                    JSONObject object = new JSONObject(msg);
                    if (object.optInt("code") == 1) {
                        showError(0);
                        handler.sendEmptyMessage(2);
                    } else {
                        getKey(phone);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                    showError(1);
                }
            }
        });
    }

    private void getKey(final String phone) {

        HttpRequest.sendPost(TLUrl.getInstance().URL_sendsms, "phone=" + phone+"&company=huaqiao",
                new HttpRevMsg() {

                    @Override
                    public void revMsg(String msg) {
                        // TODO Auto-generated method stub
                        LogUtil.e("getKey", msg);
                        handler.sendEmptyMessage(2);
                        // {"code":1,"result":"556912bfe224548e3a000006"}
                        // {"code":""}
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.optInt("code") == 1) {
                                String key = object.optString("result");
                                Intent intent = new Intent(
                                        RegiestActivity.this,
                                        ConfirmCodeActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("key", key);
                                bundle.putString("phone", et_regiest_phone.getText().toString().trim());
//                                bundle.putString("phone", phone);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            } else {
                                showError(object.optInt("code"));
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            showError(0);
                        }
                    }
                });
    }

    private void showError(int code) {
        String msg = "";
        switch (code) {
            case 1:
                msg = "手机注册失败";
                break;
            case -1021:
                msg = "短信未找到";
                break;
            case -1022:
                msg = "发送短信失败";
                break;
            default:
                msg = "用户名已存在";
                break;
        }
        showMessage(msg);
    }

    private void showMessage(String msg) {
        Message message = new Message();
        message.obj = msg;
        handler.sendMessage(message);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ProgressDlgUtil.showProgressDlg("", RegiestActivity.this);
                    break;
                case 2:
                    ProgressDlgUtil.stopProgressDlg();
                    break;

                default:
                    showToast(msg.obj.toString());
                    break;
            }
        }

        ;
    };
}
