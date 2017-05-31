package com.abcs.huaqiaobang.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.activity.AboutWeActivity;
import com.abcs.huaqiaobang.activity.FeedbackActivity;
import com.abcs.huaqiaobang.activity.HelpAskActivity;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.wxapi.ShareActivity;
import com.abcs.sociax.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/1/21.
 */
public class MyMoreFragment extends Fragment implements View.OnClickListener {

    @InjectView(R.id.my_rl_guanyuwomen)
    RelativeLayout myRlGuanyuwomen;
    @InjectView(R.id.my_rl_help)
    RelativeLayout myRlHelp;
    @InjectView(R.id.my_rl_yijian)
    RelativeLayout myRlYijian;
    @InjectView(R.id.my_rl_share)
    RelativeLayout myRlShare;
    @InjectView(R.id.tv_update)
    TextView tvUpdate;
    @InjectView(R.id.my_rl_update)
    RelativeLayout myRlUpdate;
    @InjectView(R.id.tv_vUpdate_version)
    TextView tvVUpdateVersion;
    @InjectView(R.id.tv_vUpdate_date)
    TextView tvVUpdateDate;
    @InjectView(R.id.btn_sound)
    ToggleButton btnSound;
    @InjectView(R.id.btn_shake)
    ToggleButton btnShake;
    private View view;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (view == null) {
            view = inflater.inflate(R.layout.fragment_my_more_item, null);
        }
        ButterKnife.inject(this, view);
        preferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        init();

        return view;
    }

    private void init() {
        myRlGuanyuwomen.setOnClickListener(this);
        myRlHelp.setOnClickListener(this);
        myRlShare.setOnClickListener(this);
        myRlYijian.setOnClickListener(this);
        myRlUpdate.setOnClickListener(this);

        btnSound.setOnClickListener(this);
        btnShake.setOnClickListener(this);
        btnShake.setChecked(MyApplication.isShake);
        btnSound.setChecked(MyApplication.isSound);
        tvVUpdateVersion.setText(getResources().getString(R.string.current_version) + getVersion());
        tvVUpdateDate.setText(getResources().getString(R.string.update_date) + Util.format.format(getInstallTime()));

    }

    public String getVersion() {
        try {
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            String version = info.versionName;
            return "V" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return this.getString(R.string.can_not_find_version_name);
        }
    }

    public long getInstallTime() {
        try {
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            long time = info.firstInstallTime;
            return time;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
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
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.my_rl_guanyuwomen:
                intent = new Intent(getActivity(), AboutWeActivity.class);
                intent.putExtra("url", "file:///android_asset/aboutus.html");
                intent.putExtra("title", "关于我们");
                intent.putExtra("aboutus", true);
                startActivity(intent);
                break;
            case R.id.my_rl_help:
                intent = new Intent(getActivity(), HelpAskActivity.class);
                startActivity(intent);
                break;
            case R.id.my_rl_yijian:
                intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.my_rl_share:
                intent = new Intent(getActivity(), ShareActivity.class);
                startActivity(intent);
                break;
            case R.id.my_rl_update:
//                upDateAPP();
                break;
            case R.id.btn_sound:
                btnSound.setChecked(!MyApplication.isSound);
                MyApplication.isSound = !MyApplication.isSound;
                preferences.edit().putBoolean("isSound", MyApplication.isSound).commit();
                break;
            case R.id.btn_shake:
                btnShake.setChecked(!MyApplication.isShake);
                MyApplication.isShake = !MyApplication.isShake;
                preferences.edit().putBoolean("isShake", MyApplication.isShake).commit();
                break;

        }

    }

   /* private void upDateAPP() {

        tvVUpdateDate.setText(getResources().getString(R.string.update_date) + Util.format.format(new Date()));
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("正在检查更新");
        dialog.show();
        PgyUpdateManager.register(getActivity(),
                new UpdateManagerListener() {


                    @Override
                    public void onUpdateAvailable(final String result) {
                        dialog.dismiss();

                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        new PromptDialog(getActivity(), "有新版本，是否更新?",
                                new Complete() {

                                    @Override
                                    public void complete() {

                                        Log.i("tga", "check Bulid====");
                                        startDownloadTask(getActivity(),
                                                appBean.getDownloadURL());
                                    }
                                }).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "已经是最新版", Toast.LENGTH_SHORT).show();
                        Log.i("tga", "check Bulid====");
                    }
                });


        if (!MyApplication.isupdate) {
            UmengUpdateAgent.update(getActivity());
            MyApplication.isupdate = true;
        }

    }*/
}
