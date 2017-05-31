package com.abcs.huaqiaobang.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.activity.AddressActivity;
import com.abcs.haiwaigou.activity.BindPhoneActivity;
import com.abcs.haiwaigou.activity.HuanqiuActivity;
import com.abcs.haiwaigou.broadcast.MyBroadCastReceiver;
import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.utils.DataCleanManager;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.AboutWeActivity;
import com.abcs.huaqiaobang.activity.FeedbackActivity;
import com.abcs.huaqiaobang.activity.InformationActivity;
import com.abcs.huaqiaobang.activity.ModifyPasswordActivity;
import com.abcs.huaqiaobang.dialog.NoticeDialog;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.dialog.PromptDialog;
import com.abcs.huaqiaobang.presenter.UserDataInterface;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.weight.Info;
import com.abcs.huaqiaobang.wxapi.ShareActivity;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.abcs.sociax.t4.android.ActivityHome;
import com.abcs.sociax.t4.android.activity.MessageActivity;
import com.abcs.sociax.t4.sharesdk.ShareSDKManager;
import com.thinksns.sociax.thinksnsbase.utils.ActivityStack;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.thinksns.tschat.chat.TSChatManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Administrator on 2016/5/24 0024.
 */
public class UserMoreFragment extends Fragment implements UserDataInterface {

    @InjectView(R.id.rl_message)
    RelativeLayout rlMessage;
    @InjectView(R.id.rl_cewansu)
    RelativeLayout rl_cewansu;


    @InjectView(R.id.img_information)
    ImageView imgInformation;
    @InjectView(R.id.rl_information)
    RelativeLayout rlInformation;
    @InjectView(R.id.img_paypwd)
    ImageView imgPaypwd;
    @InjectView(R.id.tv_paypwd)
    TextView tvPaypwd;
    @InjectView(R.id.rl_modifyPayPwd)
    RelativeLayout rlModifyPayPwd;
    @InjectView(R.id.img_pwd)
    ImageView imgPwd;
    @InjectView(R.id.tv_pwd)
    TextView tvPwd;
    @InjectView(R.id.tv_cewansu)
    TextView tv_cewansu;
    @InjectView(R.id.rl_modifyPwd)
    RelativeLayout rlModifyPwd;
    @InjectView(R.id.img_bindPhone)
    ImageView imgBindPhone;
    @InjectView(R.id.tv_Phone)
    TextView tvPhone;
    @InjectView(R.id.rl_bindPhone)
    RelativeLayout rlBindPhone;
    @InjectView(R.id.img_bindEmail)
    ImageView imgBindEmail;
    @InjectView(R.id.tv_Email)
    TextView tvEmail;
    @InjectView(R.id.rl_bindEmail)
    RelativeLayout rlBindEmail;
    @InjectView(R.id.img_address)
    ImageView imgAddress;
    @InjectView(R.id.rl_address)
    RelativeLayout rlAddress;
    @InjectView(R.id.img_clean)
    ImageView imgClean;
    @InjectView(R.id.tv_cache)
    TextView tvCache;
    @InjectView(R.id.img_arrow)
    ImageView imgArrow;
    @InjectView(R.id.rl_clean)
    RelativeLayout rlClean;
    @InjectView(R.id.linear1)
    LinearLayout linear1;
    @InjectView(R.id.img_about)
    ImageView imgAbout;
    @InjectView(R.id.rl_about)
    RelativeLayout rlAbout;
    @InjectView(R.id.img_feedback)
    ImageView imgFeedback;
    @InjectView(R.id.rl_feedback)
    RelativeLayout rlFeedback;
    @InjectView(R.id.img_share)
    ImageView imgShare;
    @InjectView(R.id.rl_share)
    RelativeLayout rlShare;
    @InjectView(R.id.img_update)
    ImageView imgUpdate;
    @InjectView(R.id.t_version)
    TextView tVersion;
    @InjectView(R.id.img_update_more)
    ImageView imgUpdateMore;
    @InjectView(R.id.rl_update)
    RelativeLayout rlUpdate;
    @InjectView(R.id.btn_exit)
    Button btnExit;
    private View view;
    Activity activity;

    private String paypwd;
    boolean isBindEmail;
    boolean isBindPhone;
    boolean isBindSuccess;
    String email;
    String phone;
    boolean isPayPwd = false;

    MyBroadCastReceiver myBroadCastReceiver;

    private Info info;
    private byte[] imageBytes;
    private boolean flag;
    private int last_degree=0,cur_degree;


