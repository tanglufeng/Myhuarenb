package com.abcs.haiwaigou.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.fragment.ClubFragment;
import com.abcs.huaqiaobang.fragment.HuaTiFragment;
import com.abcs.huaqiaobang.fragment.HuoDongFragment;
import com.abcs.huaqiaobang.model.BaseFragmentActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BangActivity extends BaseFragmentActivity {

    @InjectView(R.id.radioGroup)
    RadioGroup radioGroup;
    @InjectView(R.id.content)
    FrameLayout content;
    private ClubFragment clubFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private HuoDongFragment huoDongFragment;
    private HuaTiFragment huaTiFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bang);
        ButterKnife.inject(this);

        manager = getSupportFragmentManager();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                transaction = manager.beginTransaction();
                hintFragment();
                switch (checkedId) {

                    case R.id.rb_julebu:

                        if (clubFragment == null) {
                            clubFragment = new ClubFragment();
                            transaction.add(R.id.content, clubFragment);
                        } else {
                            transaction.show(clubFragment);
                        }


                        break;
                    case R.id.rb_huodong:
                        if (huoDongFragment == null) {
                            huoDongFragment = new HuoDongFragment();

                            transaction.add(R.id.content, huoDongFragment);
                        } else {
                            transaction.show(huoDongFragment);
                        }
                        break;
                    case R.id.rb_huati:
                        if (huaTiFragment == null) {
                            huaTiFragment = new HuaTiFragment();

                            transaction.add(R.id.content, huaTiFragment);
                        } else {
                            transaction.show(huaTiFragment);
                        }
                        break;

                }
                transaction.commit();
            }

        });
        ((RadioButton) radioGroup.findViewById(R.id.rb_julebu)).setChecked(true);

    }

    private void hintFragment() {

        if (huaTiFragment != null)
            transaction.hide(huaTiFragment);
        if (huoDongFragment != null)
            transaction.hide(huoDongFragment);
        if (huaTiFragment != null)
            transaction.hide(huaTiFragment);

    }
}
