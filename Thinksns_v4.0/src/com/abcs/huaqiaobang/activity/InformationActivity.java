package com.abcs.huaqiaobang.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.model.StatusBarCompat;
import com.abcs.huaqiaobang.presenter.MyPrsenter;
import com.abcs.huaqiaobang.view.CircleImageView;
import com.abcs.huaqiaobang.ytbt.emotion.DisplayUtils;
import com.abcs.huaqiaobang.ytbt.settings.QRCodeUtil;
import com.abcs.huaqiaobang.ytbt.util.Tool;
import com.abcs.sociax.android.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class InformationActivity extends BaseActivity {

    @InjectView(R.id.rl_left)
    RelativeLayout rlLeft;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.rl_right)
    RelativeLayout rlRight;
    @InjectView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @InjectView(R.id.tv_nickname)
    TextView tvNickname;

    @InjectView(R.id.tv_level)
    TextView tv_level;
    @InjectView(R.id.tv_sex)
    TextView tvSex;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_information_hqb);

        ButterKnife.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Translucent status bar
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            setTranslucentStatus(true);
            StatusBarCompat.compat(this, Color.parseColor("#ffffff"));
        }
        title.setText("个人信息");
        ImageLoader.getInstance().displayImage(MyApplication.getInstance().self.getAvatarUrl(), imgAvatar);
        tvNickname.setText(MyApplication.getInstance().self.getNickName());
        modifySex(MyApplication.getInstance().self.getSex());

        initLevel(MyApplication.getLeveId());
    }

    @OnClick({R.id.rl_core,R.id.rl_huiyuan, R.id.rl_sex, R.id.rl_nickname, R.id.rl_avatar, R.id.rl_left})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rl_huiyuan:  // 会员等级
                Intent intent = new Intent(this, YuanHuiCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_core:
//                showQRCode();
                break;
            case R.id.rl_left:
                finish();
                break;
            case R.id.rl_sex:
                startActivityForResult(new Intent(this, SexSelectAcitivity.class), 2);
                break;
            case R.id.rl_nickname:
                startActivityForResult(new Intent(this, NickNameActivity.class), Activity.RESULT_FIRST_USER);
                break;
            case R.id.rl_avatar:
                startActivityForResult(new Intent(this, HeaderViewActivity.class), 3);
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showQRCode() {
        Tool.showProgressDialog(this, "正在加载", true);
        final ImageView iv = new ImageView(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(iv);
        final String filePath = getFileRoot(this) + File.separator + "qr_"
                + MyApplication.getInstance().getUid() + ".jpg";
        String path = getFileRoot(this) + File.separator + "avator_"
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
                    int width = DisplayUtils.getScreenWidthPixels(InformationActivity.this);

                    @Override
                    public void run() {
                        boolean success = QRCodeUtil.createQRImage("{\"nickname\":\"" + MyApplication.getInstance().getOwnernickname() + "\",\"uid\":\""
                                        + MyApplication.getInstance().getUid() + "\",\"avatar\":\"" + MyApplication.getInstance().getAvater() + "\"}",
                                width / 3 * 2, width / 3 * 2, bitmap, filePath);
                        if (success) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv.setImageBitmap(BitmapFactory
                                            .decodeFile(filePath));
                                    Tool.removeProgressDialog();
                                    builder.show();
                                    if (bitmap != null && bitmap.isRecycled())
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

    public String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            tvNickname.setText(data.getStringExtra("name"));
        }
        if (data != null && requestCode == 2) {

            modifySex(data.getStringExtra("sex"));
            MyApplication.getInstance().self.setSex(data.getStringExtra("sex"));
        } else if (requestCode == 3 && resultCode == 1) {
            ImageLoader.getInstance().displayImage(MyApplication.getInstance().self.getAvatarUrl(), imgAvatar);
        }
        new MyPrsenter(MyApplication.getInstance().getMainActivity().my).loginSuccess();
    }



    private void modifySex(String sex) {

        if ("0".equals(sex)) {
            tvSex.setText("女");
        } else {
            tvSex.setText("男");
        }
    }

    private void initLevel(String levelId){

        if(!TextUtils.isEmpty(levelId)){
            if(levelId.equals("1")){
                tv_level.setText("普卡会员");
            }else  if(levelId.equals("2")){
                tv_level.setText("金卡会员");
            }else  if(levelId.equals("3")){
                tv_level.setText("白金卡会员");
            }else  if(levelId.equals("4")){
                tv_level.setText("黑卡会员");
            }
        }
    }
}
