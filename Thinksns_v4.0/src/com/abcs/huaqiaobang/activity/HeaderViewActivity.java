package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.chart.ChartActivity;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.util.capturePhoto.CapturePhoto;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.http.client.entity.InputStreamUploadEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/1/22.
 */
public class HeaderViewActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.take_photo)
    TextView takePhoto;
    @InjectView(R.id.picture_header)
    TextView pictureHeader;
    @InjectView(R.id.default_header)
    TextView defaultHeader;
    private CapturePhoto capture;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_modifyheaderview);
        ButterKnife.inject(this);

        capture = new CapturePhoto(this);

        Log.i("LOG_DEBUG", " header  onCreate()");
        takePhoto.setOnClickListener(this);
        pictureHeader.setOnClickListener(this);
        defaultHeader.setOnClickListener(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("LOG_DEBUG", " header  onSaveInstanceState()");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.default_header:

//                MyApplication.getInstance().getMainActivity().my.refreshImgHeader(1);
//                MyApplication.getInstance().getMainActivity().main.refreshImgHeader(1);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                        bitmap,
                        120,
                        120,
                        true);
                bitmap.recycle();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
                byte[] b = baos.toByteArray();
                uploadAvatar(b);
                break;
            case R.id.picture_header:
                capture.dispatchTakePictureIntent(CapturePhoto.PICK_IMAGE,
                        0);

                break;
            case R.id.take_photo:
                ImageLoader.getInstance().clearMemoryCache();
                ImageLoader.getInstance().clearDiscCache();
                System.gc();
                capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE,
                        1);

                break;
        }

    }

    private void uploadAvatar(final byte[] bs) {
        ProgressDlgUtil.showProgressDlg("上传中", this);

        HttpRequest.sendGet(TLUrl.getInstance().URL_getAvatar, "", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (msg.length() == 0) {
                            ProgressDlgUtil.stopProgressDlg();
                            showToast("上传头像失败");
                            return;
                        }
                    }
                });

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
//                                                                            MyApplication.getInstance().getMainActivity().my.refreshImgHeader();
//                                                                            MyApplication.getInstance().getMainActivity().main.refreshImgHeader();
                                                                            setResult(1);
                                                                            showToast("修改头像成功");
                                                                            Util.preference.edit().putString("avatar", "").commit();
                                                                            finish();
                                                                        }
                                                                    });

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
            if (bs != null && bs.length > 0) {
                try {
                    uploadAvatar(bs);
                } catch (Exception e) {

                }
            } else {
                showToast("上传头像失败，无法找到该照片");
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LOG_DEBUG", " header  onDestroy()");
    }
}