    private Handler handler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO Auto-generated method stub
            if(msg.what==0x123)
            {
                ProgressDlgUtil.stopProgressDlg();
//                tv_now_speed.setText(msg.arg1+"KB/S");
                tv_cewansu.setText(networkInfo.getTypeName()+"  "+msg.arg2+"KB/S");
//                startAnimation(msg.arg1);
            }
            if(msg.what==0x100)
            {
//                tv_now_speed.setText("0KB/S");
//                startAnimation(0);
//                btn.setText("开始测试");
                rl_cewansu.setEnabled(true);
            }
        }

    };


    public static UserMoreFragment newInstance() {
        UserMoreFragment userMoreFragment = new UserMoreFragment();
        return userMoreFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.hwg_fragment_user_more, null);
        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);
        ButterKnife.inject(this, view);
        myBroadCastReceiver = new MyBroadCastReceiver(activity, updateUI);
        myBroadCastReceiver.register();
        info=new Info();
        initData();
        return view;
    }

    MyBroadCastReceiver.UpdateUI updateUI = new MyBroadCastReceiver.UpdateUI() {
        @Override
        public void updateShopCar(Intent intent) {

        }

        @Override
        public void updateCarNum(Intent intent) {

        }

        @Override
        public void updateCollection(Intent intent) {
            if (intent.getStringExtra("type").equals(MyUpdateUI.CHANGEUSER)) {
                initData();
                Log.i("zjz", "UserFragment更换用户");
            } else if (intent.getStringExtra("type").equals(MyUpdateUI.MYORDERNUM)) {
                initData();
            }
        }

        @Override
        public void update(Intent intent) {

        }
    };

    private void initData() {
        try {
            tvCache.setText(DataCleanManager.getTotalCacheSize(activity));
            Log.i("zjz", "缓存=" + DataCleanManager.getTotalCacheSize(activity));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (MyApplication.getInstance().self == null) {
            btnExit.setVisibility(View.GONE);
        } else {
            initUser();
            btnExit.setVisibility(View.VISIBLE);
        }

        if (MyApplication.getInstance().getMykey() == null) {
            rlInformation.setVisibility(View.GONE);
            rlModifyPwd.setVisibility(View.GONE);
            rlBindPhone.setVisibility(View.GONE);
            rlBindEmail.setVisibility(View.GONE);
            rlAddress.setVisibility(View.GONE);
            rlModifyPayPwd.setVisibility(View.GONE);
        } else {
            rlInformation.setVisibility(View.VISIBLE);
            rlModifyPwd.setVisibility(View.VISIBLE);
            rlBindPhone.setVisibility(View.VISIBLE);
            rlBindEmail.setVisibility(View.VISIBLE);
            rlAddress.setVisibility(View.VISIBLE);
            rlModifyPayPwd.setVisibility(View.VISIBLE);
            if (Util.isThirdLogin) {
                rlModifyPayPwd.setVisibility(View.GONE);
                rlInformation.setVisibility(View.GONE);
            } else {
                rlModifyPayPwd.setVisibility(View.VISIBLE);
                rlInformation.setVisibility(View.VISIBLE);
            }
        }



        tVersion.setText(getResources().getString(R.string.current_version) + getVersion());
        IntentFilter filter = new IntentFilter("com.abcs.huaqiaobang.changeuser");
        activity.registerReceiver(receiver, filter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() == "com.abcs.huaqiaobang.changeuser") {
                initUser();
            }
        }
    };

    public String getVersion() {
        try {
            PackageManager manager = activity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
            String version = info.versionName;
            return "V" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return this.getString(R.string.can_not_find_version_name);
        }
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

                                paypwd = data.optString("member_paypwd");
                                Log.i("zjz", "pwd=" + paypwd);
                                if (tvPwd != null)
                                    tvPwd.setText(paypwd.equals("null") ? "设置支付密码" : "修改支付密码");
                                Log.i("zjz", "isTrue=" + (paypwd.equals("null")));
                                if (paypwd.equals("null")) {
                                    isPayPwd = false;
                                } else {
                                    isPayPwd = true;
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

        if (MyApplication.getInstance().self != null && MyApplication.getInstance().self.getEmail() != null && tvEmail != null) {
            if (!MyApplication.getInstance().self.isInvail()) {
                tvEmail.setText(MyApplication.getInstance().self.getEmail() + "(未验证)");
            } else {
                tvEmail.setText(MyApplication.getInstance().self.getEmail() + "(已验证)");
            }
        } else {
            if (tvEmail != null)
                tvEmail.setText("请绑定邮箱");
        }
//        if (isBindEmail && tvEmail != null) {
//            tvEmail.setText(email);
//        } else if (isBindSuccess && tvEmail != null) {
//            tvEmail.setText("请去" + email + "进行验证");
//            tvEmail.setTextSize(12);
//        } else if (tvEmail != null) {
//            tvEmail.setText("请绑定邮箱");
//        }

        if(MyApplication.getInstance().self!=null&&MyApplication.getInstance().self.getPhone()!=null&&tvPhone!=null){
            if(MyApplication.getInstance().self.isInvailPhone()){
                Log.e("zds_phone",MyApplication.getInstance().self.isInvailPhone()+"");
                tvPhone.setText(MyApplication.getInstance().self.getPhone()+"(已验证)");
            }else {
                tvPhone.setText(MyApplication.getInstance().self.getPhone()+"(未验证)");
            }
        }else if(tvPhone!=null){
            tvPhone.setText("请绑定手机号");
        }
//        if (tvPhone != null)
//            tvPhone.setText(isBindPhone ? phone : "请绑定手机号");
    }


    private NetworkInfo networkInfo;

    @OnClick({R.id.rl_information, R.id.rl_modifyPwd, R.id.rl_address, R.id.rl_clean,
            R.id.rl_about, R.id.rl_feedback, R.id.rl_share, R.id.btn_exit
            , R.id.rl_bindEmail, R.id.rl_bindPhone, R.id.rl_update, R.id.rl_modifyPayPwd,R.id.rl_message,R.id.rl_cewansu,R.id.rl_news
    })
    public void onClick(View view) {

        Intent intent;
        switch (view.getId()) {

            case R.id.rl_message:
                if (MyApplication.getInstance().self == null)
                    return;
                intent = new Intent(activity, MessageActivity.class);
                startActivity(intent);

                break;
            case R.id.rl_cewansu:  // 检测当前的网速

                ProgressDlgUtil.showProgressDlg("",activity);
                ConnectivityManager connectivityManager=(ConnectivityManager) activity.getSystemService(CONNECTIVITY_SERVICE);
                 networkInfo=connectivityManager.getActiveNetworkInfo();
//                tv_type.setText(networkInfo.getTypeName());


                tv_cewansu.setText("测试中");
                rl_cewansu.setEnabled(false);
                info.hadfinishByte=0;
                info.speed=0;
                info.totalByte=1024;
                new DownloadThread().start();
                new GetInfoThread().start();

                break;
            case R.id.rl_information:
                if (MyApplication.getInstance().self == null)
                    return;
                intent = new Intent(activity, InformationActivity.class);
                startActivity(intent);

                break;
            case R.id.rl_modifyPayPwd:
                intent = new Intent(activity, ModifyPasswordActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.rl_modifyPwd:
                if (MyApplication.getInstance().self == null)
                    return;
                if (isBindPhone) {
                    intent = new Intent(activity, BindPhoneActivity.class);
                    if (isPayPwd) {
                        intent.putExtra("isFirst", false);
                        intent.putExtra("title", "修改支付密码");
                    } else {
                        intent.putExtra("isFirst", true);
                        intent.putExtra("title", "设置支付密码");
                    }
                    intent.putExtra("type", BindPhoneActivity.BINDPAYPWD);
                    startActivity(intent);
                } else {
                    Toast.makeText(activity, "请先绑定手机号！", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.rl_address:
                if (MyApplication.getInstance().self == null)
                    return;
                intent = new Intent(activity, AddressActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_clean:
                try {
                    if (!tvCache.getText().toString().equals("0K")) {
                        new PromptDialog(activity, "确定清空华人邦本地缓存数据？(此操作不可恢复)", new Complete() {
                            @Override
                            public void complete() {
                                try {
                                    DataCleanManager.clearAllCache(activity);
                                    tvCache.setText(DataCleanManager.getTotalCacheSize(activity));
                                    if (tvCache.getText().toString().equals("0K")) {
//                                        showToast("清除缓存成功！");
                                        Toast.makeText(activity, "清除缓存成功！", Toast.LENGTH_SHORT).show();
                                    } else {
//                                        showToast("清空缓存失败，请重试！");
                                        Toast.makeText(activity, "清空缓存失败，请重试！", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Log.i("zjz", "清空失败");
                                    Toast.makeText(activity, "清除缓存成功！", Toast.LENGTH_SHORT).show();
                                    tvCache.setText("0K");
                                    e.printStackTrace();
                                }
                            }
                        }).show();
                    } else {
                        Toast.makeText(activity, "已经没有缓存！", Toast.LENGTH_SHORT).show();
//                        showToast("已经没有缓存！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.rl_about:
                intent = new Intent(activity, AboutWeActivity.class);
                intent.putExtra("url", "file:///android_asset/aboutus.html");
                intent.putExtra("title", "关于我们");
                intent.putExtra("aboutus", true);
                startActivity(intent);
                break;
            case R.id.rl_feedback:

                intent = new Intent(activity, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_share:

                intent = new Intent(activity, ShareActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_exit:

                ((ActivityHome)getActivity()).logout("true");

                MyApplication.getInstance().getTongDao().logout(activity.getApplicationContext()); //退出同道
                TSChatManager.close();
                Bundle data = new Bundle();
                data.putBoolean("login_out", true);
                ActivityStack.startActivity(getActivity(),WXEntryActivity.class, data);
//                ActivityStack.startActivity(getActivity(),ThinksnsActivity.class, data);
                //注销极光推送
                ShareSDKManager.unregister();
                getActivity().finish();

              //  MyApplication.getInstance().getMainActivity().logout("true");
//                finish();
                break;
//            case R.id.rl_left:
//                finish();
//                break;

            case R.id.rl_bindEmail:
                intent = new Intent();
                intent.setClass(activity, BindPhoneActivity.class);
                intent.putExtra("type", BindPhoneActivity.BINDEMAIL);
                if (isBindEmail) {

                    intent.putExtra("title", "修改绑定邮箱");
                    intent.putExtra("isFirst", false);
                } else {
                    intent.putExtra("title", "绑定邮箱");
                    intent.putExtra("isFirst", false);
                }
                startActivityForResult(intent, 4);
                break;

            case R.id.rl_bindPhone:
                intent = new Intent();
                intent.setClass(activity, BindPhoneActivity.class);
                if (isBindPhone) {
                    intent.putExtra("isFirst", false);
                    intent.putExtra("title", "修改绑定手机号");
                } else {
                    intent.putExtra("isFirst", false);
                    intent.putExtra("title", "绑定手机号");
                }
                intent.putExtra("type", BindPhoneActivity.BINDPHONE);
                startActivityForResult(intent, 3);

                break;

            case R.id.rl_update:
//                upDateAPP();
                break;
            case R.id.rl_news:
                intent=new Intent(activity, HuanqiuActivity.class);
                startActivity(intent);
                break;
        }

    }


    class DownloadThread extends Thread
    {

        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            String url_string="http://qhcdn.oss-cn-hangzhou.aliyuncs.com/hqb_abc_img%2F%E5%90%AF%E5%8A%A8%E9%A1%B551484041887902.jpg";
            long start_time,cur_time;
            URL url;
            URLConnection connection;
            InputStream iStream;

            try
            {
                url=new URL(url_string);
                connection=url.openConnection();

                info.totalByte=connection.getContentLength();

                iStream=connection.getInputStream();
                start_time=System.currentTimeMillis();
                while(iStream.read()!=-1 && flag)
                {

                    info.hadfinishByte++;
                    cur_time=System.currentTimeMillis();
                    if(cur_time-start_time==0)
                    {
                        info.speed=1000;
                    }
                    else {
                        info.speed=info.hadfinishByte/(cur_time-start_time)*1000;
                    }
                }
                iStream.close();
            } catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    class GetInfoThread extends Thread
    {

        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            double sum,counter;
            int cur_speed,ave_speed;
            try
            {
                sum=0;
                counter=0;
                while(info.hadfinishByte<info.totalByte && flag)
                {
                    Thread.sleep(1000);

                    sum+=info.speed;
                    counter++;

                    cur_speed=(int) info.speed;
                    ave_speed=(int) (sum/counter);
                    Log.e("Test", "cur_speed:"+info.speed/1024+"KB/S ave_speed:"+ave_speed/1024);
                    Message msg=new Message();
                    msg.arg1=((int)info.speed/1024);
                    msg.arg2=((int)ave_speed/1024);
                    msg.what=0x123;
                    handler.sendMessage(msg);
                }
                if(info.hadfinishByte==info.totalByte && flag)
                {
                    handler.sendEmptyMessage(0x100);
                }
            } catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onDestroy() {
        flag=false;
        super.onDestroy();
    }

    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        flag=true;
        super.onResume();
    }

/*    private void startAnimation(int cur_speed)
    {
        cur_degree=getDegree(cur_speed);

        RotateAnimation rotateAnimation=new RotateAnimation(last_degree, cur_degree, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(1000);
        last_degree=cur_degree;
        img.startAnimation(rotateAnimation);
    }*/

    private int getDegree(double cur_speed)
    {
        int ret=0;
        if(cur_speed>=0 && cur_speed<=512)
        {
            ret=(int) (15.0*cur_speed/128.0);
        }
        else if(cur_speed>=512 && cur_speed<=1024)
        {
            ret=(int) (60+15.0*cur_speed/256.0);
        }
        else if(cur_speed>=1024 && cur_speed<=10*1024)
        {
            ret=(int) (90+15.0*cur_speed/1024.0);
        }else {
            ret=180;
        }
        return ret;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (data != null && resultCode == 1) {
//            modifyNickName(data.getStringExtra("name"));
//        }
//        if (data != null && requestCode == 2) {
//
//            modifySex(data.getStringExtra("sex"));
//            MyApplication.getInstance().self.setSex(data.getStringExtra("sex"));
//        }
        if (data != null && requestCode == 1) {
            MyApplication.getInstance().getMainActivity().logout("true");
//            finish();
        }
        if (data != null && requestCode == 3) {
            initUser();
//            personalTvVPhone.setText(data.getStringExtra("bind_phone"));
        }
        if (data != null && requestCode == 4) {
            String email = data.getStringExtra("bind_email");
            if(TextUtils.isEmpty(email)){
                return;
            }
            if (email.length() > 8) {
                String start = email.substring(0, 3);
                String end = email.substring(email.length() - 5, email.length());
                tvEmail.setText(start + "****" + end + "(未验证)");
            } else {
                tvEmail.setText(email.substring(0) + "(未验证)");
            }
            String userName = Util.preference.getString("lizai_userName", "");
            String pwd = Util.preference.getString("lizai_pwd", "");
            if (userName.length() > 0 && pwd.length() > 0) {

                sendLogin(userName, pwd);
            }
//            initUser();
//            personalTvVEmail.setText(data.getStringExtra("bind_email"));
        }
    }


    private void sendLogin(String userName, String pwd) {
        String param = "uname=" + userName + "&upass=" + pwd + "&from=华侨帮";
        HttpRequest.sendPost(TLUrl.getInstance().URL_login, param, new HttpRevMsg() {
            @Override
            public void revMsg(String msg) {
                NoticeDialog.stopNoticeDlg();
                if (msg.length() == 0) {
                    NoticeDialog.stopNoticeDlg();
                    Toast.makeText(activity, "网络异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONObject json = new JSONObject(msg);
                    if (json != null && json.has("code")) {
                        int code = json.getInt("code");
                        if (code == 1) {
                            String token = json.getString("result");
                            Util.token = token;
                            LoginForToken(token);
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    private void LoginForToken(String token) {

        Log.i("tga", "LoginForToken=====" + token);
        HttpRequest.sendPost(TLUrl.getInstance().URL_oauth + "?iou=1", "token=" + token,
                new HttpRevMsg() {
                    @Override
                    public void revMsg(String msg) {
                        // TODO Auto-generated method stub
                        try {
                            final JSONObject json = new JSONObject(msg);
                            if (json != null && json.has("code")) {
                                int code = json.getInt("code");
                                if (code == 1) {
                                    Intent intent = new Intent(
                                            "com.abct.occft.hq.login");
                                    intent.putExtra("type", "login");
                                    intent.putExtra("msg",
                                            json.getString("result"));
                                    activity.sendBroadcast(intent);

                                    // final String result = json
                                    // .getString("result");
                                    // activity.shouYe.handler
                                    // .post(new Runnable() {
                                    //
                                    // @Override
                                    // public void run() {
                                    // // TODO Auto-generated
                                    // // method stub
                                    // activity.shouYe
                                    // .loginResult(result);
                                    // }
                                    // });
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }


    /*private void upDateAPP() {

//        tvVUpdateDate.setText(getResources().getString(R.string.update_date) + Util.format.format(new Date()));
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("正在检查更新");
        dialog.show();
        PgyUpdateManager.register(activity,
                new UpdateManagerListener() {


                    @Override
                    public void onUpdateAvailable(final String result) {
                        dialog.dismiss();

                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        new PromptDialog(activity, "有新版本，是否更新?",
                                new Complete() {

                                    @Override
                                    public void complete() {

                                        Log.i("tga", "check Bulid====");
                                        startDownloadTask(activity,
                                                appBean.getDownloadURL());
                                    }
                                }).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        dialog.dismiss();
                        Toast.makeText(activity, "已经是最新版", Toast.LENGTH_SHORT).show();
                        Log.i("tga", "check Bulid====");
                    }
                });

        if (!MyApplication.isupdate) {
            UmengUpdateAgent.update(activity);
            MyApplication.isupdate = true;
        }

    }*/


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        activity.unregisterReceiver(receiver);
        myBroadCastReceiver.unRegister();
    }

    @Override
    public void loginSuccess() {
        initData();
    }
}
