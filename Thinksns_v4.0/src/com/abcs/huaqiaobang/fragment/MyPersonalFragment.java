package com.abcs.huaqiaobang.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.BindPhoneActivity;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.activity.HeaderViewActivity;
import com.abcs.huaqiaobang.activity.ModifyPasswordActivity;
import com.abcs.huaqiaobang.activity.NickNameActivity;
import com.abcs.huaqiaobang.activity.SexSelectAcitivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.ytbt.emotion.DisplayUtils;
import com.abcs.huaqiaobang.ytbt.settings.QRCodeUtil;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/1/21.
 */
public class MyPersonalFragment extends Fragment implements View.OnClickListener {


    @InjectView(R.id.my_rl_nickname)
    RelativeLayout myRlNickname;
    @InjectView(R.id.my_rl_sex)
    RelativeLayout myRlSex;
    @InjectView(R.id.personal_img_modifheader)
    ImageView personalImgModifheader;
    @InjectView(R.id.my_rl_modifheader)
    RelativeLayout myRlModifheader;
    @InjectView(R.id.personal_tv_vArea)
    TextView personalTvVArea;
    @InjectView(R.id.personal_tv_vSource)
    TextView personalTvVSource;
    @InjectView(R.id.personal_tv_vLoginTime)
    TextView personalTvVLoginTime;
    @InjectView(R.id.my_rl_modifyPassword)
    RelativeLayout myRlModifyPassword;
    @InjectView(R.id.my_rl_hongbao)
    RelativeLayout myRlHongbao;
    @InjectView(R.id.my_rl_jifen)
    RelativeLayout myRlJifen;
    @InjectView(R.id.my_rl_order)
    RelativeLayout myRlOrder;
    @InjectView(R.id.my_rl_huaqiaojin)
    RelativeLayout myRlHuaqiaojin;
    @InjectView(R.id.personal_btn_exit)
    Button personalBtnExit;
    @InjectView(R.id.personal_tv_vNickname)
    TextView personalTvVNickname;
    @InjectView(R.id.personal_tv_vSex)
    TextView personalTvVSex;
    @InjectView(R.id.my_rl_core)
    RelativeLayout myRlCore;
    @InjectView(R.id.personal_tv_vEmail)
    TextView personalTvVEmail;
    @InjectView(R.id.my_rl_Email)
    RelativeLayout myRlEmail;
    @InjectView(R.id.personal_tv_vPhone)
    TextView personalTvVPhone;
    @InjectView(R.id.my_rl_Phone)
    RelativeLayout myRlPhone;
    private View view;
    private MyApplication application;

    private Activity context;

