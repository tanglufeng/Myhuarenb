package com.abcs.huaqiaobang.wxapi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.chart.ChartActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.login.AutoLogin;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.Options;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.util.capturePhoto.CapturePhoto;
import com.abcs.huaqiaobang.tljr.data.Constent;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.http.client.entity.InputStreamUploadEntity;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class PersonalActivity extends BaseActivity {
    private LinearLayout ll;
    private CapturePhoto capture;
    private Handler handler = new Handler();
    private ArrayList<OnePersonal> list = new ArrayList<OnePersonal>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.occft_activity_person);
        ll = (LinearLayout) findViewById(R.id.tljr_per_ll);
        capture = new CapturePhoto(this);
        initAllTabInfo();
        initListener();
    }

    public void selectImage() {
        final CharSequence[] items = {"拍照", "从手机相册选择", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(
                PersonalActivity.this);
        builder.setTitle("修改头像");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("拍照")) {
                    // take photo from camera
                    capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE,
                            0);
                } else if (items[item].equals("从手机相册选择")) {
                    // take photo from gallery
                    capture.dispatchTakePictureIntent(CapturePhoto.PICK_IMAGE,
                            1);
                } else if (items[item].equals("取消")) {
                    // close the dialog
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        byte[] bs = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (capture.getActionCode() == CapturePhoto.PICK_IMAGE) {
                Uri targetUri = data.getData();
                if (targetUri != null)
                    bs = capture.handleMediaPhoto(targetUri);
            } else {
                bs = capture.handleCameraPhoto();
            }
            MobclickAgent.onEvent(PersonalActivity.this, "modifyAvatar");
            if (bs != null && bs.length > 0) {
                uploadAvatar(bs);
            } else {
                showToast("上传头像失败");
            }
        }

    }

    private void initListener() {
        findViewById(R.id.tljr_per_btn_lfanhui).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        finish();
                        overridePendingTransition(R.anim.move_left_in_activity,
                                R.anim.move_right_out_activity);
                    }
                });
        findViewById(R.id.tljr_btn_exit).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        MobclickAgent.onEvent(PersonalActivity.this, "Logout");
                        new AlertDialog.Builder(PersonalActivity.this)
                                .setTitle("提示")
                                .setMessage("确认退出帐号吗?")
                                .setNegativeButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                MyApplication.getInstance().self = null;
                                                Util.token = "";
                                                AutoLogin.isFirstStartApp = false;

                                                Constent.preference.edit().putString("news_p_id", "0").commit();
                                                logout("true");
                                            }
                                        }).setPositiveButton("取消", null).show();
                    }
                });
    }

    private void initAllTabInfo() {
        String[] s = {"头像", "ID", "昵称", "地区", "来源", "最后登录时间", "绑定邮箱",
                "绑定手机"};
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < s.length; i++) {
            names.add(s[i]);
        }
        if (!Util.isThirdLogin) {
            names.add("修改密码");
        }
        for (int i = 0; i < names.size(); i++) {
            OnePersonal personal = new OnePersonal(this, names.get(i),
                    getInfo(i), i);
            list.add(personal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                params.height = 160 * Util.HEIGHT / Util.IMAGEHEIGTH;
            } else {
                params.height = 90 * Util.HEIGHT / Util.IMAGEHEIGTH;
            }
            if (i % 3 == 0) {
                params.topMargin = 30 * Util.HEIGHT / Util.IMAGEHEIGTH;
            }
            ll.addView(personal.getView(), params);
        }
    }

    private String getInfo(int i) {
        try {
            switch (i) {

                case 0:
                    return MyApplication.getInstance().self.getAvatarUrl();
                case 1:
                    return MyApplication.getInstance().self.getId();
                case 2:
                    return MyApplication.getInstance().self.getNickName();
                case 3:
                    return MyApplication.getInstance().self.getArea();
                case 4:
                    return MyApplication.getInstance().self.getFrom();
                case 5:
                    return Util.getDate(MyApplication.getInstance().self.getLast()
                            .optLong("time"));
                case 6:
                    return MyApplication.getInstance().self.getEmail();
                case 7:
                    return MyApplication.getInstance().self.getPhone();
                default:
                    return "";
            }

        } catch (Exception e) {
            finish();
        }
        return "";
    }


    public void logout(String msg) {
        ProgressDlgUtil.stopProgressDlg();
        Intent intent = new Intent("com.abct.occft.hq.login");
        intent.putExtra("type", "logout");
        intent.putExtra("msg", msg);
        sendBroadcast(intent);
        // Intent intent = new Intent(this, MainActivity.class);
        // intent.putExtra("logout", msg);
        // startActivity(intent);
        finish();
        overridePendingTransition(R.anim.move_left_in_activity,
                R.anim.move_right_out_activity);
    }

    private void uploadAvatar(final byte[] bs) {
        ProgressDlgUtil.showProgressDlg("上传中", this);
        HttpRequest.sendGet(TLUrl.getInstance().URL_getAvatar, "", new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                if (msg.length() == 0) {
                    ProgressDlgUtil.stopProgressDlg();
                    showToast("上传头像失败");
                    return;
                }
                if (msg.equals("error")) {
                    ProgressDlgUtil.stopProgressDlg();
                    showToast("上传头像失败");
                } else {
                    JSONObject obj = JSONObject.parseObject(msg);
                    final String putURL = obj.getString("puturl");
                    final String avatar = obj.getString("objname");

                    final HttpUtils hu = new HttpUtils(10000);
                    final RequestParams params = new RequestParams();
                    InputStream fStream = new ByteArrayInputStream(bs);
                    hu.configTimeout(20000);
                    params.addQueryStringParameter("Content-Length", bs.length
                            + "");
                    params.setBodyEntity(new InputStreamUploadEntity(fStream,
                            bs.length));
                    params.setContentType("image/jpeg");
                    hu.configResponseTextCharset("utf-8");
                    hu.configRequestRetryCount(5);
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            hu.send(HttpMethod.PUT, putURL, params,
                                    new RequestCallBack<String>() {

                                        @Override
                                        public void onFailure(
                                                HttpException arg0, String arg1) {
                                            ProgressDlgUtil.stopProgressDlg();
                                            showToast("修改头像失败");
                                        }

                                        @Override
                                        public void onSuccess(
                                                ResponseInfo<String> arg0) {
                                            String postUrl = TLUrl.getInstance().URL_user
                                                    + "changeavatar?iou=1";
                                            String params = "token="
                                                    + Util.token + "&avatar="
                                                    + TLUrl.getInstance().URL_avatar + avatar;
                                            HttpRequest.sendPost(postUrl,
                                                    params, new HttpRevMsg() {

                                                        @Override
                                                        public void revMsg(
                                                                String msg) {
                                                            ProgressDlgUtil
                                                                    .stopProgressDlg();
                                                            if (msg.length() == 0) {
                                                                showToast("修改头像失败");
                                                            } else {
                                                                JSONObject obj = JSONObject
                                                                        .parseObject(msg);
                                                                int code = obj
                                                                        .getIntValue("code");
                                                                if (code == 1) {
                                                                    MyApplication
                                                                            .getInstance().self
                                                                            .setAvatarUrl(TLUrl.getInstance().URL_avatar
                                                                                    + avatar);
                                                                    handler.post(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            ImageView img_avatar = (ImageView) list
                                                                                    .get(0)
                                                                                    .getView()
                                                                                    .findViewById(
                                                                                            R.id.tljr_item_personal_info);
                                                                            Util.setRoundImage(
                                                                                    MyApplication
                                                                                            .getInstance().self
                                                                                            .getAvatarUrl(),
                                                                                    img_avatar,
                                                                                    handler);
                                                                            MyApplication.imageLoader
                                                                                    .displayImage(
                                                                                            MyApplication
                                                                                                    .getInstance().self
                                                                                                    .getAvatarUrl(),
                                                                                            img_avatar,
                                                                                            Options.getCircleListOptions());
//                                                                            MyApplication
//                                                                                    .getInstance()
//                                                                                    .getMainActivity().main
//                                                                                    .initUser();
                                                                        }
                                                                    });

                                                                    showToast("修改头像成功");
                                                                    MyApplication myApplication = MyApplication
                                                                            .getInstance();
                                                                    ChartActivity
                                                                            .getYunZhiToken(
                                                                                    myApplication.self
                                                                                            .getId(),
                                                                                    myApplication.self
                                                                                            .getNickName(),
                                                                                    null,
                                                                                    myApplication.self
                                                                                            .getAvatarUrl());
                                                                } else {
                                                                    showToast("修改头像失败");
                                                                }
                                                            }
                                                        }
                                                    });

                                        }
                                    });
                        }
                    });

                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event != null
                && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (event.getRepeatCount() == 5) {
                Log.i("onKeyDown", "" + event.getRepeatCount());
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