    boolean isBindEmail;
    boolean isBindPhone;
    boolean isBindSuccess;
    String email;
    String phone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_my_persona_item, null);


        }
        ButterKnife.inject(this, view);

        context = getActivity();
        init();
        initUser();
        loadData();
        return view;
    }

    private void initUser() {
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_member, "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg=" + msg);
                                JSONObject data = object.getJSONObject("datas");
                                if (data.optString("member_email_bind").equals("1")) {
                                    email = data.optString("member_email");
                                    isBindEmail = true;
                                } else if (!data.optString("member_email").equals("null")) {
                                    email = data.optString("member_email");
                                    isBindSuccess = true;
                                } else {
                                    isBindSuccess = false;
                                    isBindEmail = false;
                                }
                                if (data.optString("member_mobile_bind").equals("1")) {
                                    phone = data.optString("member_mobile");
                                    isBindPhone = true;
                                } else {
                                    isBindPhone = false;
                                }
                                initView();
                                ProgressDlgUtil.stopProgressDlg();
                            } else {
                                ProgressDlgUtil.stopProgressDlg();
                                Log.i("zjz", "goodsDetail:解析失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }

    private void initView() {
        if (isBindEmail) {
            personalTvVEmail.setText(email);
        } else if (isBindSuccess) {
            personalTvVEmail.setText("请去" + email + "进行验证");
            personalTvVEmail.setTextSize(12);
        } else {
            personalTvVEmail.setText("请绑定邮箱");
        }
        personalTvVPhone.setText(isBindPhone ? phone : "请绑定手机号");
    }

    private void init() {

        myRlNickname.setOnClickListener(this);
        myRlModifheader.setOnClickListener(this);
        myRlSex.setOnClickListener(this);
        myRlHongbao.setOnClickListener(this);
        myRlJifen.setOnClickListener(this);
        myRlOrder.setOnClickListener(this);
        myRlHuaqiaojin.setOnClickListener(this);
        myRlModifyPassword.setOnClickListener(this);
        personalBtnExit.setOnClickListener(this);
        myRlCore.setOnClickListener(this);
        myRlPhone.setOnClickListener(this);
        myRlEmail.setOnClickListener(this);
        IntentFilter filter = new IntentFilter("com.abcs.huaqiaobang.changeuser");
        filter.addAction("com.abcs.huaqiaobang.mypersonallocation");
        context.registerReceiver(breceiver, filter);


    }

    public void loadImage(Bitmap bitmap) {


//        if (bitmap != null && !bitmap.isRecycled()) {
            personalImgModifheader.setImageBitmap(bitmap);
//            bitmap.recycle();
//        }
//        application = MyApplication.getInstance();
//        personalTvVArea.setText(application.self.getArea());
//        personalTvVLoginTime.setText(Util.getDate(application.self.getLast()
//                .optLong("time")));
//        personalTvVSource.setText(application.self.getFrom());
//        if ("1".equals(application.self.getSex())) {
//
//        }
    }

    public void loadData() {
        application = MyApplication.getInstance();
        if (!Util.preference.getString("logintype", "").equals("")) {
            myRlModifyPassword.setVisibility(View.GONE);
            myRlNickname.setClickable(false);
            myRlSex.setClickable(false);
            myRlModifheader.setClickable(false);

        } else {
            myRlModifyPassword.setVisibility(View.VISIBLE);
            myRlNickname.setClickable(true);
            myRlSex.setClickable(true);
            myRlModifheader.setClickable(true);
        }
//        application.self.setArea(application.location);
//        personalTvVArea.setText(application.self.getArea());
        personalTvVLoginTime.setText(Util.getDate(application.self.getLast()
                .optLong("time")));
        personalTvVSource.setText(application.self.getFrom());

        personalTvVNickname.setText(MyApplication.getInstance().self.getNickName());


        modifySex(MyApplication.getInstance().self.getSex());
    }

    private void modifySex(String sex) {

        if ("0".equals(sex)) {
            personalTvVSex.setText("女");
        } else {
            personalTvVSex.setText("男");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        ButterKnife.reset(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(breceiver);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.my_rl_modifheader:
                startActivity(new Intent(getActivity(), HeaderViewActivity.class));

                break;
            case R.id.my_rl_sex:

                startActivityForResult(new Intent(getActivity(), SexSelectAcitivity.class), 2);
                break;
            case R.id.my_rl_nickname:

                startActivityForResult(new Intent(getActivity(), NickNameActivity.class), Activity.RESULT_FIRST_USER);
                break;

            case R.id.my_rl_modifyPassword:

                startActivity(new Intent(getActivity(), ModifyPasswordActivity.class));
                break;

            case R.id.personal_btn_exit:
                MyApplication.getInstance().getMainActivity().logout("true");
                showToast("退出成功");
                break;
            case R.id.my_rl_Email:
                intent.setClass(getActivity(), BindPhoneActivity.class);
                intent.putExtra("type", BindPhoneActivity.BINDEMAIL);
                if (isBindEmail) {

                    intent.putExtra("title", "修改绑定邮箱");
                    intent.putExtra("isFirst", true);
                } else {
                    intent.putExtra("title", "绑定邮箱");
                    intent.putExtra("isFirst", false);
                }
                startActivityForResult(intent, 4);
                break;
            case R.id.my_rl_Phone:
                intent.setClass(getActivity(), BindPhoneActivity.class);
                if (isBindPhone) {
                    intent.putExtra("isFirst", true);
                    intent.putExtra("title", "修改绑定手机号");
                } else {
                    intent.putExtra("isFirst", false);
                    intent.putExtra("title", "绑定手机号");
                }
                intent.putExtra("type", BindPhoneActivity.BINDPHONE);
                startActivityForResult(intent, 3);
//                bindPhone();
                break;
            case R.id.my_rl_core:
                showQRCode();
                break;

        }

    }

    private void showQRCode() {
        Tool.showProgressDialog(getContext(), "正在加载", true);
        final ImageView iv = new ImageView(getContext());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(iv);
        final String filePath = getFileRoot(getContext()) + File.separator + "qr_"
                + MyApplication.getInstance().getUid() + ".jpg";
        String path = getFileRoot(getContext()) + File.separator + "avator_"
                + MyApplication.getInstance().getUid() + ".jpg";
        String avater = MyApplication.getInstance().self.getAvatarUrl();
        new HttpUtils().download(avater, path, new RequestCallBack<File>() {
            private Bitmap bitmap;

            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                bitmap = BitmapFactory
                        .decodeFile(arg0.result.getAbsolutePath());
                creatQR(iv, builder, filePath);
            }

            private void creatQR(final ImageView iv,
                                 final AlertDialog.Builder builder, final String filePath) {
                new Thread(new Runnable() {
                    int width = DisplayUtils.getScreenWidthPixels(getActivity());

                    @Override
                    public void run() {
                        boolean success = QRCodeUtil.createQRImage("{\"nickname\":\"" + MyApplication.getInstance().getOwnernickname() + "\",\"uid\":\""
                                        + MyApplication.getInstance().getUid() + "\",\"avatar\":\"" + MyApplication.getInstance().getAvater() + "\"}",
                                width / 3 * 2, width / 3 * 2, bitmap, filePath);
                        if (success) {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv.setImageBitmap(BitmapFactory
                                            .decodeFile(filePath));
                                    Tool.removeProgressDialog();
                                    builder.show();
                                    if(bitmap!=null&&bitmap.isRecycled())
                                    bitmap.recycle();
                                }
                            });
                        }
                    }
                }).start();
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                creatQR(iv, builder, filePath);
            }
        });
    }

    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }


    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


    private void modifyNickName(final String name) {


        String geturl = TLUrl.getInstance().URL_user + "changenickname?iou=1";
        String param = "token=" + Util.token + "&nickname="
                + name;
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
                                if (MyApplication.getInstance()
                                        .getMainActivity().main != null) {
//                                    MyApplication.getInstance()
//                                            .getMainActivity().main
//                                            .modifyNickName();
//                                    MyApplication.getInstance().getMainActivity().my.refreshNickName();
//                                    MyApplication.getInstance().getMainActivity().main.refreshNickName();
                                    personalTvVNickname.setText(name);
                                }
                                showToast("修改成功");
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == 1) {
            modifyNickName(data.getStringExtra("name"));
        }
        if (data != null && requestCode == 2) {

            modifySex(data.getStringExtra("sex"));
            MyApplication.getInstance().self.setSex(data.getStringExtra("sex"));
        }
        if (data != null && requestCode == 3) {
            personalTvVPhone.setText(data.getStringExtra("bind_phone"));
        }
        if (data != null && requestCode == 4) {
            initUser();
//            personalTvVEmail.setText(data.getStringExtra("bind_email"));
        }
    }

    private Handler handler = new Handler();

    public void refreshImgHeader(int i) {
        personalImgModifheader.setImageResource(R.drawable.img_avatar);

    }

    BroadcastReceiver breceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() == "com.abcs.huaqiaobang.changeuser") {
                loadData();
                initUser();

            } else if (intent.getAction() == "com.abcs.huaqiaobang.mypersonallocation") {
                personalTvVArea.setText(intent.getStringExtra("location"));
                MyApplication.getInstance().getMainActivity().mLocationClient.stop();
            }
        }
    };
}
